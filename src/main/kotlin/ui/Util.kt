package ui

import core.Piece
import core.PieceType

val Piece.symbol: Char
    get() = when (type) {
        PieceType.PAWN -> blackOrWhite('♟', '♙')
        PieceType.ROOK -> blackOrWhite('♜', '♖')
        PieceType.KNIGHT -> blackOrWhite('♞', '♘')
        PieceType.BISHOP -> blackOrWhite('♝', '♗')
        PieceType.QUEEN -> blackOrWhite('♛', '♕')
        PieceType.KING -> blackOrWhite('♚', '♔')
    }