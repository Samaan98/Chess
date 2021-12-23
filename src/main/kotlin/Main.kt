import core.Board
import core.Chess
import core.CommandProcessor
import core.MovesCalculator
import ui.BoardUi
import ui.InputProcessor

private val board = Board()
private val boardUi = BoardUi(board)
private val movesCalculator = MovesCalculator(board)
private val inputProcessor = InputProcessor(boardUi)
private val commandProcessor = CommandProcessor(board, movesCalculator)
private val chess = Chess(inputProcessor, commandProcessor)

fun main() {
    while (true) {
        boardUi.printBoard()

        processMove()
    }
}

private fun processMove() {
    runCatching {
        val input = readLine()!!
        chess.makeMove(input)
    }.onFailure {
        println(it.message)
        processMove()
    }
}
