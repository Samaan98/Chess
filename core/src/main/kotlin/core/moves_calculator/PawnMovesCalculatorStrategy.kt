package core.moves_calculator

import core.board.Board
import core.piece.Piece
import core.util.Indexes
import core.util.isInBounds
import core.util.j
import kotlin.math.abs

//todo достижение края доски и замена фигуры
internal class PawnMovesCalculatorStrategy : MovesCalculatorStrategy() {

    override fun calculateMoves(piece: Piece, i: Int, j: Int, moves: MutableSet<Indexes>, board: Board) {
        val nextIBlackOrWhite = piece.blackOrWhite(1, -1)
        val nextI = i + nextIBlackOrWhite
        val next = nextI to j
        if (board.isCellEmpty(next)) {
            moves.add(next)
            val afterNext = nextI + nextIBlackOrWhite to j
            val isInInitialPosition = i == piece.blackOrWhite(
                Board.PAWNS_BLACK_INITIAL_ROW_INDEX,
                Board.PAWNS_WHITE_INITIAL_ROW_INDEX
            )
            if (isInInitialPosition && board.isCellEmpty(afterNext)) {
                moves.add(afterNext)
            }
        }

        val nextCaptureLeft = nextI to j - 1
        val nextCaptureRight = nextI to j + 1
        arrayOf(nextCaptureLeft, nextCaptureRight).forEach {
            if (it.isInBounds && board.isEnemy(it)) {
                moves.add(it)
            }
        }

        calculateEnPassantMoves(piece, i, j, nextCaptureLeft, nextCaptureRight, moves, board)
    }

    /**
     * En passant conditions:
     * 1. The capturing pawn must be on its fifth rank.
     * 2. The captured pawn must be on an adjacent file.
     * 3. The captured pawn must have just moved two squares in a single move (i.e. a double-step move).
     * 4. The capture can only be made on the move immediately after the enemy pawn makes the double-step move;
     * otherwise, the right to capture it en passant is lost.
     */
    private fun calculateEnPassantMoves(
        piece: Piece,
        i: Int,
        j: Int,
        nextCaptureLeft: Indexes,
        nextCaptureRight: Indexes,
        moves: MutableSet<Indexes>,
        board: Board
    ) {
        val canBeCapturedEnPassant = board.canBeCapturedEnPassant ?: return // condition #3
        val isInCapturingRank = i == piece.blackOrWhite(
            Board.EN_PASSANT_PAWN_BLACK_ROW_INDEX,
            Board.EN_PASSANT_PAWN_WHITE_ROW_INDEX
        ) // condition #1
        val hasPawnToCapture = abs(canBeCapturedEnPassant.j - j) == 1 // condition #2
        if (isInCapturingRank && hasPawnToCapture) {
            val isLeft = canBeCapturedEnPassant.j < j
            moves.add(if (isLeft) nextCaptureLeft else nextCaptureRight)
        }
        // condition #4 is fulfilled when canBeCapturedEnPassant is cleared before every move
    }
}