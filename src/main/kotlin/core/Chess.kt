package core

import core.board.BoardFactory
import core.command.Command
import core.command.CommandProcessor
import core.command.CommandResult
import core.moves_calculator.MovesCalculator
import ui.BoardUi
import ui.InputProcessor

//todo тесты на всю логику
// объявить ничью
// пат
// сдаться
// ход назад
// таймер
// король может атаковать короля противника лишь при помощи «вскрытого» шаха
// ПРОЧИТАТЬ ПРО ВСЕ КОРНЕРКЕЙСЫ, КОТОРЫХ ПРОСТО, СУКА, МИЛЛИОН
// параллельные вычисления для сложных операций
class Chess {

    val board = BoardFactory().createBoard()

    private val movesCalculator = MovesCalculator()
    private val commandProcessor = CommandProcessor(board, movesCalculator)

    val boardUi = BoardUi()
    private val inputProcessor = InputProcessor(boardUi)

    private var isWhiteMove = true

    fun makeMove(input: String): CommandResult {

        val command = inputProcessor.parse(input)
        val result = commandProcessor.process(command, isWhiteMove)

        if (command !is Command.GetAvailableMoves) {
            isWhiteMove = !isWhiteMove
        }

        return result
    }
}