package core.moves_calculator

import core.board.Board
import core.piece.Piece
import core.util.Indexes

internal class RookMovesCalculatorStrategy : MovesCalculatorStrategy() {

    fun calculateMovesForRook(
        piece: Piece,
        i: Int,
        j: Int,
        moves: MutableSet<Indexes>,
        board: Board
    ) {
        var canMoveLeft = true
        var canMoveUp = true
        var canMoveRight = true
        var canMoveDown = true
        var nextCellIndex = 1

        while (canMoveLeft || canMoveUp || canMoveRight || canMoveDown) {
            val nextLeft = nextMoveIfCanMoveOrNull(canMoveLeft) {
                i to (j - nextCellIndex)
            }
            val nextUp = nextMoveIfCanMoveOrNull(canMoveUp) {
                (i - nextCellIndex) to j
            }
            val nextRight = nextMoveIfCanMoveOrNull(canMoveRight) {
                i to (j + nextCellIndex)
            }
            val nextDown = nextMoveIfCanMoveOrNull(canMoveDown) {
                (i + nextCellIndex) to j
            }

            canMoveLeft = addMoveIfCanMove(piece, nextLeft, moves, board)
            canMoveUp = addMoveIfCanMove(piece, nextUp, moves, board)
            canMoveRight = addMoveIfCanMove(piece, nextRight, moves, board)
            canMoveDown = addMoveIfCanMove(piece, nextDown, moves, board)

            nextCellIndex++
        }
    }

    override fun calculateMoves(
        piece: Piece,
        i: Int,
        j: Int,
        moves: MutableSet<Indexes>,
        board: Board
    ) {
        calculateMovesForRook(piece, i, j, moves, board)
    }
}