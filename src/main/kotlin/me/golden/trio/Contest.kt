package me.golden.trio

import me.golden.trio.annotation.AnnotationCaller
import me.golden.trio.loader.builder.FileLoaderBuilder
import me.golden.trio.loader.builder.StringLoaderBuilder
import kotlin.reflect.KFunction

object Contest {

    fun useFile() = FileLoaderBuilder()

    fun useString() = StringLoaderBuilder()

    fun withAnnotation(function: KFunction<*>) = AnnotationCaller.call(function)

    fun withAnnotation(vararg `package`: String) = AnnotationCaller.callAll(*`package`)

}
