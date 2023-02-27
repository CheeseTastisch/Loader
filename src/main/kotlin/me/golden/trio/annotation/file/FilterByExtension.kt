package me.golden.trio.annotation.file

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class FilterByExtension(val value: String)
