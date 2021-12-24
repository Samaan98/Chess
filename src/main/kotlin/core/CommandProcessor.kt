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
        from.ensureMyTurn(isWhiteMove)

        val availableMoves = movesCalculator.calculateMoves(from)
        if (moveCommand.to in availableMoves) {
            board.move(moveCommand)
        } else errorImpossibleMove(moveCommand.to)

        return CommandResult.Success
    }

    private fun processGetAvailableMovesCommand(
        getAvailableMovesCommand: Command.GetAvailableMoves,
        isWhiteMove: Boolean
    ): CommandResult {
        val from = getAvailableMovesCommand.from
        from.ensureMyTurn(isWhiteMove)

        return CommandResult.AvailableMoves(
            data = movesCalculator.calculateMoves(from)
        )
    }

    private fun Indexes.ensureMyTurn(isWhiteMove: Boolean) {
        val piece = board[this] ?: errorNoFigureAtCell(this)
        require(piece.isWhite == isWhiteMove) {
            "Сейчас ход ${if (isWhiteMove) "белых" else "черных"}"
        }
    }
}