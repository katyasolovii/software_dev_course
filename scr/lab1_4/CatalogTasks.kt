data class Product(
    val id: Int,
    val name: String,
    val category: String,
    val price: Double,
    val rating: Double,
    val inStock: Boolean
)

val products = listOf(
    Product(1, "Google Pixel 9", "Smartphones", 899.0, 4.8, true),
    Product(2, "iPhone 16 Pro", "Smartphones", 1199.0, 4.9, true),
    Product(3, "Galaxy A55", "Smartphones", 399.0, 4.3, false),
    Product(4, "MacBook Air M3", "Laptops", 1299.0, 4.9, true),
    Product(5, "ThinkPad X1 Carbon", "Laptops", 1499.0, 4.6, false),
    Product(6, "Dell XPS 13", "Laptops", 1150.0, 4.4, true),
    Product(7, "Sony WH-1000XM5", "Audio", 349.0, 4.7, true),
    Product(8, "AirPods Pro 2", "Audio", 249.0, 4.8, false),
    Product(9, "Pixel Buds Pro 2", "Audio", 229.0, 4.5, true)
)

fun List<Product>.filterAndTransform(
    predicate: (Product) -> Boolean,
    transform: (Product) -> String
): List<String> {
    val result = mutableListOf<String>()
    for (product in this) {
        if (predicate(product)) {
            result.add(transform(product))
        }
    }
    return result
}

fun main() {
    println("Частина 1. Ланцюжки трансформації даних")
    products
        .filter { it.inStock && it.rating >= 4.7 && it.price < 1000.0 }
        .sortedByDescending { it.rating }
        .map { "Назва: ${it.name} | Рейтинг: ${it.rating} | Ціна: $${it.price}" }
        .forEach { println(it) }

    println("\nЧастина 2. Пошук та предикатні перевірки")
    val expensiveLaptop = products.find { it.category == "Laptops" && it.price > 1200.0 }
    println("Ноутбук дорожчий за $1200: ${expensiveLaptop?.name ?: "Товар не знайдено"}")

    val hasAudioOver300 = products.any { it.category == "Audio" && it.price > 300.0 }
    println("Чи є аудіо дорожче $300: $hasAudioOver300")

    val allSmartphonesAbove4 = products
        .filter { it.category == "Smartphones" }
        .all { it.rating > 4.0 }
    println("Чи всі смартфони мають рейтинг > 4.0: $allSmartphonesAbove4")

    println("\nЧастина 3. Розділення та групування вибірки")
    val (available, outOfStock) = products.partition { it.inStock }
    println("Кількість товарів у наявності: ${available.size}")
    println("Кількість товарів, яких немає на складі: ${outOfStock.size}")

    val groupedByCategory = products.groupBy { it.category }
    groupedByCategory.forEach { (category, items) ->
        val mostExpensive = items.maxByOrNull { it.price }
        mostExpensive?.let {
            println("Категорія $category -> Найдорожчий: ${it.name} ($${it.price})")
        }
    }

    println("\n4. Власна функція вищого порядку")
    val promoMessages = products.filterAndTransform(
        predicate = { it.price < 300.0 }
    ) { product ->
        "Акційна ціна на ${product.name}: лише $${product.price}!"
    }

    promoMessages.forEach { println(it) }
}