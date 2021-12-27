package core.piece

data class Piece internal constructor(
    val type: PieceType,
    val isWhite: Boolean
) {
    fun <T> blackOrWhite(black: T, white: T): T {
        return if (isWhite) white else black
    }

    inline fun <T> doForBlackOrWhite(black: () -> T, white: () -> T): T {
        return if (isWhite) white.invoke() else black.invoke()
    }
}