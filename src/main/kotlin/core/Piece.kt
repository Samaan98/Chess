package core

data class Piece(
    val type: PieceType,
    val isWhite: Boolean
) {
    val symbol: Char
        get() = when (type) {
            PieceType.PAWN -> blackOrWhite('♟', '♙')
            PieceType.ROOK -> blackOrWhite('♜', '♖')
            PieceType.KNIGHT -> blackOrWhite('♞', '♘')
            PieceType.BISHOP -> blackOrWhite('♝', '♗')
            PieceType.QUEEN -> blackOrWhite('♛', '♕')
            PieceType.KING -> blackOrWhite('♚', '♔')
        }

    private fun <T> blackOrWhite(black: T, white: T): T {
        return if (isWhite) white else black
    }
}