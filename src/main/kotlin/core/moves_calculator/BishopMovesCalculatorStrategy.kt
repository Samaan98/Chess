package core.moves_calculator

import core.board.Board
import core.piece.Piece
import core.util.Indexes

class BishopMovesCalculatorStrategy : MovesCalculatorStrategy() {

    fun calculateMovesForBishop(
        piece: Piece,
        i: Int,
        j: Int,
        moves: MutableSet<Indexes>,
        board: Board
    ) {
        var canMoveUpLeft = true
        var canMoveDownLeft = true
        var canMoveUpRight = true
        var canMoveDownRight = true
        var nextCellIndex = 1

        while (canMoveUpLeft || canMoveDownLeft || canMoveUpRight || canMoveDownRight) {
            val nextIUp = i - nextCellIndex
            val nextIDown = i + nextCellIndex
            val nextJLeft = j - nextCellIndex
            val nextJRight = j + nextCellIndex

            val nextUpLeft = nextMoveIfCanMoveOrNull(canMoveUpLeft) {
                nextIUp to nextJLeft
            }
            val nextDownLeft = nextMoveIfCanMoveOrNull(canMoveDownLeft) {
                nextIDown to nextJLeft
            }
            val nextUpRight = nextMoveIfCanMoveOrNull(canMoveUpRight) {
                nextIUp to nextJRight
            }
            val nextDownRight = nextMoveIfCanMoveOrNull(canMoveDownRight) {
                nextIDown to nextJRight
            }

            canMoveUpLeft = addMoveIfCanMove(piece, nextUpLeft, moves, board)
            canMoveDownLeft = addMoveIfCanMove(piece, nextDownLeft, moves, board)
            canMoveUpRight = addMoveIfCanMove(piece, nextUpRight, moves, board)
            canMoveDownRight = addMoveIfCanMove(piece, nextDownRight, moves, board)

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
        calculateMovesForBishop(piece, i, j, moves, board)
    }
}