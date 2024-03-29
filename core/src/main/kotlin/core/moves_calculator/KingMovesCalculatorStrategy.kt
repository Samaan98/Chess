package core.moves_calculator

import core.board.Board
import core.piece.Piece
import core.piece.PieceType
import core.util.Indexes
import core.util.i
import core.util.isInBounds
import core.util.j

internal class KingMovesCalculatorStrategy(private val checkDetector: CheckDetector) : MovesCalculatorStrategy() {

    override fun calculateMoves(piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>, board: Board) {
        val from = i to j

        doForEveryPositionAroundKing(i, j) {
            val hasEnemyKingAroundAfterMove = hasEnemyKingAroundAfterMove(from, it, board)
            // There is no enemy King around after the move, or there is, but also there is a discovered check.
            val canAttackEnemyKing = !hasEnemyKingAroundAfterMove ||
                    checkDetector.isCheckAfterMove(from, it, board, toggleMove = true)
            if (it.isInBounds && (canCapture(it, board) || (board.isCellEmpty(it) && canAttackEnemyKing))) {
                moves.add(it)
            }
        }

        calculateCastlingMoves(i, j, moves, board)
    }

    /**
     * Castling is permissible provided all of the following conditions hold:
     * 1. Castling is performed on the kingside or queenside with the rook on the same rank.
     * 2. Neither the king nor the chosen rook has previously moved.
     * 3. The king is not currently in check.
     * 4. There are no pieces between the king and the chosen rook.
     * 5. The king does not pass through a square that is attacked by an enemy piece.
     * 6. The king does not end up in check. (True of any legal move.)
     */
    private fun calculateCastlingMoves(i: Int, j: Int, moves: MutableSet<Indexes>, board: Board) {
        val isLeftCastlingAvailable = board.isLeftCastlingAvailable() // conditions #1 & #2
        val isRightCastlingAvailable = board.isRightCastlingAvailable() // conditions #1 & #2

        val isAnyCastlingAvailable = isLeftCastlingAvailable || isRightCastlingAvailable
        if (!isAnyCastlingAvailable || checkDetector.isCheck(board)) return // condition #3

        var isLeft = true
        repeat(2) {
            if (if (isLeft) isLeftCastlingAvailable else isRightCastlingAvailable) {
                if (areAllFieldsBetweenKingAndRookEmpty(i, j, board, isLeft = isLeft)) { // condition #4
                    val nextJ = if (isLeft) -1 else 1
                    val nextMove = i to j + nextJ
                    if (!checkDetector.isCheckAfterMove(i to j, nextMove, board)) { // condition #5
                        moves.add(i to nextMove.j + nextJ)
                    }
                }
            }
            isLeft = !isLeft
        }
        // condition #6 is fulfilled when check moves are filtered in MovesCalculator
    }

    private fun hasEnemyKingAroundAfterMove(from: Indexes, to: Indexes, board: Board): Boolean {
        val copiedBoard = board.copy().apply {
            move(from, to)
        }
        doForEveryPositionAroundKing(to.i, to.j) {
            if (copiedBoard.isEnemy(it) && copiedBoard[it]?.type == PieceType.KING) {
                return true
            }
        }
        return false
    }

    private fun areAllFieldsBetweenKingAndRookEmpty(i: Int, j: Int, board: Board, isLeft: Boolean): Boolean {
        val distanceToRook = if (isLeft) {
            Board.DISTANCE_FROM_KING_TO_LEFT_ROOK
        } else {
            Board.DISTANCE_FROM_KING_TO_RIGHT_ROOK
        }
        return (1..distanceToRook).all { nextJ ->
            val position = i to (j + if (isLeft) -nextJ else nextJ)
            board.isCellEmpty(position)
        }
    }

    private inline fun doForEveryPositionAroundKing(i: Int, j: Int, action: (position: Indexes) -> Unit) {
        val iVariants = listOf(i - 1, i, i + 1)
        val jVariants = listOf(j - 1, j, j + 1)

        for (iVariant in iVariants) {
            for (jVariant in jVariants) {
                if (iVariant == i && jVariant == j) continue
                action(iVariant to jVariant)
            }
        }
    }
}