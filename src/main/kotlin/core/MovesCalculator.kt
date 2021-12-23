package core

class MovesCalculator(private val board: Board) {

    fun calculateMoves(position: Indexes): Set<Indexes> {
        val piece = board[position] ?: error("Нет фигуры на данной клетке")
        return when (piece.type) {
            PieceType.PAWN -> calculateMoves(position, piece, ::forPawn)
            PieceType.ROOK -> calculateMoves(position, piece, ::forRook)
            PieceType.KNIGHT -> TODO()
            PieceType.BISHOP -> calculateMoves(position, piece, ::forBishop)
            PieceType.QUEEN -> TODO()
            PieceType.KING -> TODO()
        }
    }

    //todo взятие на проходе
    //todo достижение края доски и замена фигуры
    private fun forPawn(piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>) {
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
            addMoveIfPawnCanCapture(piece, it, moves)
        }
    }

    private fun addMoveIfPawnCanCapture(
        piece: Piece,
        nextPosition: Indexes,
        moves: MutableSet<Indexes>
    ) {
        if (nextPosition.isInBounds && board.isEnemy(nextPosition, piece.isWhite)) {
            moves.add(nextPosition)
        }
    }

    private fun forRook(piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>) {
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

            canMoveLeft = addMoveIfCanMove(piece, nextLeft, moves)
            canMoveUp = addMoveIfCanMove(piece, nextUp, moves)
            canMoveRight = addMoveIfCanMove(piece, nextRight, moves)
            canMoveDown = addMoveIfCanMove(piece, nextDown, moves)

            nextCellIndex++
        }
    }

    private fun forBishop(piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>) {
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

            canMoveUpLeft = addMoveIfCanMove(piece, nextUpLeft, moves)
            canMoveDownLeft = addMoveIfCanMove(piece, nextDownLeft, moves)
            canMoveUpRight = addMoveIfCanMove(piece, nextUpRight, moves)
            canMoveDownRight = addMoveIfCanMove(piece, nextDownRight, moves)

            nextCellIndex++
        }
    }

    private fun calculateMoves(
        position: Indexes,
        piece: Piece,
        forPiece: (piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>) -> Unit
    ): Set<Indexes> {
        val moves = hashSetOf<Indexes>()
        val i = position.i
        val j = position.j

        forPiece(piece, i, j, moves)

        return moves
    }

    /**
     * @return can move further after this move
     */
    private fun addMoveIfCanMove(
        piece: Piece,
        nextPosition: Indexes?,
        moves: MutableSet<Indexes>
    ): Boolean {
        if (nextPosition == null) return false
        val isEnemy = board.isEnemy(nextPosition, piece.isWhite)
        return if (nextPosition.isInBounds && (board.isCellEmpty(nextPosition) || isEnemy)) {
            moves.add(nextPosition)
            !isEnemy
        } else false
    }

    private inline fun nextMoveIfCanMoveOrNull(canMove: Boolean, nextMove: () -> Indexes): Indexes? {
        return if (canMove) nextMove() else null
    }
}