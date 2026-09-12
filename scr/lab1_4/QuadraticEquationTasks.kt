import kotlin.math.sqrt
import kotlin.random.Random

class QuadraticEquation(a: Double, var b: Double, var c: Double) {
    var a: Double = a
        set(value) {
            require(value != 0.0) { "Коефіцієнт 'a' не може бути нулем!" }
            field = value
        }

    init {
        require(a != 0.0) { "Коефіцієнт 'a' не може бути нулем!" }
    }
    constructor(a: Int, b: Int, c: Int) : this(a.toDouble(), b.toDouble(), c.toDouble())
    constructor(b: Double, c: Double) : this(1.0, b, c)

    val discriminant: Double
        get() = b * b - 4 * a * c

    fun solve(): List<Double> {
        val d = discriminant
        return when {
            d > 0.0 -> {
                val x1 = (-b + sqrt(d)) / (2 * a)
                val x2 = (-b - sqrt(d)) / (2 * a)
                listOf(x1, x2)
            }
            d == 0.0 -> {
                val x = -b / (2 * a)
                listOf(x)
            }
            else -> emptyList()
        }
    }
    override fun toString(): String {
        val bSign = if (b >= 0) "+ $b" else "- ${-b}"
        val cSign = if (c >= 0) "+ $c" else "- ${-c}"
        return "${a}x^2 $bSign x $cSign = 0"
    }
}

fun main() {
    val eq1 = QuadraticEquation(1.0, -5.0, 6.0)
    println("Рівняння 1: $eq1")
    println("Дискримінант: ${eq1.discriminant}")
    println("Корені: ${eq1.solve()}\n")

    val eq2 = QuadraticEquation(1, -2, 1)
    println("Рівняння 2 (цілочисельне): $eq2")
    println("Корені: ${eq2.solve()}\n")

    val eq3 = QuadraticEquation(4.0, 4.0)
    println("Рівняння 3 (зведене): $eq3")
    println("Корені: ${eq3.solve()}\n")

    println("Початковий стан: $eq1 | D = ${eq1.discriminant}")
    eq1.b = 2.0
    eq1.c = 1.0
    println("Після зміни b=2.0, c=1.0: $eq1")
    println("Новий дискримінант: ${eq1.discriminant}")
    println("Нові корені: ${eq1.solve()}\n")

    try {
        eq1.a = 0.0
    } catch (e: IllegalArgumentException) {
        println("Сетер успішно перехопив помилку: ${e.message}\n")
    }

    println("Генерація 100 рівнянь та фільтрація")
    val equations = List(100) {
        var randA: Int
        do {
            randA = Random.nextInt(-50, 51)
        } while (randA == 0)

        QuadraticEquation(
            randA,
            Random.nextInt(-50, 51),
            Random.nextInt(-50, 51)
        )
    }
    val withTwoRoots = equations.filter { it.discriminant > 0.0 }
    withTwoRoots.forEach { eq ->
        println("$eq -> корені: ${eq.solve()}")
    }
    println("\nЗагальна кількість знайдених рівнянь із 2 коренями: ${withTwoRoots.size}")
}