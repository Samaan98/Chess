package core

sealed class ChessError : RuntimeException() {
    data class NoFigureAtCell(val position: Indexes) : ChessError()
    data class ImpossibleMove(val to: Indexes) : ChessError()
    data class InvalidCommand(val command: String) : ChessError()
}

fun errorNoFigureAtCell(position: Indexes): Nothing = throw ChessError.NoFigureAtCell(position)
fun errorImpossibleMove(to: Indexes): Nothing = throw ChessError.ImpossibleMove(to)
fun errorInvalidCommand(command: String): Nothing = throw ChessError.InvalidCommand(command)