package core.board

import core.piece.Piece
import core.piece.PieceType
import core.util.Indexes
import core.util.errorNoFigureAtCell
import core.util.i
import core.util.j
import kotlin.math.abs

//todo список съеденных фигур
class Board internal constructor(
    private val _board: MutableMap<Indexes, Piece>,
    kingsPositions: MutableMap<Boolean, Indexes>? = null,
    isLeftCastlingAvailable: MutableMap<Boolean, Boolean>? = null,
    isRightCastlingAvailable: MutableMap<Boolean, Boolean>? = null,
    isWhiteMove: Boolean? = null,
    canBeCapturedEnPassant: Indexes? = null
) {

    companion object {
        const val SIZE = 8
        const val LAST_INDEX = SIZE - 1
        val INDICES = 0..LAST_INDEX

        const val PIECES_BLACK_INITIAL_ROW_INDEX = 0
        const val PIECES_WHITE_INITIAL_ROW_INDEX = LAST_INDEX
        const val PAWNS_BLACK_INITIAL_ROW_INDEX = PIECES_BLACK_INITIAL_ROW_INDEX + 1
        const val PAWNS_WHITE_INITIAL_ROW_INDEX = PIECES_WHITE_INITIAL_ROW_INDEX - 1

        const val ROOK_LEFT_INDEX = 0
        const val ROOK_RIGHT_INDEX = LAST_INDEX

        const val DISTANCE_FROM_KING_TO_LEFT_ROOK = 3
        const val DISTANCE_FROM_KING_TO_RIGHT_ROOK = 2

        const val EN_PASSANT_PAWN_WHITE_ROW_INDEX = 3
        const val EN_PASSANT_PAWN_BLACK_ROW_INDEX = 4
    }

    private val kingsPositions = kingsPositions ?: _board.filterValues {
        it.type == PieceType.KING
    }.map { (position, piece) ->
        piece.isWhite to position
    }.toMap(mutableMapOf())

    private val isLeftCastlingAvailable = isLeftCastlingAvailable ?: mutableMapOf(
        true to true,
        false to true
    )

    private val isRightCastlingAvailable = isRightCastlingAvailable ?: mutableMapOf(
        true to true,
        false to true
    )

    var isWhiteMove = isWhiteMove ?: true
        private set

    var canBeCapturedEnPassant: Indexes? = canBeCapturedEnPassant
        private set

    val board: Map<Indexes, Piece> by ::_board

    operator fun get(indexes: Indexes): Piece? = board[indexes]

    /**
     * Copy the board. Primarily used to detect hypothetical checks.
     * This copy is expected to be kinda like a deep copy.
     */
    fun copy() = Board(
        _board = _board.toMutableMap(),
        kingsPositions = kingsPositions.toMutableMap(),
        isLeftCastlingAvailable = isLeftCastlingAvailable.toMutableMap(),
        isRightCastlingAvailable = isRightCastlingAvailable.toMutableMap(),
        isWhiteMove = isWhiteMove,
        canBeCapturedEnPassant = canBeCapturedEnPassant?.copy()
    )

    fun toggleMove() {
        isWhiteMove = !isWhiteMove
    }

    fun isCellEmpty(indexes: Indexes): Boolean = this[indexes] == null

    fun isEnemy(indexes: Indexes): Boolean = this[indexes]?.let {
        it.isWhite != isWhiteMove
    } ?: false

    fun isLeftCastlingAvailable() = isLeftCastlingAvailable.getOrDefault(isWhiteMove, false)
    fun isRightCastlingAvailable() = isRightCastlingAvailable.getOrDefault(isWhiteMove, false)

    fun getKingPosition() = kingsPositions[isWhiteMove] ?: error("No king on board")

    fun getAllEnemyPieces() = board.filterValues {
        it.isWhite != isWhiteMove
    }

    fun getAllMyPieces() = board.filterValues {
        it.isWhite == isWhiteMove
    }

    fun move(from: Indexes, to: Indexes) {
        val lastCanBeCapturedEnPassant = canBeCapturedEnPassant
        canBeCapturedEnPassant = null

        val piece = _board.remove(from) ?: errorNoFigureAtCell(from)
        _board[to] = piece

        when (piece.type) {
            PieceType.KING -> {
                kingsPositions[piece.isWhite] = to
                performCastlingIfNeeded(from, to)
                updateCastlingAvailability(piece, from)
            }
            PieceType.ROOK -> {
                updateCastlingAvailability(piece, from)
            }
            PieceType.PAWN -> {
                performEnPassantCaptureIfNeeded(lastCanBeCapturedEnPassant, to)
                if (abs(to.i - from.i) == 2) { // double-step move
                    canBeCapturedEnPassant = to
                }
            }
            else -> {
                // do nothing
            }
        }
    }

    private fun performCastlingIfNeeded(from: Indexes, to: Indexes) {
        val isCastling = abs(to.j - from.j) == 2
        if (!isCastling) return

        val isLeftCastling = to.j < from.j

        val rookFromJ = if (isLeftCastling) ROOK_LEFT_INDEX else ROOK_RIGHT_INDEX
        val rookToJ = rookFromJ + if (isLeftCastling) {
            DISTANCE_FROM_KING_TO_LEFT_ROOK
        } else {
            -DISTANCE_FROM_KING_TO_RIGHT_ROOK
        }
        val rookFrom = from.i to rookFromJ
        val rookTo = from.i to rookToJ

        move(rookFrom, rookTo)
    }

    /**
     * Castling is permissible when neither the king nor the kingside or queenside rook on the same rank
     * has previously moved.
     */
    private fun updateCastlingAvailability(piece: Piece, from: Indexes) {
        val isAnyCastlingAvailable = isLeftCastlingAvailable() || isRightCastlingAvailable()
        if (!isAnyCastlingAvailable) return

        val isKing = piece.type == PieceType.KING
        if (isKing || piece.type == PieceType.ROOK) {
            val isWhite = piece.isWhite
            if (isKing) {
                arrayOf(
                    isLeftCastlingAvailable,
                    isRightCastlingAvailable
                ).forEach {
                    it[isWhite] = false
                }
            } else {
                if (from.j == ROOK_LEFT_INDEX) {
                    isLeftCastlingAvailable
                } else {
                    isRightCastlingAvailable
                }[isWhite] = false
            }
        }
    }

    private fun performEnPassantCaptureIfNeeded(lastCanBeCapturedEnPassant: Indexes?, to: Indexes) {
        if (lastCanBeCapturedEnPassant == null) return
        if (lastCanBeCapturedEnPassant.j == to.j) {
            _board.remove(lastCanBeCapturedEnPassant)
        }
    }
}