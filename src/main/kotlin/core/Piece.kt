package core

data class Piece(
    val type: PieceType,
    val isWhite: Boolean
) {
    fun <T> blackOrWhite(black: T, white: T): T {
        return if (isWhite) white else black
    }

    fun <T> doForBlackOrWhite(black: () -> T, white: () -> T): T {
        return blackOrWhite(black, white).invoke()
    }
}