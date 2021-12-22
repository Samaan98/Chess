package ui

import core.Board

class BoardUi(private val board: Board) {

    companion object {
        private const val HORIZONTAL_DIVIDER = " "
    }

    private val letters = Board.INDICES.map {
        board.indexesToLetters[it]
    }.joinToString(HORIZONTAL_DIVIDER, prefix = "  ")

    val lettersToIndexes: Map<Char, Int> = HashMap<Char, Int>(Board.SIZE).apply {
        for (i in Board.INDICES) {
            put('a' + i, i)
        }
    }

    fun printBoard() {
        buildString {
            for (i in Board.INDICES) {
                for (j in Board.INDICES) {
                    if (j == 0) {
                        append(Board.SIZE - i)
                        append(HORIZONTAL_DIVIDER)
                    }
                    append(board.board[i to j]?.symbol ?: '.')
                    if (j != Board.LAST_INDEX) append(HORIZONTAL_DIVIDER)
                }
                append('\n')
                if (i == Board.LAST_INDEX) {
                    append(letters)
                }
            }
        }.also {
            println(it)
        }
    }
}