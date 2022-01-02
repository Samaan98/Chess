import core.Chess
import core.board.PawnPromotionCallback
import core.board.PawnPromotionType
import core.command.CommandResult
import core.util.ChessError
import java.io.File

private const val IS_DEBUG = false

private val chess = Chess(PawnPromotionCallbackImpl())
private val boardUi = chess.boardUi
private val boardText = BoardText(chess.board, boardUi)
private val inputCommands = File("src/main/resources", "input_commands.txt").readLines()

fun main() {
    if (IS_DEBUG && inputCommands.isNotEmpty()) {
        inputCommands.forEach {
            chess.processInputCommand(it)
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
                    boardUi.indexesToIndexesUi(lastCommandResult.data)
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
        chess.processInputCommand(input)
    }.onFailure {
        val message = if (it is ChessError) {
            when (it) {
                is ChessError.ImpossibleMove ->
                    "Невозможный ход на ${boardUi.indexesToIndexesUi(it.to)}"
                is ChessError.InvalidCommand ->
                    "Нераспознанная команда: '${it.command}'"
                is ChessError.NoFigureAtCell ->
                    "Нет фигуры на ${boardUi.indexesToIndexesUi(it.position)}"
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

private class PawnPromotionCallbackImpl : PawnPromotionCallback {

    companion object {
        private const val ORDINAL_TO_INPUT_DIFFERENCE = 1
    }

    private val promotionTypeMessage = PawnPromotionType.values()
        .joinToString(separator = "\n") { pawnPromotionType ->
            val pieceName = when (pawnPromotionType) {
                PawnPromotionType.BISHOP -> "Слон"
                PawnPromotionType.KNIGHT -> "Конь"
                PawnPromotionType.ROOK -> "Ладья"
                PawnPromotionType.QUEEN -> "Ферзь"
            }
            val ordinal = pawnPromotionType.toInputNumber()
            "$ordinal - $pieceName"
        }


    override fun getPawnPromotionType(): PawnPromotionType {
        println("На какую фигуру заменить?\n${promotionTypeMessage}")
        var pawnPromotionType: PawnPromotionType? = null
        while (pawnPromotionType == null) {
            pawnPromotionType = runCatching {
                readLine()!!.trim().toInt().toPawnPromotionType()
            }.onFailure {
                println("Ошибка, повторите ввод")
            }.getOrNull()
        }
        return pawnPromotionType
    }

    private fun PawnPromotionType.toInputNumber(): String {
        return (this.ordinal + ORDINAL_TO_INPUT_DIFFERENCE).toString()
    }

    private fun Int.toPawnPromotionType(): PawnPromotionType {
        return PawnPromotionType.values()[this - ORDINAL_TO_INPUT_DIFFERENCE]
    }
}
