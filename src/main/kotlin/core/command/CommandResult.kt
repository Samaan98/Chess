package core.command

import core.util.Indexes

sealed class CommandResult {
    object Success : CommandResult()
    object Check : CommandResult()
    object Checkmate : CommandResult()
    data class AvailableMoves(val data: Set<Indexes>) : CommandResult()
}
