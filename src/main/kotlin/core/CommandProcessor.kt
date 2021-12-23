package core

class CommandProcessor(
    private val board: Board,
    private val movesCalculator: MovesCalculator
) {

    fun process(command: Command) {
        when (command) {
            is Command.Move -> processMoveCommand(command)
        }
    }

    private fun processMoveCommand(moveCommand: Command.Move) {
        val availableMoves = movesCalculator.calculateMoves(moveCommand.from)
        if (moveCommand.to in availableMoves) {
            board.move(moveCommand)
        } else error("Невозможный ход")
    }
}