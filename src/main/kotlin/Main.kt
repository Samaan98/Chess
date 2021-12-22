import core.Board
import ui.BoardUi

fun main() {
    val board = Board()
    val boardUi = BoardUi(board)

    boardUi.printBoard()
}