import core.Board
import core.Board.Companion.BOARD_SIZE

fun main() {
    val board = Board()

    val boardUi = buildString {
        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
                append(board.board[i to j]?.symbol ?: '.')
                append(' ')
            }
            append('\n')
        }
    }
    println(boardUi)
}