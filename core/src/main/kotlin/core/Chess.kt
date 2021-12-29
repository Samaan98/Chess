package core

import core.board.BoardFactory
import core.board.BoardUi
import core.command.Command
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
class Chess {

    val board = BoardFactory().createBoard()

    private val boardIterator = BoardIterator()

    private val rookMovesCalculatorStrategy = RookMovesCalculatorStrategy(boardIterator)
    private val bishopMovesCalculatorStrategy = BishopMovesCalculatorStrategy(boardIterator)
    private val knightMovesCalculatorStrategy = KnightMovesCalculatorStrategy()

    private val movesCalculator = MovesCalculator(
        CheckDetector(
            boardIterator,
            rookMovesCalculatorStrategy,
            bishopMovesCalculatorStrategy,
            knightMovesCalculatorStrategy
        ),
        PawnMovesCalculatorStrategy(),
        rookMovesCalculatorStrategy,
        knightMovesCalculatorStrategy,
        bishopMovesCalculatorStrategy,
        QueenMovesCalculatorStrategy(
            boardIterator,
            rookMovesCalculatorStrategy,
            bishopMovesCalculatorStrategy
        ),
        KingMovesCalculatorStrategy()
    )

    private val commandProcessor = CommandProcessor(board, movesCalculator)

    val boardUi = BoardUi()
    private val inputProcessor = InputProcessor(boardUi)

    private var isWhiteMove = true

    fun makeMove(input: String): CommandResult {

        val command = inputProcessor.parse(input)
        val result = commandProcessor.process(command, isWhiteMove)

        if (command !is Command.GetAvailableMoves) {
            isWhiteMove = !isWhiteMove
        }

        return result
    }
}