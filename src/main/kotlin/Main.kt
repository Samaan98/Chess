import core.Board
import core.MovesCalculator
import ui.BoardUi

fun main() {
    val board = Board()
    val boardUi = BoardUi(board)
    val movesCalculator = MovesCalculator(board)

    boardUi.printBoard()

    fun test() {
        val position = 1 to 4

        movesCalculator.calculateMoves(
            position,
            board[position]!!
        ).also {
            println(it)
        }
    }
    test()
}
