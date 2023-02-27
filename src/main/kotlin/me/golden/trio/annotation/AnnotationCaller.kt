package me.golden.trio.annotation

import io.github.classgraph.ClassGraph
import me.golden.trio.Contest
import me.golden.trio.annotation.file.Directory
import me.golden.trio.annotation.file.FilterByExtension
import me.golden.trio.annotation.file.FilterByName
import me.golden.trio.annotation.file.FilterOutFiles
import me.golden.trio.annotation.string.Strings
import kotlin.reflect.KFunction
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.kotlinFunction

internal object AnnotationCaller {

    fun callAll(vararg `package`: String) {
        ClassGraph()
            .enableAllInfo()
            .acceptPackages(*`package`)
            .scan()
            .getClassesWithMethodAnnotation(Use::class.java.name)
            .forEach { clazz ->
                clazz.loadClass()
                    .methods
                    .mapNotNull { it.kotlinFunction }
                    .filter { it.annotations.any { annotation -> annotation is Use } }
                    .forEach { method -> call(method) }
            }
    }

    fun call(method: KFunction<*>) {
        val annotation = method.annotations
        if (annotation.any { it is Disable }) return

        if (method.parameters.size != 1) throw IllegalArgumentException("Method must have one parameter")
        if (!method.parameters[0].type.isSubtypeOf(
                MutableList::class.createType(
                    listOf(KTypeProjection.invariant(String::class.starProjectedType))
                )
            )
        ) throw IllegalArgumentException("Parameter must be MutableList<String>")

        if (!method.returnType.isSubtypeOf(
                List::class.createType(
                    listOf(KTypeProjection.invariant(String::class.starProjectedType))
                )
            )
            && method.returnType != String::class.starProjectedType
        ) throw IllegalArgumentException("Method must return Unit")

        val use = annotation.find { it is Use } ?: throw IllegalArgumentException("No @Use annotation found")
        val join = annotation.find { it is Join }

        val builder = when ((use as Use).system) {
            Use.System.FILE -> {
                val directory = annotation.find { it is Directory }
                    ?: throw IllegalArgumentException("No @Directory annotation found")

                val filterByExtension = annotation.filterIsInstance<FilterByExtension>()
                val filterByName = annotation.filterIsInstance<FilterByName>()
                val filterOutFiles = annotation.any { it is FilterOutFiles }

                val builder = Contest.useFile().setDirectory((directory as Directory).value)

                if (filterByName.isNotEmpty()) builder.filterByName(*filterByName.map { it.value }.toTypedArray())
                if (filterByExtension.isNotEmpty()) builder.filterByExtension(*filterByExtension.map { it.value }
                    .toTypedArray())
                if (filterOutFiles) builder.filterOutFiles()

                builder
            }

            Use.System.STRING -> {
                val strings = annotation.find { it is Strings }
                    ?: throw IllegalArgumentException("No @Strings annotation found")

                Contest.useString().setStrings(*(strings as Strings).strings)
            }
        }

        if (join != null && join is Join) builder.setJoinType(join.type, join.separator)

        if (method.returnType == List::class) {
            builder.setToListSolver {
                @Suppress("UNCHECKED_CAST")
                method.call(it) as List<String>
            }
        } else {
            builder.setToStringSolver {
                method.call(it) as String
            }
        }

        builder.buildAndExecute()
    }

}