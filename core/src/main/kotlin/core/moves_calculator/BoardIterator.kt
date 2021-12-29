package core.moves_calculator

import core.util.Indexes

internal class BoardIterator {

    inline fun iterate(
        movesIds: Set<String>,
        moveProducer: (String, Int) -> Indexes,
        iterationAction: (String, Indexes) -> Boolean
    ) {
        val moveIdToCanMove = mutableMapOf<String, Boolean>()

        var hasDataToIterate = true
        var nextCellIndex = 1

        while (hasDataToIterate) {
            movesIds.forEach { moveId ->
                val canMove = moveIdToCanMove.getOrDefault(moveId, true)
                if (canMove) {
                    val move = moveProducer(moveId, nextCellIndex)
                    moveIdToCanMove[moveId] = iterationAction(moveId, move)
                }
            }
            hasDataToIterate = moveIdToCanMove.values.any { it }
            nextCellIndex++
        }
    }
}