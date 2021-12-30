package core.command

import core.board.Board
import core.moves_calculator.MovesCalculator
import core.util.Indexes
import core.util.errorImpossibleMove
import core.util.errorNoFigureAtCell
import core.util.errorOpponentMove

internal class CommandProcessor(
    private val board: Board,
    private val movesCalculator: MovesCalculator
) {

    // todo remove isWhiteMove from everywhere?
    private val isWhiteMove by board::isWhiteMove

    fun process(command: Command): CommandResult {
        return when (command) {
            is Command.Move -> processMoveCommand(command)
            is Command.GetAvailableMoves -> processGetAvailableMovesCommand(command)
        }
    }

    private fun processMoveCommand(moveCommand: Command.Move): CommandResult {
        val from = moveCommand.from
        from.ensureNotOpponentMove()

        val availableMoves = movesCalculator.calculateMovesWithCheckMovesFiltered(from, isWhiteMove, board)

        if (moveCommand.to !in availableMoves) errorImpossibleMove(moveCommand.to)

        board.move(moveCommand.from, moveCommand.to)

        val enemy = !isWhiteMove
        val isCheckForEnemy = movesCalculator.isCheck(forWhite = enemy, board)

        val result = if (isCheckForEnemy) {
            val isCheckmateForEnemy = movesCalculator.isCheckmate(forWhite = enemy, board)
            if (isCheckmateForEnemy) {
                CommandResult.Checkmate
            } else {
                CommandResult.Check
            }
        } else {
            CommandResult.Success
        }

        board.toggleMove()

        return result
    }

    private fun processGetAvailableMovesCommand(getAvailableMovesCommand: Command.GetAvailableMoves): CommandResult {
        val from = getAvailableMovesCommand.from
        from.ensureNotOpponentMove()

        return CommandResult.AvailableMoves(
            data = movesCalculator.calculateMovesWithCheckMovesFiltered(from, isWhiteMove, board)
        )
    }

    private fun Indexes.ensureNotOpponentMove() {
        val piece = board[this] ?: errorNoFigureAtCell(this)
        if (piece.isWhite != isWhiteMove) errorOpponentMove(isWhiteMove)
    }
}