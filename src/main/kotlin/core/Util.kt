package core

typealias Indexes = Pair<Int, Int>

fun <K, V> Map<K, V>.swapKeysAndValues(toMap: MutableMap<V, K> = HashMap()) = map { (key, value) ->
    value to key
}.toMap(toMap)