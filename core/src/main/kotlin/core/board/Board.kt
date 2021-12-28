package core.board

import core.piece.Piece
import core.piece.PieceType
import core.util.Indexes
import core.util.errorNoFigureAtCell

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

    private val _kingsPositions: MutableMap<Boolean, Indexes> = _board.filterValues {
        it.type == PieceType.KING
    }.map { (position, piece) ->
        piece.isWhite to position
    }.toMap(mutableMapOf())

    val board: Map<Indexes, Piece> by ::_board
    val kingsPositions: Map<Boolean, Indexes> by ::_kingsPositions

    operator fun get(indexes: Indexes): Piece? = board[indexes]

    fun copy() = Board(_board.toMutableMap())

    fun isCellEmpty(indexes: Indexes): Boolean = this[indexes] == null

    fun isEnemy(indexes: Indexes, isWhite: Boolean): Boolean = this[indexes]?.let {
        it.isWhite != isWhite
    } ?: false

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
            _kingsPositions[piece.isWhite] = to
        }
    }
}