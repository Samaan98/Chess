package core.moves_calculator

import core.board.Board
import core.piece.Piece
import core.util.Indexes

internal class RookMovesCalculatorStrategy(
    private val boardIterator: BoardIterator
) : MovesCalculatorStrategy() {

    private val left = "left"
    private val up = "up"
    private val right = "right"
    private val down = "down"

    val movesIds = setOf(left, up, right, down)

    fun produceMove(moveId: String, nextCellIndex: Int, i: Int, j: Int): Indexes {
        val nextIUp = i - nextCellIndex
        val nextIDown = i + nextCellIndex
        val nextJLeft = j - nextCellIndex
        val nextJRight = j + nextCellIndex

        return when (moveId) {
            left -> i to nextJLeft
            up -> nextIUp to j
            right -> i to nextJRight
            down -> nextIDown to j
            else -> error("Unknown moveId: $moveId")
        }
    }

    override fun calculateMoves(piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>, board: Board) {
        boardIterator.iterate(
            movesIds = movesIds,
            moveProducer = { moveId, nextCellIndex ->
                produceMove(moveId, nextCellIndex, i, j)
            },
            iterationAction = { _, move ->
                addMoveIfCanMoveAndCanMoveFurther(piece, move, moves, board)
            }
        )
    }
}