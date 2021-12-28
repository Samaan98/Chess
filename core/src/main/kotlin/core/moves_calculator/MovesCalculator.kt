package core.moves_calculator

import core.board.Board
import core.piece.PieceType
import core.util.Indexes
import core.util.errorNoFigureAtCell
import java.util.*

internal class MovesCalculator {

    private val movesCalculatorStrategy = EnumMap<PieceType, MovesCalculatorStrategy>(PieceType::class.java).apply {
        val rookMovesCalculatorStrategy = RookMovesCalculatorStrategy()
        val bishopMovesCalculatorStrategy = BishopMovesCalculatorStrategy()
        PieceType.values().forEach {
            val strategy = when (it) {
                PieceType.PAWN -> PawnMovesCalculatorStrategy()
                PieceType.ROOK -> rookMovesCalculatorStrategy
                PieceType.KNIGHT -> KnightMovesCalculatorStrategy()
                PieceType.BISHOP -> bishopMovesCalculatorStrategy
                PieceType.QUEEN -> QueenMovesCalculatorStrategy(
                    rookMovesCalculatorStrategy,
                    bishopMovesCalculatorStrategy
                )
                PieceType.KING -> KingMovesCalculatorStrategy()
            }
            put(it, strategy)
        }
    }

    /**
     * Calculates available moves for a piece at [position].
     * Such moves that lead to check are filtered.
     * @return a set of available moves.
     */
    fun calculateMovesWithCheckMovesFiltered(
        position: Indexes,
        forWhite: Boolean,
        board: Board
    ): Set<Indexes> {
        return calculateMoves(position, board).filter { potentialMoveTo ->
            board.copy().apply { // copy the board
                move(position, potentialMoveTo) // move the piece on the copied board
            }.let { modifiedCopiedBoard ->
                !isCheck(forWhite = forWhite, board = modifiedCopiedBoard) // check if this move leads to check
            }
        }.toSet()
    }

    /**
     * Detects if there's a check for [forWhite].
     */
    fun isCheck(forWhite: Boolean, board: Board): Boolean {
        val kingPosition = board.getKingPosition(forWhite)

        return board.getAllEnemyPieces(forWhite).keys.any { enemyPiecePosition ->
            calculateMoves(enemyPiecePosition, board).any { potentialCheckMove ->
                potentialCheckMove == kingPosition
            }
        }
    }

    /**
     * Detects if there's a checkmate for [forWhite].
     * If [forWhite] has any moves that can possibly get king out of check, then there's no checkmate.
     */
    fun isCheckmate(forWhite: Boolean, board: Board): Boolean {
        val hasAnyMoves = board.getAllPieces(forWhite = forWhite).keys.any { position ->
            calculateMovesWithCheckMovesFiltered(position, forWhite, board).isNotEmpty()
        }
        return !hasAnyMoves
    }

    private fun calculateMoves(position: Indexes, board: Board): Set<Indexes> {
        val piece = board[position] ?: errorNoFigureAtCell(position)
        return movesCalculatorStrategy[piece.type]?.calculateMoves(position, piece, board)
            ?: error("No strategy found for type ${piece.type}")
    }
}