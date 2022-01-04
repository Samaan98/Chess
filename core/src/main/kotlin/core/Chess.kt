package core

import core.board.PawnPromotionCallback
import core.command.CommandResult
import core.di.Deps

//todo тесты на всю логику
// модуль зависимостей
// объявить ничью
// пат
// сдаться
// ход назад
// таймер
// король может атаковать короля противника лишь при помощи «вскрытого» шаха
// ПРОЧИТАТЬ ПРО ВСЕ КОРНЕРКЕЙСЫ, КОТОРЫХ ПРОСТО, СУКА, МИЛЛИОН
// параллельные вычисления для сложных операций
class Chess(pawnPromotionCallback: PawnPromotionCallback) {

    private val deps = Deps(pawnPromotionCallback)

    val board by deps::board
    val boardUi by deps::boardUi

    fun processInputCommand(input: String): CommandResult {
        val command = deps.inputProcessor.parse(input)
        return deps.commandProcessor.process(command)
    }
}