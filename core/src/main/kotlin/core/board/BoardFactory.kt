package core.board

import core.piece.Piece
import core.piece.PieceType

internal class BoardFactory {

    // todo инициализация в цикле
    fun createBoard() = Board(
        hashMapOf(
            // Ладьи
            0 to 0 to Piece(PieceType.ROOK, false),
            0 to 7 to Piece(PieceType.ROOK, false),
            7 to 0 to Piece(PieceType.ROOK, true),
            7 to 7 to Piece(PieceType.ROOK, true),
            // Кони
            0 to 1 to Piece(PieceType.KNIGHT, false),
            0 to 6 to Piece(PieceType.KNIGHT, false),
            7 to 1 to Piece(PieceType.KNIGHT, true),
            7 to 6 to Piece(PieceType.KNIGHT, true),
            // Слоны
            0 to 2 to Piece(PieceType.BISHOP, false),
            0 to 5 to Piece(PieceType.BISHOP, false),
            7 to 2 to Piece(PieceType.BISHOP, true),
            7 to 5 to Piece(PieceType.BISHOP, true),
            // Ферзи
            0 to 3 to Piece(PieceType.QUEEN, false),
            7 to 3 to Piece(PieceType.QUEEN, true),
            // Короли
            0 to 4 to Piece(PieceType.KING, false),
            7 to 4 to Piece(PieceType.KING, true),
            // Пешки
            *Array(16) {
                val isBlack = it < 8
                val i = if (isBlack) Board.PAWNS_BLACK_INITIAL_ROW_INDEX else Board.PAWNS_WHITE_INITIAL_ROW_INDEX
                val j = if (isBlack) it else it - 8
                i to j to Piece(PieceType.PAWN, !isBlack)
            }
        )
    )
}
