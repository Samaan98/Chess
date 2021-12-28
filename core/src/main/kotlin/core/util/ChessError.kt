package core.util

sealed class ChessError : RuntimeException() {
    data class NoFigureAtCell internal constructor(val position: Indexes) : ChessError()
    data class ImpossibleMove internal constructor(val to: Indexes) : ChessError()
    data class InvalidCommand internal constructor(val command: String) : ChessError()
    data class OpponentMove internal constructor(val isWhiteMove: Boolean) : ChessError()
}