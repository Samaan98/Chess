package core.moves_calculator

import core.board.Board
import core.piece.Piece
import core.util.Indexes
import core.util.isInBounds

//todo взятие на проходе
//todo достижение края доски и замена фигуры
internal class PawnMovesCalculatorStrategy : MovesCalculatorStrategy() {

    override fun calculateMoves(
        piece: Piece,
        i: Int,
        j: Int,
        moves: MutableSet<Indexes>,
        board: Board
    ) {
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
            if (it.isInBounds && board.isEnemy(it, piece.isWhite)) {
                moves.add(it)
            }
        }
    }
}