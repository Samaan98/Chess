import core.*
import ui.BoardText
import ui.BoardUi
import ui.InputProcessor

private const val IS_DEBUG = false

private val board = BoardFactory().createBoard()
private val boardUi = BoardUi()
private val boardText = BoardText(board, boardUi)
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
            println(boardText.getBoard())
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
            CommandResult.Check -> {
                println("Шах")
            }
        }
    }
}

private fun processMove(): CommandResult {
    return runCatching {
        val input = readLine()!!
        chess.makeMove(input)
    }.onFailure {
        val message = if (it is ChessError) {
            when (it) {
                is ChessError.ImpossibleMove ->
                    "Невозможный ход на ${inputProcessor.indexesToIndexesUi(it.to)}"
                is ChessError.InvalidCommand ->
                    "Нераспознанная команда: '${it.command}'"
                is ChessError.NoFigureAtCell ->
                    "Нет фигуры на ${inputProcessor.indexesToIndexesUi(it.position)}"
                is ChessError.OpponentMove ->
                    "Сейчас ход ${if (it.isWhiteMove) "белых" else "чёрных"}"
            }
        } else "Неизвестная ошибка"
        println(message)
        if (IS_DEBUG) it.printStackTrace()
    }.getOrElse {
        processMove()
    }
}
