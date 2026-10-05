data class Employee(val firstName: String, val lastName: String, val position: String) {
    var bonus: Int = 0
}

class ManualEmployee(val firstName: String, val lastName: String, val position: String) {
    var bonus: Int = 0

    override fun toString(): String {
        return "ManualEmployee(firstName=$firstName, lastName=$lastName, position=$position)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ManualEmployee) return false
        return firstName == other.firstName &&
                lastName == other.lastName &&
                position == other.position
    }

    override fun hashCode(): Int {
        var result = firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + position.hashCode()
        return result
    }

    operator fun component1(): String = firstName
    operator fun component2(): String = lastName
    operator fun component3(): String = position

    fun copy(
        firstName: String = this.firstName,
        lastName: String = this.lastName,
        position: String = this.position
    ): ManualEmployee {
        return ManualEmployee(firstName, lastName, position)
    }
}

fun main() {
    println("=== 1. Перевірка Data Class (Частина 2) ===")
    val emp1 = Employee("Андрій", "Почанко", "Лікар")
    val emp2 = Employee("Андрій", "Почанко", "Лікар")
    val emp3 = emp1

    println("Працівник: $emp1")
    println("emp1 === emp2: ${emp1 === emp2}") // false (різні адреси)
    println("emp1 === emp3: ${emp1 === emp3}")
    println("emp1 == emp2: ${emp1 == emp2}")   // true (однаковий вміст)

    val (name, surname, pos) = emp1
    println("Розпаковано: $name $surname, посада: $pos")

    val promoted = emp1.copy(position = "Головний лікар")
    println("Підвищений працівник: $promoted")

    val empA = Employee("Микола", "Чубко", "Художник").apply { bonus = 500 }
    val empB = Employee("Микола", "Чубко", "Художник").apply { bonus = 1000 }
    println("empA == empB (з різним bonus): ${empA == empB}") // true, бо bonus поза конструктором


    println("\n=== 2. Перевірка ручного класу ManualEmployee (Частина 3) ===")
    val manualEmp1 = ManualEmployee("Андрій", "Почанко", "Лікар")
    val manualEmp2 = ManualEmployee("Андрій", "Почанко", "Лікар")

    println("Друк через toString: $manualEmp1")
    println("manualEmp1 == manualEmp2 через equals: ${manualEmp1 == manualEmp2}")

    val (mName, mSurname, mPos) = manualEmp1
    println("Розпаковано ручний клас: $mName $mSurname, $mPos")

    val manualPromoted = manualEmp1.copy(position = "Головний лікар")
    println("Клонований через copy: $manualPromoted")
}