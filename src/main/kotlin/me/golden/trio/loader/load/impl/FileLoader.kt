package me.golden.trio.loader.load.impl

import me.golden.trio.loader.builder.FileLoaderBuilder
import me.golden.trio.loader.builder.JoinType
import org.apache.commons.io.FileUtils
import java.io.File

class FileLoader(builder: FileLoaderBuilder) : Loader<FileLoaderBuilder>(builder) {

    init {
        if (builder.directory == null) throw IllegalArgumentException("Directory must be set")
    }

    override fun getInputs(): List<Pair<List<String>, (List<String>) -> Unit>> {
        val files = builder.directory!!.listFiles() ?: throw IllegalArgumentException("Directory must be a directory")
        val filters = builder.filters

        val inputs = mutableListOf<Pair<List<String>, (List<String>) -> Unit>>()

        files
            .filter { file -> filters.all { it(file) } }
            .forEach { file ->
                val input = when (builder.joinType) {
                    JoinType.SPACE -> listOf(file.readLines().joinToString(" "))
                    JoinType.NEW_LINE -> listOf(file.readLines().joinToString("\n"))
                    JoinType.CUSTOM -> listOf(file.readLines().joinToString(builder.separator!!))
                    else -> file.readLines()
                }

                if (builder.joinType == JoinType.NONE) {
                    inputs.addAll(
                        input.mapIndexed { index, line ->
                            listOf(line) to { out ->
                                val outFile = File(file.parent, file.name + ".${index + 1}.out")
                                FileUtils.touch(outFile)
                                FileUtils.writeLines(outFile, out, false)
                            }
                        }
                    )
                } else {
                    inputs.add(input to { out ->
                        val outFile = File(file.parent, file.name + ".out")
                        FileUtils.touch(outFile)
                        FileUtils.writeLines(outFile, out, false)
                    })
                }
            }

        return inputs
    }

}