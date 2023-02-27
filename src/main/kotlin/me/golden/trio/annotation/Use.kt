package me.golden.trio.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Use(val system: System) {

    enum class System {
        FILE,
        STRING
    }

}
