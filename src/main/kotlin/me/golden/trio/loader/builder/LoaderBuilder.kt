package me.golden.trio.loader.builder

import me.golden.trio.loader.load.ILoader
import me.golden.trio.loader.solver.ISolver

abstract class LoaderBuilder<Type : LoaderBuilder<Type>> {

    internal var solver: ISolver? = null
    internal var joinType: JoinType = JoinType.LIST
    internal var separator: String? = null

    fun setSolver(solver: ISolver): Type {
        this.solver = solver
        return getThis()
    }

    fun setToStringSolver(solver: (MutableList<String>) -> String) = setSolver(object : ISolver {
        override fun solve(input: MutableList<String>) = listOf(solver(input))
    })

    fun setToListSolver(solver: (MutableList<String>) -> List<String>) = setSolver(object : ISolver {
        override fun solve(input: MutableList<String>) = solver(input)
    })

    fun setJoinType(joinType: JoinType) = setJoinType(joinType, null)

    fun setJoinType(joinType: JoinType, separator: String?): Type {
        this.joinType = joinType
        this.separator = separator
        return getThis()
    }

    fun setStringJoin(separator: String) = setJoinType(JoinType.CUSTOM, separator)

    abstract fun build(): ILoader

    fun buildAndExecute(): ILoader {
        val loader = build()
        loader.execute()

        return loader
    }

    internal abstract fun getThis(): Type

}