import kotlin.random.Random

fun generateRandomArray(size: Int, maxValue: Int): IntArray? {
    if (size <= 0 || maxValue <= 0 ) return null
    return IntArray(size) { Random.nextInt(0, maxValue + 1) }
}

fun main() {
    generateRandomArray(10, 50) ?. let { array ->
        array.apply {
            for (i in indices) {
                this[i] = if (this[i] % 2 != 0) this[i]*2 else this[i]/2
            }
        }
            .also { println("Змінений масив: ${it.contentToString()}") }
            .run { maxOrNull() }
            .also { println("Максимальне значення масиву: $it") }
    } ?: println("Помилка вхідних даних")
}