package core.moves_calculator

import core.board.Board
import core.piece.Piece
import core.piece.PieceType
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
        position: Indexes,
        moves: MutableSet<Indexes>,
        board: Board
    ): Boolean {
        return if (position.isInBounds && (board.isCellEmpty(position) || canCapture(position, board))) {
            moves.add(position)
            !board.isEnemy(position)
        } else false
    }

    protected fun canCapture(position: Indexes, board: Board): Boolean {
        return board.isEnemy(position) && board[position]?.type != PieceType.KING
    }
}