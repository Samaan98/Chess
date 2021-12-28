package core.moves_calculator

import core.board.Board
import core.piece.Piece
import core.util.Indexes

internal class KingMovesCalculatorStrategy : MovesCalculatorStrategy() {

    override fun calculateMoves(
        piece: Piece,
        i: Int,
        j: Int,
        moves: MutableSet<Indexes>,
        board: Board
    ) {
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
    }

    /**
     * Castling is permissible provided all of the following conditions hold:
     * 1. Castling is performed on the kingside or queenside with the rook on the same rank.
     * 2. Neither the king nor the chosen rook has previously moved.
     * 3. There are no pieces between the king and the chosen rook.
     * 4. The king is not currently in check.
     * 5. The king does not pass through a square that is attacked by an enemy piece.
     * 6. The king does not end up in check. (True of any legal move.)
     */
    //todo
    private fun calculateCastlingMoves(
        piece: Piece,
        i: Int,
        j: Int,
        moves: MutableSet<Indexes>,
        board: Board
    ) {
        val isWhite = piece.isWhite
        if (board.isLeftCastlingAvailable(isWhite)) { // conditions #1 & #2
            if (areAllFieldsBetweenKingAndRookEmpty(i, j, board, isLeft = true)) { // condition #3
                moves.add(i to j - 2)
            }
        }
        if (board.isRightCastlingAvailable(isWhite)) { // conditions #1 & #2
            if (areAllFieldsBetweenKingAndRookEmpty(i, j, board, isLeft = false)) { // condition #3
                moves.add(i to j + 2)
            }
        }
        // condition #6 is accomplished when check moves are filtered
    }

    private fun areAllFieldsBetweenKingAndRookEmpty(
        i: Int,
        j: Int,
        board: Board,
        isLeft: Boolean
    ): Boolean {
        val distanceToRook = if (isLeft) 3 else 2
        return (1..distanceToRook).all { nextJ ->
            val position = i to (j + if (isLeft) -nextJ else nextJ)
            board.isCellEmpty(position)
        }
    }
}