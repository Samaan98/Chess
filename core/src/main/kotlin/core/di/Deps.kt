package core.di

import core.board.BoardFactory
import core.board.BoardUi
import core.board.PawnPromotionCallback
import core.command.CommandProcessor
import core.command.InputProcessor
import core.moves_calculator.*

internal class Deps(pawnPromotionCallback: PawnPromotionCallback) {

    val board = BoardFactory(pawnPromotionCallback).createBoard()

    private val boardIterator = BoardIterator()

    private val rookMovesCalculatorStrategy = RookMovesCalculatorStrategy(boardIterator)
    private val bishopMovesCalculatorStrategy = BishopMovesCalculatorStrategy(boardIterator)
    private val knightMovesCalculatorStrategy = KnightMovesCalculatorStrategy()

    private val checkDetector = CheckDetector(
        boardIterator,
        rookMovesCalculatorStrategy,
        bishopMovesCalculatorStrategy,
        knightMovesCalculatorStrategy
    )

    private val movesCalculator = MovesCalculator(
        checkDetector,
        PawnMovesCalculatorStrategy(),
        rookMovesCalculatorStrategy,
        knightMovesCalculatorStrategy,
        bishopMovesCalculatorStrategy,
        QueenMovesCalculatorStrategy(
            boardIterator,
            rookMovesCalculatorStrategy,
            bishopMovesCalculatorStrategy
        ),
        KingMovesCalculatorStrategy(checkDetector)
    )

    val commandProcessor = CommandProcessor(board, movesCalculator)
    val boardUi = BoardUi()
    val inputProcessor = InputProcessor(boardUi)
}