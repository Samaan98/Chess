package core.moves_calculator

import core.board.Board
import core.piece.Piece
import core.util.Indexes

// Queen moves could be interpreted as combination of Rook and Bishop moves
internal class QueenMovesCalculatorStrategy(
    private val rookMovesCalculatorStrategy: RookMovesCalculatorStrategy,
    private val bishopMovesCalculatorStrategy: BishopMovesCalculatorStrategy
) : MovesCalculatorStrategy() {

    override fun calculateMoves(piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>, board: Board) {
        rookMovesCalculatorStrategy.calculateMovesForRook(piece, i, j, moves, board)
        bishopMovesCalculatorStrategy.calculateMovesForBishop(piece, i, j, moves, board)
    }
}