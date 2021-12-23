package ui

import core.Command
import core.Indexes

class InputProcessor(private val boardUi: BoardUi) {

    companion object {
        private const val INVALID_COMMAND_MESSAGE = "Нераспознанная команда"
    }

    fun parse(input: String): Command {
        val commands = input.lowercase().split(" ")
        return if (commands.size == 2) {
            parseMoveCommand(commands)
        } else error(INVALID_COMMAND_MESSAGE)
    }

    private fun parseMoveCommand(commands: List<String>): Command.Move {
        return Command.Move(
            from = parseIndexes(commands[0]),
            to = parseIndexes(commands[1])
        )
    }

    private fun parseIndexes(input: String): Indexes {
        return Indexes(
            boardUi.indexesUiToIndexes[input[1]] ?: error(INVALID_COMMAND_MESSAGE),
            boardUi.lettersToIndexes[input[0]] ?: error(INVALID_COMMAND_MESSAGE)
        )
    }
}
