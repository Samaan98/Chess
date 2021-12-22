package core

typealias Indexes = Pair<Int, Int>

val Pair<Int, Int>.i get() = first
val Pair<Int, Int>.j get() = second

fun <K, V> Map<K, V>.swapKeysAndValues(toMap: MutableMap<V, K> = HashMap()) = map { (key, value) ->
    value to key
}.toMap(toMap)