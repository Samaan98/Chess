package core

import ui.InputProcessor

class Chess(
    private val inputProcessor: InputProcessor,
    private val commandProcessor: CommandProcessor
) {

    private var isWhiteMove = true

    fun makeMove(input: String) {
        val command = inputProcessor.parse(input)
        commandProcessor.process(command, isWhiteMove)

        isWhiteMove = !isWhiteMove
    }
}