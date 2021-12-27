package core.util

import core.board.Board

typealias Indexes = Pair<Int, Int>

val Indexes.i get() = first
val Indexes.j get() = second
val Indexes.isInBounds get() = i in Board.INDICES && j in Board.INDICES

fun <K, V> Map<K, V>.swapKeysAndValues(toMap: MutableMap<V, K> = mutableMapOf()) = map { (key, value) ->
    value to key
}.toMap(toMap)

fun errorNoFigureAtCell(position: Indexes): Nothing = throw ChessError.NoFigureAtCell(position)
fun errorImpossibleMove(to: Indexes): Nothing = throw ChessError.ImpossibleMove(to)
fun errorInvalidCommand(command: String): Nothing = throw ChessError.InvalidCommand(command)
fun errorOpponentMove(isWhiteMove: Boolean): Nothing = throw ChessError.OpponentMove(isWhiteMove)
