package core.moves_calculator

import core.board.Board
import core.piece.PieceType
import core.util.Indexes
import core.util.i
import core.util.isInBounds
import core.util.j
import java.util.*

internal class CheckDetector(private val boardIterator: BoardIterator) {

    private val left = "left"
    private val up = "up"
    private val right = "right"
    private val down = "down"
    private val upLeft = "upLeft"
    private val upRight = "upRight"
    private val downRight = "downRight"
    private val downLeft = "downLeft"

    private val horizontalVerticalMovesIds = setOf(left, up, right, down)
    private val diagonalMovesIds = setOf(upLeft, upRight, downRight, downLeft)

    private val horizontalVerticalCheckingEnemies = EnumSet.of(PieceType.ROOK, PieceType.QUEEN)
    private val diagonalCheckingEnemies = EnumSet.of(PieceType.BISHOP, PieceType.QUEEN)

    fun isCheck(forWhite: Boolean, board: Board): Boolean {
        val kingPosition = board.getKingPosition(forWhite)
        val i = kingPosition.i
        val j = kingPosition.j

        boardIterator.iterate(
            horizontalVerticalMovesIds + diagonalMovesIds,
            moveProducer = { moveId, nextCellIndex ->
                val nextIUp = i - nextCellIndex
                val nextIDown = i + nextCellIndex
                val nextJLeft = j - nextCellIndex
                val nextJRight = j + nextCellIndex

                when (moveId) {
                    left -> i to nextJLeft
                    up -> nextIUp to j
                    right -> i to nextJRight
                    down -> nextIDown to j
                    upLeft -> nextIUp to nextJLeft
                    upRight -> nextIUp to nextJRight
                    downRight -> nextIDown to nextJRight
                    downLeft -> nextIDown to nextJLeft
                    else -> error("Unknown moveId: $moveId")
                }
            },
            iterationAction = { moveId, move ->
                doIfHasCheckingEnemyAndCanMoveFurther(
                    forWhite,
                    move,
                    board,
                    diagonal = moveId in diagonalMovesIds
                ) {
                    return true
                }
            }
        )

        return false
    }

    private inline fun doIfHasCheckingEnemyAndCanMoveFurther(
        isWhite: Boolean,
        nextPosition: Indexes?,
        board: Board,
        diagonal: Boolean,
        checkingEnemyAction: () -> Unit
    ): Boolean {
        if (nextPosition == null || !nextPosition.isInBounds) return false
        val pieceAtNextPosition = board[nextPosition]
        return if (pieceAtNextPosition != null) {
            val isEnemy = pieceAtNextPosition.isWhite != isWhite
            val checkingEnemies = if (diagonal) diagonalCheckingEnemies else horizontalVerticalCheckingEnemies
            if (isEnemy && pieceAtNextPosition.type in checkingEnemies) {
                checkingEnemyAction()
            }
            false
        } else true
    }
}