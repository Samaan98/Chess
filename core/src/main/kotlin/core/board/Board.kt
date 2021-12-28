package core.board

import core.piece.Piece
import core.piece.PieceType
import core.util.Indexes
import core.util.errorNoFigureAtCell
import core.util.j

//todo список съеденных фигур
class Board internal constructor(private val _board: MutableMap<Indexes, Piece>) {

    companion object {
        const val SIZE = 8
        const val LAST_INDEX = SIZE - 1
        val INDICES = 0..LAST_INDEX

        const val PIECES_BLACK_INITIAL_ROW_INDEX = 0
        const val PIECES_WHITE_INITIAL_ROW_INDEX = LAST_INDEX
        const val PAWNS_BLACK_INITIAL_ROW_INDEX = PIECES_BLACK_INITIAL_ROW_INDEX + 1
        const val PAWNS_WHITE_INITIAL_ROW_INDEX = PIECES_WHITE_INITIAL_ROW_INDEX - 1
    }

    private val kingsPositions = _board.filterValues {
        it.type == PieceType.KING
    }.map { (position, piece) ->
        piece.isWhite to position
    }.toMap(mutableMapOf())

    private val isLeftCastlingAvailable = mutableMapOf(
        true to true,
        false to true
    )

    private val isRightCastlingAvailable = mutableMapOf(
        true to true,
        false to true
    )

    val board: Map<Indexes, Piece> by ::_board

    operator fun get(indexes: Indexes): Piece? = board[indexes]

    fun copy() = Board(_board.toMutableMap())

    fun isCellEmpty(indexes: Indexes): Boolean = this[indexes] == null

    fun isEnemy(indexes: Indexes, isWhite: Boolean): Boolean = this[indexes]?.let {
        it.isWhite != isWhite
    } ?: false

    fun isLeftCastlingAvailable(forWhite: Boolean) = isLeftCastlingAvailable.getOrDefault(forWhite, false)
    fun isRightCastlingAvailable(forWhite: Boolean) = isRightCastlingAvailable.getOrDefault(forWhite, false)

    fun getKingPosition(forWhite: Boolean) = kingsPositions[forWhite] ?: error("No king on board")

    fun getAllEnemyPieces(forWhite: Boolean) = board.filterValues {
        it.isWhite != forWhite
    }

    fun getAllPieces(forWhite: Boolean) = board.filterValues {
        it.isWhite == forWhite
    }

    fun move(from: Indexes, to: Indexes) {
        val piece = _board.remove(from) ?: errorNoFigureAtCell(from)
        _board[to] = piece

        if (piece.type == PieceType.KING) {
            kingsPositions[piece.isWhite] = to
        }

        updateCastlingAvailability(piece, from)
    }

    /**
     * Castling is permissible when neither the king nor the kingside or queenside
     * rook on the same rank has previously moved.
     */
    private fun updateCastlingAvailability(piece: Piece, from: Indexes) {
        val isWhite = piece.isWhite
        val isAnyCastlingAvailable = isLeftCastlingAvailable(isWhite) || isRightCastlingAvailable(isWhite)
        if (!isAnyCastlingAvailable) return

        val isKing = piece.type == PieceType.KING

        if (isKing || piece.type == PieceType.ROOK) {

            if (isKing) {
                arrayOf(
                    isLeftCastlingAvailable,
                    isRightCastlingAvailable
                ).forEach {
                    it[isWhite] = false
                }
            } else {
                if (from.j == 0) { // left rook
                    isLeftCastlingAvailable
                } else { // right rook
                    isRightCastlingAvailable
                }[isWhite] = false
            }
        }
    }
}