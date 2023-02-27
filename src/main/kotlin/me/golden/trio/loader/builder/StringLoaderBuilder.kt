package me.golden.trio.loader.builder

import me.golden.trio.loader.load.impl.StringLoader

class StringLoaderBuilder : LoaderBuilder<StringLoaderBuilder>() {

    internal var strings =  listOf<String>()

    fun setStrings(strings: List<String>): StringLoaderBuilder {
        this.strings = strings
        return this
    }

    fun setStrings(vararg strings: String) = setStrings(strings.toList())

    override fun build() = StringLoader(this)

    override fun getThis() = this

}