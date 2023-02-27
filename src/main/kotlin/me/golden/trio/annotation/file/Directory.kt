package me.golden.trio.annotation.file

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Directory(val value: String)
