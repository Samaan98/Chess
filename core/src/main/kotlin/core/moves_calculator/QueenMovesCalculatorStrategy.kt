package core.moves_calculator

import core.board.Board
import core.piece.Piece
import core.util.Indexes

internal class QueenMovesCalculatorStrategy(
    private val boardIterator: BoardIterator,
    private val rookMovesCalculatorStrategy: RookMovesCalculatorStrategy,
    private val bishopMovesCalculatorStrategy: BishopMovesCalculatorStrategy
) : MovesCalculatorStrategy() {

    // Queen moves could be interpreted as combination of Rook and Bishop moves
    override fun calculateMoves(piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>, board: Board) {
        val rookMovesIds = rookMovesCalculatorStrategy.movesIds
        val bishopMovesIds = bishopMovesCalculatorStrategy.movesIds

        boardIterator.iterate(
            movesIds = rookMovesIds + bishopMovesIds,
            moveProducer = { moveId, nextCellIndex ->
                if (moveId in rookMovesIds) {
                    rookMovesCalculatorStrategy.produceMove(moveId, nextCellIndex, i, j)
                } else {
                    bishopMovesCalculatorStrategy.produceMove(moveId, nextCellIndex, i, j)
                }
            },
            iterationAction = { _, move ->
                addMoveIfCanMoveAndCanMoveFurther(move, moves, board)
            }
        )
    }
}