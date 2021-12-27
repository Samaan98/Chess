package core.moves_calculator

import core.board.Board
import core.piece.Piece
import core.util.Indexes
import core.util.isInBounds

class KnightMovesCalculatorStrategy : MovesCalculatorStrategy() {

    override fun calculateMoves(
        piece: Piece,
        i: Int,
        j: Int,
        moves: MutableSet<Indexes>,
        board: Board
    ) {
        val iOneUp = i - 1
        val iOneDown = i + 1
        val iTwoUp = i - 2
        val iTwoDown = i + 2
        val jOneLeft = j - 1
        val jOneRight = j + 1
        val jTwoLeft = j - 2
        val jTwoRight = j + 2

        val oneUpTwoLeft = iOneUp to jTwoLeft
        val twoUpOneLeft = iTwoUp to jOneLeft
        val twoUpOneRight = iTwoUp to jOneRight
        val oneUpTwoRight = iOneUp to jTwoRight
        val oneDownTwoRight = iOneDown to jTwoRight
        val twoDownOneRight = iTwoDown to jOneRight
        val twoDownOneLeft = iTwoDown to jOneLeft
        val oneDownTwoLeft = iOneDown to jTwoLeft

        arrayOf(
            oneUpTwoLeft,
            twoUpOneLeft,
            twoUpOneRight,
            oneUpTwoRight,
            oneDownTwoRight,
            twoDownOneRight,
            twoDownOneLeft,
            oneDownTwoLeft
        ).forEach {
            if (it.isInBounds && (board.isCellEmpty(it) || board.isEnemy(it, piece.isWhite))) {
                moves.add(it)
            }
        }
    }
}