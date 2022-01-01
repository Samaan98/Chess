package core.moves_calculator

import core.board.Board
import core.piece.Piece
import core.util.Indexes
import core.util.i
import core.util.isInBounds
import core.util.j

internal abstract class MovesCalculatorStrategy {

    fun calculateMoves(position: Indexes, piece: Piece, board: Board): Set<Indexes> {
        val moves = mutableSetOf<Indexes>()
        val i = position.i
        val j = position.j

        calculateMoves(piece, i, j, moves, board)

        return moves
    }

    protected abstract fun calculateMoves(
        piece: Piece,
        i: Int,
        j: Int,
        moves: MutableSet<Indexes>,
        board: Board
    )

    /**
     * @return can move further after this move.
     */
    protected fun addMoveIfCanMoveAndCanMoveFurther(
        nextPosition: Indexes,
        moves: MutableSet<Indexes>,
        board: Board
    ): Boolean {
        return if (canMoveOrCapture(nextPosition, board)) {
            moves.add(nextPosition)
            !board.isEnemy(nextPosition)
        } else false
    }

    protected fun canMoveOrCapture(position: Indexes, board: Board): Boolean {
        return position.isInBounds && (board.isCellEmpty(position) || board.isEnemy(position))
    }
}