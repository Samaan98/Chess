package ui

import core.Board
import core.swapKeysAndValues

class BoardUi(private val board: Board) {

    companion object {
        private const val HORIZONTAL_DIVIDER = " "
        private const val EMPTY_CELL_SYMBOL = "."
    }

    val indexesToLetters: Map<Int, Char> = HashMap<Int, Char>(Board.SIZE).apply {
        for (i in Board.INDICES) {
            put(i, 'a' + i)
        }
    }

    val lettersToIndexes: Map<Char, Int> = indexesToLetters.swapKeysAndValues()

    private val lettersLegend = Board.INDICES
        .map { indexesToLetters[it] }
        .joinToString(HORIZONTAL_DIVIDER, prefix = "  ")

    val indexesToIndexesUi: Map<Int, Char> = HashMap<Int, Char>(Board.SIZE).apply {
        for (i in Board.INDICES) {
            put(i, (Board.SIZE - i).toString().first())
        }
    }

    val indexesUiToIndexes: Map<Char, Int> = indexesToIndexesUi.swapKeysAndValues()

    fun printBoard() {
        buildString {
            for (i in Board.INDICES) {
                for (j in Board.INDICES) {
                    if (j == 0) {
                        append(indexesToIndexesUi[i])
                        append(HORIZONTAL_DIVIDER)
                    }
                    append(board[i to j]?.symbol ?: EMPTY_CELL_SYMBOL)
                    if (j != Board.LAST_INDEX) append(HORIZONTAL_DIVIDER)
                }
                append("\n")
                if (i == Board.LAST_INDEX) {
                    append(lettersLegend)
                }
            }
        }.also {
            println(it)
        }
    }
}