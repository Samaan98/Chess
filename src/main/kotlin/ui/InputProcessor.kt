package ui

import core.Command
import core.Indexes

class InputProcessor(private val boardUi: BoardUi) {

    companion object {
        private const val INVALID_COMMAND_MESSAGE = "Invalid command"
    }

    fun parseMoveCommand(input: String): Command.Move {
        val commands = input.lowercase().split(" ")
        return Command.Move(
            from = parseIndexes(commands[0]),
            to = parseIndexes(commands[1])
        )
    }

    fun parseIndexes(input: String): Indexes {
        return Indexes(
            boardUi.lettersToIndexes[input[0]] ?: error(INVALID_COMMAND_MESSAGE),
            boardUi.indexesUiToIndexes[input[1]] ?: error(INVALID_COMMAND_MESSAGE)
        )
    }
}
