package ui

import core.Command
import core.Indexes
import core.i
import core.j

class InputProcessor(private val boardUi: BoardUi) {

    companion object {
        private const val INVALID_COMMAND_MESSAGE = "Нераспознанная команда"
    }

    fun parse(input: String): Command {
        val commands = input.lowercase().split(" ")
        return when (commands.size) {
            2 -> parseMoveCommand(commands)
            1 -> parseGetAvailableMovesCommand(commands.first())
            else -> error(INVALID_COMMAND_MESSAGE)
        }
    }

    fun indexesUiToIndexes(input: String): Indexes {
        return Indexes(
            boardUi.numbersToIndexes[input[1]] ?: error(INVALID_COMMAND_MESSAGE),
            boardUi.lettersToIndexes[input[0]] ?: error(INVALID_COMMAND_MESSAGE)
        )
    }

    fun indexesToIndexesUi(indexes: Indexes): String {
        val letter = boardUi.indexesToLetters[indexes.j]
        val number = boardUi.indexesToNumbers[indexes.i]
        return "$letter$number"
    }

    fun indexesToIndexesUi(indexes: Set<Indexes>): List<String> {
        return indexes.map(::indexesToIndexesUi).sorted()
    }

    private fun parseMoveCommand(commands: List<String>): Command.Move {
        return Command.Move(
            from = indexesUiToIndexes(commands[0]),
            to = indexesUiToIndexes(commands[1])
        )
    }

    private fun parseGetAvailableMovesCommand(command: String): Command.GetAvailableMoves {
        return Command.GetAvailableMoves(
            from = indexesUiToIndexes(command)
        )
    }
}
