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

    /**
     * Detects if there's a check for [forWhite].
     */
    fun isCheck(forWhite: Boolean, board: Board): Boolean {
        val kingPosition = board.getKingPosition(forWhite)
        val i = kingPosition.i
        val j = kingPosition.j

        // pawns and knights
        if (hasCheckingPawns(forWhite, i, j, board) || hasCheckingKnights(forWhite, i, j, board)) return true

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
                    forWhite,
                    move,
                    board,
                    diagonal = moveId in bishopMovesIds
                ) {
                    return true
                }
            }
        )

        return false
    }

    fun isCheckAfterMove(forWhite: Boolean, from: Indexes, to: Indexes, board: Board): Boolean {
        return board.copy().apply { // copy the board
            move(from, to) // move the piece on the copied board
        }.let { modifiedCopiedBoard ->
            isCheck(forWhite = forWhite, board = modifiedCopiedBoard) // check if this move leads to check
        }
    }

    private fun hasCheckingPawns(isWhite: Boolean, i: Int, j: Int, board: Board): Boolean {
        val checkingPawnI = i + if (isWhite) -1 else 1
        val leftCheckingPawnPosition = checkingPawnI to (j - 1)
        val rightCheckingPawnPosition = checkingPawnI to (j + 1)
        return arrayOf(leftCheckingPawnPosition, rightCheckingPawnPosition).any { position ->
            board[position]?.let { piece ->
                piece.isWhite != isWhite && piece.type == PieceType.PAWN
            } ?: false
        }
    }

    private fun hasCheckingKnights(isWhite: Boolean, i: Int, j: Int, board: Board): Boolean {
        return knightMovesCalculatorStrategy.getKnightMoves(i, j).any { position ->
            board[position]?.let { piece ->
                piece.isWhite != isWhite && piece.type == PieceType.KNIGHT
            } ?: false
        }
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