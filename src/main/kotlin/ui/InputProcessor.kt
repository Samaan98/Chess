package ui

import core.command.Command
import core.util.Indexes
import core.util.errorInvalidCommand
import core.util.i
import core.util.j

//todo документация на доступные команды с примерами
class InputProcessor(private val boardUi: BoardUi) {

    fun parse(input: String): Command {
        val commands = input.lowercase().split(" ")
        return when (commands.size) {
            2 -> parseMoveCommand(commands)
            1 -> parseGetAvailableMovesCommand(commands.first())
            else -> errorInvalidCommand(input)
        }
    }

    fun indexesUiToIndexes(input: String): Indexes {
        return Indexes(
            boardUi.numbersToIndexes[input[1]] ?: errorInvalidCommand(input),
            boardUi.lettersToIndexes[input[0]] ?: errorInvalidCommand(input)
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
