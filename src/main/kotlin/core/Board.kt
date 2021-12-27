package core

//todo список съеденных фигур
data class Board(private val _board: MutableMap<Indexes, Piece>) {

    companion object {
        const val SIZE = 8
        const val LAST_INDEX = SIZE - 1
        val INDICES = 0..LAST_INDEX

        const val PAWNS_BLACK_INITIAL_ROW_INDEX = 1
        const val PAWNS_WHITE_INITIAL_ROW_INDEX = 6
    }

    private val _kingsPositions: MutableMap<Boolean, Indexes> = _board.filterValues {
        it.type == PieceType.KING
    }.map { (position, piece) ->
        piece.isWhite to position
    }.toMap(HashMap())

    val board: Map<Indexes, Piece> by ::_board
    val kingsPositions: Map<Boolean, Indexes> by ::_kingsPositions

    operator fun get(indexes: Indexes): Piece? = board[indexes]

    // this is used to also make a copy of board map
    fun copy() = this.copy(_board = _board.toMutableMap())

    fun isCellEmpty(indexes: Indexes): Boolean = this[indexes] == null

    fun isEnemy(indexes: Indexes, isWhite: Boolean): Boolean = this[indexes]?.let {
        it.isWhite != isWhite
    } ?: false

    fun getAllEnemyPieces(isWhiteMove: Boolean) = board.filterValues {
        it.isWhite != isWhiteMove
    }

    fun move(from: Indexes, to: Indexes) {
        val piece = _board.remove(from) ?: errorNoFigureAtCell(from)
        _board[to] = piece

        if (piece.type == PieceType.KING) {
            _kingsPositions[piece.isWhite] = to
        }
    }
}