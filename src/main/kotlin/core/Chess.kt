package core

import core.command.Command
import core.command.CommandProcessor
import core.command.CommandResult
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
class Chess(
    private val inputProcessor: InputProcessor,
    private val commandProcessor: CommandProcessor
) {

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