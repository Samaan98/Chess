package core

import core.board.BoardFactory
import core.board.BoardUi
import core.board.PawnPromotionCallback
import core.command.CommandProcessor
import core.command.CommandResult
import core.command.InputProcessor
import core.moves_calculator.*

//todo тесты на всю логику
// модуль зависимостей
// объявить ничью
// пат
// сдаться
// ход назад
// таймер
// король может атаковать короля противника лишь при помощи «вскрытого» шаха
// ПРОЧИТАТЬ ПРО ВСЕ КОРНЕРКЕЙСЫ, КОТОРЫХ ПРОСТО, СУКА, МИЛЛИОН
// параллельные вычисления для сложных операций
class Chess(pawnPromotionCallback: PawnPromotionCallback) {

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

    private val commandProcessor = CommandProcessor(board, movesCalculator)

    val boardUi = BoardUi()
    private val inputProcessor = InputProcessor(boardUi)

    fun processInputCommand(input: String): CommandResult {
        val command = inputProcessor.parse(input)
        return commandProcessor.process(command)
    }
}