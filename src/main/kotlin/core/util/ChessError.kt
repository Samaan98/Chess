package core.util

sealed class ChessError : RuntimeException() {
    data class NoFigureAtCell(val position: Indexes) : ChessError()
    data class ImpossibleMove(val to: Indexes) : ChessError()
    data class InvalidCommand(val command: String) : ChessError()
    data class OpponentMove(val isWhiteMove: Boolean) : ChessError()
}