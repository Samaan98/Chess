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
        piece: Piece,
        nextPosition: Indexes?,
        moves: MutableSet<Indexes>,
        board: Board
    ): Boolean {
        if (nextPosition == null) return false
        return if (piece.canMoveOrCapture(nextPosition, board)) {
            moves.add(nextPosition)
            !board.isEnemy(nextPosition, piece.isWhite)
        } else false
    }

    protected inline fun nextMoveIfCanMoveOrNull(canMove: Boolean, nextMove: () -> Indexes): Indexes? {
        return if (canMove) nextMove() else null
    }

    protected fun Piece.canMoveOrCapture(position: Indexes, board: Board): Boolean {
        return position.isInBounds && (board.isCellEmpty(position) || board.isEnemy(position, isWhite))
    }
}