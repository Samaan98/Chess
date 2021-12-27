package core.command

import core.util.Indexes

internal sealed class Command {

    data class Move(
        val from: Indexes,
        val to: Indexes
    ) : Command()

    data class GetAvailableMoves(val from: Indexes) : Command()
}