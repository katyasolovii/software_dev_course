package com.example.calculator

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val TAG = "SimpleCalculator"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etFirstNumber = findViewById<EditText>(R.id.etFirstNumber)
        val etSecondNumber = findViewById<EditText>(R.id.etSecondNumber)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnSubtract = findViewById<Button>(R.id.btnSubtract)
        val btnMultiply = findViewById<Button>(R.id.btnMultiply)
        val btnDivide = findViewById<Button>(R.id.btnDivide)

        fun calculate(operation: String) {
            val text1 = etFirstNumber.text.toString()
            val text2 = etSecondNumber.text.toString()

            if (text1.isEmpty() || text2.isEmpty()) {
                tvResult.text = "Будь ласка, введіть обидва числа"
                Log.w(TAG, "Спроба обчислення з порожніми полями")
                return
            }

            val num1 = text1.toDouble()
            val num2 = text2.toDouble()

            Log.d(TAG, "Обчислення: $num1 $operation $num2")

            if (operation == "/" && num2 == 0.0) {
                tvResult.text = "Помилка: ділення на нуль неможливе!"
                Log.w(TAG, "Помилка: ділення на нуль")
                return
            }

            val result = when (operation) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "*" -> num1 * num2
                "/" -> num1 / num2
                else -> 0.0
            }

            tvResult.text = "Результат: $result"
        }
        btnAdd.setOnClickListener { calculate("+") }
        btnSubtract.setOnClickListener { calculate("-") }
        btnMultiply.setOnClickListener { calculate("*") }
        btnDivide.setOnClickListener { calculate("/") }
    }
}