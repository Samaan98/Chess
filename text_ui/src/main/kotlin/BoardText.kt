import core.board.Board
import core.board.BoardUi

class BoardText(
    private val board: Board,
    private val boardUi: BoardUi
) {

    companion object {
        private const val HORIZONTAL_DIVIDER = " "
        private const val EMPTY_CELL_SYMBOL = "." // •
    }

    private val lettersLegend = Board.INDICES
        .map { boardUi.indexesToLetters[it] }
        .joinToString(HORIZONTAL_DIVIDER, prefix = "  ")

    fun getBoard(): String {
        return buildString {
            for (i in Board.INDICES) {
                for (j in Board.INDICES) {
                    if (j == 0) {
                        append(boardUi.indexesToNumbers[i])
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
        }
    }
}