package core.command

import core.board.BoardUi
import core.util.errorInvalidCommand

/**
 * List of available commands:
 * 1. Move/Capture command.
 * Should consist of two parts delimited by whitespace: field FROM and if field TO.
 *
 * Examples:
 * * e2 e4
 * * b1 c3
 *
 * To perform castling, use the same command for the King. The Rook will be moved automatically.
 *
 * Examples of castling:
 * * e1 c1
 * * e1 g1
 * * e8 c8
 * * e8 g8
 *
 * 2. Get Available Moves Command.
 * Should consist only of one part: field FROM. Available moves will be calculated for the piece at this field.
 *
 * Examples:
 * * e2
 * * b1
 */
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
