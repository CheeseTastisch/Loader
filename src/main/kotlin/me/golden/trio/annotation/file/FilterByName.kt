package me.golden.trio.annotation.file

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class FilterByName(val value: String)
