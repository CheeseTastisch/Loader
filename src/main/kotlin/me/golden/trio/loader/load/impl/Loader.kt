package me.golden.trio.loader.load.impl

import me.golden.trio.loader.builder.JoinType
import me.golden.trio.loader.builder.LoaderBuilder
import me.golden.trio.loader.load.ILoader

sealed class Loader<Builder : LoaderBuilder<Builder>>(internal var builder: Builder) : ILoader {

    init {
        if (builder.solver == null) throw IllegalArgumentException("Solver cannot be null")
        if (builder.joinType == JoinType.CUSTOM && builder.separator == null)
            throw IllegalArgumentException("Separator cannot be null when join type is custom")
    }

    abstract fun getInputs(): List<Pair<List<String>, (List<String>) -> Unit>>

    override fun execute() {
        val inputs = getInputs()
        val solver = builder.solver!!

        inputs.forEach { (input, callback) ->
            val output = solver.solve(input.toMutableList())
            callback(output)
        }
    }

}