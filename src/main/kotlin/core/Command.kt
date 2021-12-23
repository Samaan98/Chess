package core

sealed class Command {

    data class Move(
        val from: Indexes,
        val to: Indexes
    ) : Command()
}