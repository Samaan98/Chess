import core.Chess
import core.board.BoardFactory
import core.command.CommandProcessor
import core.command.CommandResult
import core.moves_calculator.MovesCalculator
import core.util.ChessError
import ui.BoardText
import ui.BoardUi
import ui.InputProcessor
import java.io.File

private const val IS_DEBUG = false

private val board = BoardFactory().createBoard()
private val boardUi = BoardUi()
private val boardText = BoardText(board, boardUi)
private val movesCalculator = MovesCalculator()
private val inputProcessor = InputProcessor(boardUi)
private val commandProcessor = CommandProcessor(board, movesCalculator)
private val chess = Chess(inputProcessor, commandProcessor)
private val inputCommands = File("info", "input_commands.txt").readLines()

fun main() {
    if (IS_DEBUG && inputCommands.isNotEmpty()) {
        inputCommands.forEach {
            chess.makeMove(it)
        }
    }
    startGame()
}

private fun startGame() {
    var lastCommandResult: CommandResult = CommandResult.Success

    while (true) {
        if (lastCommandResult !is CommandResult.AvailableMoves) {
            println(boardText.getBoard())
        }
        if (lastCommandResult == CommandResult.Checkmate) break

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
            CommandResult.Checkmate -> {
                println("Шах и мат!")
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
