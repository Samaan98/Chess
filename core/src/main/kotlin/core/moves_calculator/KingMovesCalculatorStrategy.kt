package core.moves_calculator

import core.board.Board
import core.piece.Piece
import core.util.Indexes
import core.util.j

internal class KingMovesCalculatorStrategy(private val checkDetector: CheckDetector) : MovesCalculatorStrategy() {

    override fun calculateMoves(piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>, board: Board) {
        val iVariants = listOf(i - 1, i, i + 1)
        val jVariants = listOf(j - 1, j, j + 1)

        for (iVariant in iVariants) {
            for (jVariant in jVariants) {
                if (iVariant == i && jVariant == j) continue
                val move = iVariant to jVariant
                if (piece.canMoveOrCapture(move, board)) {
                    moves.add(move)
                }
            }
        }

        calculateCastlingMoves(piece, i, j, moves, board)
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
    private fun calculateCastlingMoves(piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>, board: Board) {
        val isWhite = piece.isWhite

        val isLeftCastlingAvailable = board.isLeftCastlingAvailable(isWhite) // conditions #1 & #2
        val isRightCastlingAvailable = board.isRightCastlingAvailable(isWhite) // conditions #1 & #2

        val isAnyCastlingAvailable = isLeftCastlingAvailable || isRightCastlingAvailable
        if (!isAnyCastlingAvailable || checkDetector.isCheck(isWhite, board)) return // condition #3

        var isLeft = true
        repeat(2) {
            if (if (isLeft) isLeftCastlingAvailable else isRightCastlingAvailable) {
                if (areAllFieldsBetweenKingAndRookEmpty(i, j, board, isLeft = isLeft)) { // condition #4
                    val nextJ = if (isLeft) -1 else 1
                    val nextMove = i to j + nextJ
                    if (!checkDetector.isCheckAfterMove(isWhite, i to j, nextMove, board)) { // condition #5
                        moves.add(i to nextMove.j + nextJ)
                    }
                }
            }
            isLeft = !isLeft
        }
        // condition #6 is fulfilled when check moves are filtered in MovesCalculator
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
}