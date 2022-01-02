package core.board

import core.piece.PieceType

enum class PawnPromotionType {
    BISHOP,
    KNIGHT,
    ROOK,
    QUEEN;

    internal fun toPieceType(): PieceType {
        return when (this) {
            BISHOP -> PieceType.BISHOP
            KNIGHT -> PieceType.KNIGHT
            ROOK -> PieceType.ROOK
            QUEEN -> PieceType.QUEEN
        }
    }
}