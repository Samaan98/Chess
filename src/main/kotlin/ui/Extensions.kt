package ui

import core.piece.Piece
import core.piece.PieceType

val Piece.symbol: Char
    get() = when (type) {
        PieceType.PAWN -> blackOrWhite('♟', '♙')
        PieceType.ROOK -> blackOrWhite('♜', '♖')
        PieceType.KNIGHT -> blackOrWhite('♞', '♘')
        PieceType.BISHOP -> blackOrWhite('♝', '♗')
        PieceType.QUEEN -> blackOrWhite('♛', '♕')
        PieceType.KING -> blackOrWhite('♚', '♔')
    }