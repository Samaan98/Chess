package core

typealias Indexes = Pair<Int, Int>

val Indexes.i get() = first
val Indexes.j get() = second
val Indexes.isInBounds get() = i in Board.INDICES && j in Board.INDICES

fun <K, V> Map<K, V>.swapKeysAndValues(toMap: MutableMap<V, K> = HashMap()) = map { (key, value) ->
    value to key
}.toMap(toMap)