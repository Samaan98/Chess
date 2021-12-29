package core.moves_calculator

import core.board.Board
import core.piece.PieceType
import core.util.Indexes
import core.util.errorNoFigureAtCell
import java.util.*

internal class MovesCalculator(
    private val checkDetector: CheckDetector,
    private val pawnMovesCalculatorStrategy: PawnMovesCalculatorStrategy,
    private val rookMovesCalculatorStrategy: RookMovesCalculatorStrategy,
    private val knightMovesCalculatorStrategy: KnightMovesCalculatorStrategy,
    private val bishopMovesCalculatorStrategy: BishopMovesCalculatorStrategy,
    private val queenMovesCalculatorStrategy: QueenMovesCalculatorStrategy,
    private val kingMovesCalculatorStrategy: KingMovesCalculatorStrategy
) {

    private val movesCalculatorStrategy = EnumMap<PieceType, MovesCalculatorStrategy>(PieceType::class.java).apply {
        PieceType.values().forEach {
            val strategy = when (it) {
                PieceType.PAWN -> pawnMovesCalculatorStrategy
                PieceType.ROOK -> rookMovesCalculatorStrategy
                PieceType.KNIGHT -> knightMovesCalculatorStrategy
                PieceType.BISHOP -> bishopMovesCalculatorStrategy
                PieceType.QUEEN -> queenMovesCalculatorStrategy
                PieceType.KING -> kingMovesCalculatorStrategy
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

    fun isCheck(forWhite: Boolean, board: Board): Boolean {
        return checkDetector.isCheck(forWhite, board)
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