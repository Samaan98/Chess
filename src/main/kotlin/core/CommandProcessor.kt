package core

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

        val availableMoves = movesCalculator.calculateMovesWithCheckMovesFiltered(from, isWhiteMove)

        if (moveCommand.to !in availableMoves) errorImpossibleMove(moveCommand.to)

        board.move(moveCommand.from, moveCommand.to)
        val isCheckForEnemy = movesCalculator.isCheck(forWhite = !isWhiteMove)

        return if (isCheckForEnemy) {
            CommandResult.Check
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
            data = movesCalculator.calculateMovesWithCheckMovesFiltered(from, isWhiteMove)
        )
    }

    private fun Indexes.ensureNotOpponentMove(isWhiteMove: Boolean) {
        val piece = board[this] ?: errorNoFigureAtCell(this)
        if (piece.isWhite != isWhiteMove) errorOpponentMove(isWhiteMove)
    }
}