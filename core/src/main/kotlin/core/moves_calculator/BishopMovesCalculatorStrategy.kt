package core.moves_calculator

import core.board.Board
import core.piece.Piece
import core.util.Indexes

internal class BishopMovesCalculatorStrategy(
    private val boardIterator: BoardIterator
) : MovesCalculatorStrategy() {

    private val upLeft = "upLeft"
    private val upRight = "upRight"
    private val downRight = "downRight"
    private val downLeft = "downLeft"

    val movesIds = setOf(upLeft, upRight, downRight, downLeft)

    fun produceMove(moveId: String, nextCellIndex: Int, i: Int, j: Int): Indexes {
        val nextIUp = i - nextCellIndex
        val nextIDown = i + nextCellIndex
        val nextJLeft = j - nextCellIndex
        val nextJRight = j + nextCellIndex

        return when (moveId) {
            upLeft -> nextIUp to nextJLeft
            upRight -> nextIUp to nextJRight
            downRight -> nextIDown to nextJRight
            downLeft -> nextIDown to nextJLeft
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
                addMoveIfCanMoveAndCanMoveFurther(move, moves, board)
            }
        )
    }
}