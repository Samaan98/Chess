package core.board

import core.piece.Piece
import core.piece.PieceType
import core.util.Indexes

internal class BoardFactory {

    fun createBoard() = Board(
        mutableMapOf<Indexes, Piece>().apply {
            for (j in Board.INDICES) {
                val pieceType = when (j) {
                    0, 7 -> PieceType.ROOK
                    1, 6 -> PieceType.KNIGHT
                    2, 5 -> PieceType.BISHOP
                    3 -> PieceType.QUEEN
                    4 -> PieceType.KING
                    else -> error("No piece is associated with index $j")
                }
                putPieceAndPawnForBothSides(j, pieceType)
            }
        }
    )

    private fun MutableMap<Indexes, Piece>.putPieceAndPawnForBothSides(
        j: Int,
        pieceType: PieceType
    ) {
        val colors = arrayOf(true, false)
        val types = arrayOf(pieceType, PieceType.PAWN)

        colors.forEach { isWhite ->
            types.forEach { type ->
                val position = if (isWhite) {
                    when (type) {
                        PieceType.PAWN -> Board.PAWNS_WHITE_INITIAL_ROW_INDEX
                        else -> Board.PIECES_WHITE_INITIAL_ROW_INDEX
                    }
                } else {
                    when (type) {
                        PieceType.PAWN -> Board.PAWNS_BLACK_INITIAL_ROW_INDEX
                        else -> Board.PIECES_BLACK_INITIAL_ROW_INDEX
                    }
                } to j
                put(position, Piece(type, isWhite))
            }
        }
    }
}
