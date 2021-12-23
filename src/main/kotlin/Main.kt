import core.Board
import core.MovesCalculator
import ui.BoardUi
import ui.InputProcessor

private val board = Board()
private val boardUi = BoardUi(board)
private val movesCalculator = MovesCalculator(board)
private val inputProcessor = InputProcessor(boardUi)

fun main() {
    boardUi.printBoard()

//    testMovesCalculator()

    testInputProcessor()
}

private fun testInputProcessor() {
    println(inputProcessor.parseIndexes("h1"))
    println(inputProcessor.parseMoveCommand("e2 e4"))
}

private fun testMovesCalculator() {
    val position = 1 to 4

    movesCalculator.calculateMoves(
        position,
        board[position]!!
    ).also {
        println(it)
    }
}
