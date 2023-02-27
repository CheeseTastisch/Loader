package me.golden.trio.loader.builder

import me.golden.trio.loader.load.impl.FileLoader
import java.io.File

class FileLoaderBuilder : LoaderBuilder<FileLoaderBuilder>() {

    internal var directory: File? = null
    internal var filters = mutableListOf<(File) -> Boolean>()

    fun setDirectory(directory: File): FileLoaderBuilder {
        this.directory = directory
        return this
    }

    fun setDirectory(directory: String) = setDirectory(File(directory))

    fun filter(filter: (File) -> Boolean): FileLoaderBuilder {
        filters.add(filter)
        return this
    }

    fun filterByName(filter: (String) -> Boolean) = filter { filter(it.name) }

    fun filterByName(vararg names: String) = filterByName { names.contains(it) }

    fun filterByExtension(filter: (String) -> Boolean) = filter { filter(it.extension) }

    fun filterByExtension(vararg extensions: String) = filterByExtension { extensions.contains(it) }

    fun filterOutFiles() = filterByExtension { it != "out" }

    override fun build() = FileLoader(this)

    override fun getThis() = this

}