package core

sealed class Command {

    data class Move(
        val from: Indexes,
        val to: Indexes
    ) : Command()

    data class GetAvailableMoves(val from: Indexes) : Command()
}

sealed class CommandResult {
    data class AvailableMoves(val data: Set<Indexes>) : CommandResult()
}