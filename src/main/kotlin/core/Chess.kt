package core

import ui.InputProcessor

//todo тесты на всю логику
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