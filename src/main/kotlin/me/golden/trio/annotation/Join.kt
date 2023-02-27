package me.golden.trio.annotation

import me.golden.trio.loader.builder.JoinType

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Join(val type: JoinType, val separator: String = "")
