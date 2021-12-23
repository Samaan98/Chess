import core.Board
import core.CommandProcessor
import core.MovesCalculator
import ui.BoardUi
import ui.InputProcessor

private val board = Board()
private val boardUi = BoardUi(board)
private val movesCalculator = MovesCalculator(board)
private val inputProcessor = InputProcessor(boardUi)
private val commandProcessor = CommandProcessor(board, movesCalculator)

fun main() {
    boardUi.printBoard()

    val input = readLine()!!
    val command = inputProcessor.parse(input)
    commandProcessor.process(command)

    boardUi.printBoard()
}

private fun testMovesCalculator() {
    val position = 1 to 4
    println(movesCalculator.calculateMoves(position))
}
