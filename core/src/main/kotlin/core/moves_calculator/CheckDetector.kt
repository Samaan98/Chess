package core.moves_calculator

import core.board.Board
import core.piece.PieceType
import core.util.Indexes
import core.util.i
import core.util.isInBounds
import core.util.j
import java.util.*

internal class CheckDetector(
    private val boardIterator: BoardIterator,
    private val rookMovesCalculatorStrategy: RookMovesCalculatorStrategy,
    private val bishopMovesCalculatorStrategy: BishopMovesCalculatorStrategy,
    private val knightMovesCalculatorStrategy: KnightMovesCalculatorStrategy
) {

    private val horizontalVerticalCheckingEnemies = EnumSet.of(PieceType.ROOK, PieceType.QUEEN)
    private val diagonalCheckingEnemies = EnumSet.of(PieceType.BISHOP, PieceType.QUEEN)

    fun isCheck(board: Board): Boolean {
        val kingPosition = board.getKingPosition()
        val i = kingPosition.i
        val j = kingPosition.j

        // pawns and knights
        if (hasCheckingPawns(i, j, board) || hasCheckingKnights(i, j, board)) return true

        val rookMovesIds = rookMovesCalculatorStrategy.movesIds
        val bishopMovesIds = bishopMovesCalculatorStrategy.movesIds
        // rooks, bishops, queen
        // * there's no need to check king as king cannot check another king
        boardIterator.iterate(
            movesIds = rookMovesIds + bishopMovesIds,
            moveProducer = { moveId, nextCellIndex ->
                if (moveId in rookMovesIds) {
                    rookMovesCalculatorStrategy.produceMove(moveId, nextCellIndex, i, j)
                } else {
                    bishopMovesCalculatorStrategy.produceMove(moveId, nextCellIndex, i, j)
                }
            },
            iterationAction = { moveId, move ->
                doIfHasCheckingEnemyAndCanMoveFurther(
                    nextPosition = move,
                    board = board,
                    diagonal = moveId in bishopMovesIds
                ) {
                    return true
                }
            }
        )

        return false
    }

    fun isCheckAfterMove(from: Indexes, to: Indexes, board: Board, toggleMove: Boolean = false): Boolean {
        return board.copy().apply {
            move(from, to)
            if (toggleMove) toggleMove()
        }.let { modifiedCopiedBoard ->
            isCheck(board = modifiedCopiedBoard)
        }
    }

    private fun hasCheckingPawns(i: Int, j: Int, board: Board): Boolean {
        val isWhite = board.isWhiteMove
        val checkingPawnI = i + if (isWhite) -1 else 1
        val leftCheckingPawnPosition = checkingPawnI to (j - 1)
        val rightCheckingPawnPosition = checkingPawnI to (j + 1)
        return arrayOf(leftCheckingPawnPosition, rightCheckingPawnPosition).any { position ->
            board[position]?.let { piece ->
                piece.isWhite != isWhite && piece.type == PieceType.PAWN
            } ?: false
        }
    }

    private fun hasCheckingKnights(i: Int, j: Int, board: Board): Boolean {
        return knightMovesCalculatorStrategy.getKnightMoves(i, j).any { position ->
            board[position]?.let { piece ->
                piece.isWhite != board.isWhiteMove && piece.type == PieceType.KNIGHT
            } ?: false
        }
    }

    private inline fun doIfHasCheckingEnemyAndCanMoveFurther(
        nextPosition: Indexes?,
        board: Board,
        diagonal: Boolean,
        checkingEnemyAction: () -> Unit
    ): Boolean {
        if (nextPosition == null || !nextPosition.isInBounds) return false
        val pieceAtNextPosition = board[nextPosition]
        return if (pieceAtNextPosition != null) {
            val isEnemy = pieceAtNextPosition.isWhite != board.isWhiteMove
            val checkingEnemies = if (diagonal) diagonalCheckingEnemies else horizontalVerticalCheckingEnemies
            if (isEnemy && pieceAtNextPosition.type in checkingEnemies) {
                checkingEnemyAction()
            }
            false
        } else true
    }
}