package ui

import core.command.Command
import core.util.errorInvalidCommand

//todo документация на доступные команды с примерами
internal class InputProcessor(private val boardUi: BoardUi) {

    fun parse(input: String): Command {
        val commands = input.lowercase().split(" ")
        return when (commands.size) {
            2 -> parseMoveCommand(commands)
            1 -> parseGetAvailableMovesCommand(commands.first())
            else -> errorInvalidCommand(input)
        }
    }

    private fun parseMoveCommand(commands: List<String>): Command.Move {
        return Command.Move(
            from = boardUi.indexesUiToIndexes(commands[0]),
            to = boardUi.indexesUiToIndexes(commands[1])
        )
    }

    private fun parseGetAvailableMovesCommand(command: String): Command.GetAvailableMoves {
        return Command.GetAvailableMoves(
            from = boardUi.indexesUiToIndexes(command)
        )
    }
}
