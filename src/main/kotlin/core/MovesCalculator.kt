package core

class MovesCalculator {

    /**
     * Calculates available moves for a piece at [position].
     * Such moves that lead to check are filtered.
     * @return a set of available moves.
     */
    fun calculateMovesWithCheckMovesFiltered(
        position: Indexes,
        forWhite: Boolean,
        board: Board
    ): Set<Indexes> {
        return calculateMoves(position, board).filter { potentialMoveTo ->
            board.copy().apply { // copy the board
                move(position, potentialMoveTo) // move the piece on the copied board
            }.let { modifiedCopiedBoard ->
                !isCheck(forWhite = forWhite, board = modifiedCopiedBoard) // check if this move leads to check
            }
        }.toSet()
    }

    /**
     * Detects if there's a check for [forWhite].
     */
    fun isCheck(forWhite: Boolean, board: Board): Boolean {
        val kingPosition = board.kingsPositions[forWhite] ?: error("No king on board")

        return board.getAllEnemyPieces(forWhite).keys.any { enemyPiecePosition ->
            calculateMoves(enemyPiecePosition, board).any { potentialCheckMove ->
                potentialCheckMove == kingPosition
            }
        }
    }

    /**
     * Detects if there's a checkmate for [forWhite].
     * If [forWhite] has any moves that can possibly get king out of check, then there's no checkmate.
     */
    fun isCheckmate(forWhite: Boolean, board: Board): Boolean {
        val hasAnyMoves = board.getAllPieces(forWhite = forWhite).keys.any { position ->
            calculateMovesWithCheckMovesFiltered(position, forWhite, board).isNotEmpty()
        }
        return !hasAnyMoves
    }

    private fun calculateMoves(position: Indexes, board: Board): Set<Indexes> {
        val piece = board[position] ?: errorNoFigureAtCell(position)
        return when (piece.type) {
            PieceType.PAWN -> calculateMoves(position, piece, ::forPawn, board)
            PieceType.ROOK -> calculateMoves(position, piece, ::forRook, board)
            PieceType.KNIGHT -> calculateMoves(position, piece, ::forKnight, board)
            PieceType.BISHOP -> calculateMoves(position, piece, ::forBishop, board)
            PieceType.QUEEN -> calculateMoves(position, piece, ::forQueen, board)
            PieceType.KING -> calculateMoves(position, piece, ::forKing, board)
        }
    }

    //todo взятие на проходе
    //todo достижение края доски и замена фигуры
    private fun forPawn(piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>, board: Board) {
        val nextIBlackOrWhite = piece.blackOrWhite(1, -1)
        val nextI = i + nextIBlackOrWhite
        val next = nextI to j
        if (board.isCellEmpty(next)) {
            moves.add(next)
            val afterNext = nextI + nextIBlackOrWhite to j
            val isInInitialPosition = i == piece.blackOrWhite(
                Board.PAWNS_BLACK_INITIAL_ROW_INDEX,
                Board.PAWNS_WHITE_INITIAL_ROW_INDEX
            )
            if (isInInitialPosition && board.isCellEmpty(afterNext)) {
                moves.add(afterNext)
            }
        }

        val nextCaptureLeft = nextI to j - 1
        val nextCaptureRight = nextI to j + 1
        arrayOf(nextCaptureLeft, nextCaptureRight).forEach {
            if (it.isInBounds && board.isEnemy(it, piece.isWhite)) {
                moves.add(it)
            }
        }
    }

    private fun forRook(piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>, board: Board) {
        var canMoveLeft = true
        var canMoveUp = true
        var canMoveRight = true
        var canMoveDown = true
        var nextCellIndex = 1

        while (canMoveLeft || canMoveUp || canMoveRight || canMoveDown) {
            val nextLeft = nextMoveIfCanMoveOrNull(canMoveLeft) {
                i to (j - nextCellIndex)
            }
            val nextUp = nextMoveIfCanMoveOrNull(canMoveUp) {
                (i - nextCellIndex) to j
            }
            val nextRight = nextMoveIfCanMoveOrNull(canMoveRight) {
                i to (j + nextCellIndex)
            }
            val nextDown = nextMoveIfCanMoveOrNull(canMoveDown) {
                (i + nextCellIndex) to j
            }

            canMoveLeft = addMoveIfCanMove(piece, nextLeft, moves, board)
            canMoveUp = addMoveIfCanMove(piece, nextUp, moves, board)
            canMoveRight = addMoveIfCanMove(piece, nextRight, moves, board)
            canMoveDown = addMoveIfCanMove(piece, nextDown, moves, board)

            nextCellIndex++
        }
    }

