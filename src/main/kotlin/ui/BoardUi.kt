package ui

import core.board.Board
import core.util.*

class BoardUi internal constructor() {

    val indexesToLetters = initIntToCharBoardSizeMap { 'a' + it }

    val lettersToIndexes = indexesToLetters.swapKeysAndValues()

    val indexesToNumbers = initIntToCharBoardSizeMap { (Board.SIZE - it).toString().first() }

    val numbersToIndexes: Map<Char, Int> = indexesToNumbers.swapKeysAndValues()

    fun indexesUiToIndexes(input: String): Indexes {
        return Indexes(
            numbersToIndexes[input[1]] ?: errorInvalidCommand(input),
            lettersToIndexes[input[0]] ?: errorInvalidCommand(input)
        )
    }

    fun indexesToIndexesUi(indexes: Indexes): String {
        val letter = indexesToLetters[indexes.j]
        val number = indexesToNumbers[indexes.i]
        return "$letter$number"
    }

    fun indexesToIndexesUi(indexes: Set<Indexes>): List<String> {
        return indexes.map(::indexesToIndexesUi).sorted()
    }

    private fun initIntToCharBoardSizeMap(initializer: (i: Int) -> Char): Map<Int, Char> {
        return HashMap<Int, Char>(Board.SIZE).apply {
            for (i in Board.INDICES) {
                put(i, initializer(i))
            }
        }
    }
}