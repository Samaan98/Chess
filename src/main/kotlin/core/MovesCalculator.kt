package core

class MovesCalculator(private val board: Board) {

    fun calculateMoves(position: Indexes, piece: Piece): Set<Indexes> {
        return when (piece.type) {
            PieceType.PAWN -> calculateMovesForPawn(position, piece)
            PieceType.ROOK -> TODO()
            PieceType.KNIGHT -> TODO()
            PieceType.BISHOP -> TODO()
            PieceType.QUEEN -> TODO()
            PieceType.KING -> TODO()
        }
    }

    //todo взятие на проходе
    //todo достижение края доски и замена фигуры
    private fun calculateMovesForPawn(position: Indexes, piece: Piece): Set<Indexes> {
        val moves = hashSetOf<Indexes>()
        val i = position.i
        val j = position.j

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

        return moves
    }
}