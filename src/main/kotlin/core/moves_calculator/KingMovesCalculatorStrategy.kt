package core.moves_calculator

import core.board.Board
import core.piece.Piece
import core.util.Indexes

//todo рокировка
class KingMovesCalculatorStrategy : MovesCalculatorStrategy() {

    override fun calculateMoves(
        piece: Piece,
        i: Int,
        j: Int,
        moves: MutableSet<Indexes>,
        board: Board
    ) {
        val iVariants = listOf(i - 1, i, i + 1)
        val jVariants = listOf(j - 1, j, j + 1)

        for (iVariant in iVariants) {
            for (jVariant in jVariants) {
                if (iVariant == i && jVariant == j) continue
                val move = iVariant to jVariant
                if (piece.canMoveOrCapture(move, board)) {
                    moves.add(move)
                }
            }
        }
    }
}