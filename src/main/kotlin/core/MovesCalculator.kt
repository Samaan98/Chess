package core

class MovesCalculator(private val board: Board) {

    fun calculateMoves(position: Indexes): Set<Indexes> {
        val piece = board[position] ?: error("Нет фигуры на данной клетке")
        return when (piece.type) {
            PieceType.PAWN -> calculateMoves(position, piece, ::forPawn)
            PieceType.ROOK -> calculateMoves(position, piece, ::forRook)
            PieceType.KNIGHT -> TODO()
            PieceType.BISHOP -> TODO()
            PieceType.QUEEN -> TODO()
            PieceType.KING -> TODO()
        }
    }

    //todo взятие на проходе
    //todo достижение края доски и замена фигуры
    private fun forPawn(piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>) {
        val next = i + piece.blackOrWhite(1, -1) to j
        if (board.isCellEmpty(next)) {
            moves.add(next)
            val afterNext = i + piece.blackOrWhite(2, -2) to j
            val isInInitialPosition = i == piece.blackOrWhite(
                Board.PAWNS_BLACK_INITIAL_ROW_INDEX,
                Board.PAWNS_WHITE_INITIAL_ROW_INDEX
            )
            if (isInInitialPosition && board.isCellEmpty(afterNext)) {
                moves.add(afterNext)
            }
        }

        //todo взятие (capture)
    }

    private fun forRook(piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>) {
        var canMoveLeft = true
        var canMoveUp = true
        var canMoveRight = true
        var canMoveDown = true
        var nextCellIndex = 1

        while (canMoveLeft || canMoveUp || canMoveRight || canMoveDown) {
            val nextLeft = if (canMoveLeft) i to (j - nextCellIndex) else null
            val nextUp = if (canMoveUp) (i - nextCellIndex) to j else null
            val nextRight = if (canMoveRight) i to (j + nextCellIndex) else null
            val nextDown = if (canMoveDown) (i + nextCellIndex) to j else null

            canMoveLeft = addMoveIfCanMove(piece, nextLeft, moves)
            canMoveUp = addMoveIfCanMove(piece, nextUp, moves)
            canMoveRight = addMoveIfCanMove(piece, nextRight, moves)
            canMoveDown = addMoveIfCanMove(piece, nextDown, moves)

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
}