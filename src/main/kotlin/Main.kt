import core.*
import ui.BoardUi
import ui.InputProcessor

private val board = Board()
private val boardUi = BoardUi(board)
private val movesCalculator = MovesCalculator(board)
private val inputProcessor = InputProcessor(boardUi)
private val commandProcessor = CommandProcessor(board, movesCalculator)
private val chess = Chess(inputProcessor, commandProcessor)

fun main() {
    startGame()
}

private fun startGame() {
    var lastCommandResult: CommandResult = CommandResult.Success

    while (true) {
        if (lastCommandResult !is CommandResult.AvailableMoves) {
            boardUi.printBoard()
        }

        lastCommandResult = processMove()

        when (lastCommandResult) {
            is CommandResult.AvailableMoves -> {
                println(
                    inputProcessor.indexesToIndexesUi(lastCommandResult.data)
                        .takeIf { it.isNotEmpty() }
                        ?: "Нет доступных ходов"
                )
            }
            CommandResult.Success -> {
                // do nothing
            }
        }
    }
}

private fun processMove(): CommandResult {
    return runCatching {
        val input = readLine()!!
        chess.makeMove(input)
    }.onFailure {
        println(it.message)
    }.getOrElse {
        processMove()
    }
}
