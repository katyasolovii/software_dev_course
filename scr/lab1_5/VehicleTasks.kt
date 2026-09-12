data class Vehicle(
    var brand: String = "",
    var model: String = "",
    var year: Int = 0,
    var licensePlate: String = ""
)

fun main(){
    val vehicle_1 = Vehicle().apply {
        brand = "Honda"
        model = "Civic"
        year = 2020
        licensePlate = "КА3344ВІ"
    }.also {
        println("Створено новий транспортний засіб: $it")
    }

    with(vehicle_1){
        println("Марка: $brand \nМодель: $model \nРік випуску: $year " +
                "\nНомерний знак: $licensePlate")
    }
}