    private fun forKnight(piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>, board: Board) {
        val iOneUp = i - 1
        val iOneDown = i + 1
        val iTwoUp = i - 2
        val iTwoDown = i + 2
        val jOneLeft = j - 1
        val jOneRight = j + 1
        val jTwoLeft = j - 2
        val jTwoRight = j + 2

        val oneUpTwoLeft = iOneUp to jTwoLeft
        val twoUpOneLeft = iTwoUp to jOneLeft
        val twoUpOneRight = iTwoUp to jOneRight
        val oneUpTwoRight = iOneUp to jTwoRight
        val oneDownTwoRight = iOneDown to jTwoRight
        val twoDownOneRight = iTwoDown to jOneRight
        val twoDownOneLeft = iTwoDown to jOneLeft
        val oneDownTwoLeft = iOneDown to jTwoLeft

        arrayOf(
            oneUpTwoLeft,
            twoUpOneLeft,
            twoUpOneRight,
            oneUpTwoRight,
            oneDownTwoRight,
            twoDownOneRight,
            twoDownOneLeft,
            oneDownTwoLeft
        ).forEach {
            if (it.isInBounds && (board.isCellEmpty(it) || board.isEnemy(it, piece.isWhite))) {
                moves.add(it)
            }
        }
    }

    private fun forBishop(piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>, board: Board) {
        var canMoveUpLeft = true
        var canMoveDownLeft = true
        var canMoveUpRight = true
        var canMoveDownRight = true
        var nextCellIndex = 1

        while (canMoveUpLeft || canMoveDownLeft || canMoveUpRight || canMoveDownRight) {
            val nextIUp = i - nextCellIndex
            val nextIDown = i + nextCellIndex
            val nextJLeft = j - nextCellIndex
            val nextJRight = j + nextCellIndex

            val nextUpLeft = nextMoveIfCanMoveOrNull(canMoveUpLeft) {
                nextIUp to nextJLeft
            }
            val nextDownLeft = nextMoveIfCanMoveOrNull(canMoveDownLeft) {
                nextIDown to nextJLeft
            }
            val nextUpRight = nextMoveIfCanMoveOrNull(canMoveUpRight) {
                nextIUp to nextJRight
            }
            val nextDownRight = nextMoveIfCanMoveOrNull(canMoveDownRight) {
                nextIDown to nextJRight
            }

            canMoveUpLeft = addMoveIfCanMove(piece, nextUpLeft, moves, board)
            canMoveDownLeft = addMoveIfCanMove(piece, nextDownLeft, moves, board)
            canMoveUpRight = addMoveIfCanMove(piece, nextUpRight, moves, board)
            canMoveDownRight = addMoveIfCanMove(piece, nextDownRight, moves, board)

            nextCellIndex++
        }
    }

    // Queen moves could be interpreted as a combination of Rook and Bishop moves
    private fun forQueen(piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>, board: Board) {
        forRook(piece, i, j, moves, board)
        forBishop(piece, i, j, moves, board)
    }

    //todo рокировка
    private fun forKing(piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>, board: Board) {
        val iVariants = listOf(i - 1, i, i + 1)
        val jVariants = listOf(j - 1, j, j + 1)

        for (iVariant in iVariants) {
            for (jVariant in jVariants) {
                if (iVariant == i && jVariant == j) continue
                val move = iVariant to jVariant
                if (piece.canMoveOrCapture(move, board)) {
                    moves.add(move)
                }
            }
        }
    }

    private inline fun calculateMoves(
        position: Indexes,
        piece: Piece,
        forPiece: (piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>, board: Board) -> Unit,
        board: Board
    ): Set<Indexes> {
        val moves = hashSetOf<Indexes>()
        val i = position.i
        val j = position.j

        forPiece(piece, i, j, moves, board)

        return moves
    }

    /**
     * @return can move further after this move
     */
    private fun addMoveIfCanMove(
        piece: Piece,
        nextPosition: Indexes?,
        moves: MutableSet<Indexes>,
        board: Board
    ): Boolean {
        if (nextPosition == null) return false
        return if (piece.canMoveOrCapture(nextPosition, board)) {
            moves.add(nextPosition)
            !board.isEnemy(nextPosition, piece.isWhite)
        } else false
    }

    private fun Piece.canMoveOrCapture(position: Indexes, board: Board): Boolean {
        return position.isInBounds && (board.isCellEmpty(position) || board.isEnemy(position, isWhite))
    }

    private inline fun nextMoveIfCanMoveOrNull(canMove: Boolean, nextMove: () -> Indexes): Indexes? {
        return if (canMove) nextMove() else null
    }
}