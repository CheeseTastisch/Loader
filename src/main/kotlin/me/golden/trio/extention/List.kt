package me.golden.trio.extention

fun <T : Any> MutableList<T>.removeFirst(amount: Int): List<T> {
    val taken = this.take(amount)
    for(i in 0 until amount) this.removeFirst()

    return taken
}