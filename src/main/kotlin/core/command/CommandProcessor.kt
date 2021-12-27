package core.command

import core.MovesCalculator
import core.board.Board
import core.util.Indexes
import core.util.errorImpossibleMove
import core.util.errorNoFigureAtCell
import core.util.errorOpponentMove

class CommandProcessor(
    private val board: Board,
    private val movesCalculator: MovesCalculator
) {

    fun process(command: Command, isWhiteMove: Boolean): CommandResult {
        return when (command) {
            is Command.Move -> processMoveCommand(command, isWhiteMove)
            is Command.GetAvailableMoves -> processGetAvailableMovesCommand(command, isWhiteMove)
        }
    }

    private fun processMoveCommand(moveCommand: Command.Move, isWhiteMove: Boolean): CommandResult {
        val from = moveCommand.from
        from.ensureNotOpponentMove(isWhiteMove)

        val availableMoves = movesCalculator.calculateMovesWithCheckMovesFiltered(from, isWhiteMove, board)

        if (moveCommand.to !in availableMoves) errorImpossibleMove(moveCommand.to)

        board.move(moveCommand.from, moveCommand.to)

        val enemy = !isWhiteMove
        val isCheckForEnemy = movesCalculator.isCheck(forWhite = enemy, board)

        return if (isCheckForEnemy) {
            val isCheckmateForEnemy = movesCalculator.isCheckmate(forWhite = enemy, board)
            if (isCheckmateForEnemy) {
                CommandResult.Checkmate
            } else {
                CommandResult.Check
            }
        } else {
            CommandResult.Success
        }
    }

    private fun processGetAvailableMovesCommand(
        getAvailableMovesCommand: Command.GetAvailableMoves,
        isWhiteMove: Boolean
    ): CommandResult {
        val from = getAvailableMovesCommand.from
        from.ensureNotOpponentMove(isWhiteMove)

        return CommandResult.AvailableMoves(
            data = movesCalculator.calculateMovesWithCheckMovesFiltered(from, isWhiteMove, board)
        )
    }

    private fun Indexes.ensureNotOpponentMove(isWhiteMove: Boolean) {
        val piece = board[this] ?: errorNoFigureAtCell(this)
        if (piece.isWhite != isWhiteMove) errorOpponentMove(isWhiteMove)
    }
}