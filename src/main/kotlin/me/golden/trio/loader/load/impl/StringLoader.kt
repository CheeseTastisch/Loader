package me.golden.trio.loader.load.impl

import me.golden.trio.loader.builder.JoinType
import me.golden.trio.loader.builder.StringLoaderBuilder

class StringLoader(builder: StringLoaderBuilder) : Loader<StringLoaderBuilder>(builder) {

    override fun getInputs(): List<Pair<List<String>, (List<String>) -> Unit>> {
        val callback = { out: List<String> ->
            println(out.joinToString("\n"))
        }

        return when(builder.joinType) {
            JoinType.NONE -> builder.strings.map { listOf(it) to callback }
            JoinType.SPACE -> listOf(listOf(builder.strings.joinToString(" ")) to callback)
            JoinType.NEW_LINE -> listOf(listOf(builder.strings.joinToString("\n")) to callback)
            JoinType.CUSTOM -> listOf(listOf(builder.strings.joinToString(builder.separator!!)) to callback)
            JoinType.LIST -> listOf(builder.strings to callback)
        }
    }


}