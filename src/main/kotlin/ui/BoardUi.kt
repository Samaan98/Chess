package ui

import core.Board
import core.swapKeysAndValues

class BoardUi {

    val indexesToLetters = initIntToCharBoardSizeMap { 'a' + it }

    val lettersToIndexes = indexesToLetters.swapKeysAndValues()

    val indexesToNumbers = initIntToCharBoardSizeMap { (Board.SIZE - it).toString().first() }

    val numbersToIndexes: Map<Char, Int> = indexesToNumbers.swapKeysAndValues()

    private fun initIntToCharBoardSizeMap(initializer: (i: Int) -> Char): Map<Int, Char> {
        return HashMap<Int, Char>(Board.SIZE).apply {
            for (i in Board.INDICES) {
                put(i, initializer(i))
            }
        }
    }
}