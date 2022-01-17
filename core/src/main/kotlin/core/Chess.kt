package core

import core.board.PawnPromotionCallback
import core.command.CommandResult
import core.command.InputProcessor
import core.di.Deps

//todo
// тесты на всю логику
// пат
class Chess(pawnPromotionCallback: PawnPromotionCallback) {

    private val deps = Deps(pawnPromotionCallback)

    val board by deps::board
    val boardUi by deps::boardUi

    /**
     * @see InputProcessor for available commands.
     */
    fun processInputCommand(input: String): CommandResult {
        val command = deps.inputProcessor.parse(input)
        return deps.commandProcessor.process(command)
    }
}