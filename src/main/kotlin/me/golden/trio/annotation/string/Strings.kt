package me.golden.trio.annotation.string

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Strings(vararg val strings: String)
