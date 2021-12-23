package core

class CommandProcessor(
    private val board: Board,
    private val movesCalculator: MovesCalculator
) {

    fun process(command: Command, isWhiteMove: Boolean) {
        when (command) {
            is Command.Move -> processMoveCommand(command, isWhiteMove)
        }
    }

    private fun processMoveCommand(moveCommand: Command.Move, isWhiteMove: Boolean) {
        val piece = board[moveCommand.from] ?: error("Нет фигуры на данной клетке")
        piece.ensureMyTurn(isWhiteMove)

        val availableMoves = movesCalculator.calculateMoves(moveCommand.from)
        if (moveCommand.to in availableMoves) {
            board.move(moveCommand)
        } else error("Невозможный ход")
    }

    private fun Piece.ensureMyTurn(isWhiteMove: Boolean) {
        require(isWhite == isWhiteMove) {
            "Сейчас ход ${if (isWhiteMove) "белых" else "черных"}"
        }
    }
}