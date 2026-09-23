package com.example.calculatorfull

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calculatorfull.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var firstNumber = 0.0
    private var operation = ""
    private var isNewNumber = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fun addDigit(digit: String) {
            if (isNewNumber || binding.tvDisplay.text.toString() == "0") {
                binding.tvDisplay.text = digit
                isNewNumber = false
            } else {
                binding.tvDisplay.append(digit)
            }
        }

        binding.btn0.setOnClickListener { addDigit("0") }
        binding.btn1.setOnClickListener { addDigit("1") }
        binding.btn2.setOnClickListener { addDigit("2") }
        binding.btn3.setOnClickListener { addDigit("3") }
        binding.btn4.setOnClickListener { addDigit("4") }
        binding.btn5.setOnClickListener { addDigit("5") }
        binding.btn6.setOnClickListener { addDigit("6") }
        binding.btn7.setOnClickListener { addDigit("7") }
        binding.btn8.setOnClickListener { addDigit("8") }
        binding.btn9.setOnClickListener { addDigit("9") }

        // Функція для запам'ятовування обраної математичної операції (+, -, *, /)
        fun setOperation(op: String) {
            val text = binding.tvDisplay.text.toString()
            try {
                firstNumber = text.toDouble()
            } catch (e: Exception) {
                firstNumber = 0.0
            }
            operation = op
            isNewNumber = true
        }

        binding.btnPlus.setOnClickListener { setOperation("+") }
        binding.btnMinus.setOnClickListener { setOperation("-") }
        binding.btnMultiply.setOnClickListener { setOperation("*") }
        binding.btnDivide.setOnClickListener { setOperation("/") }

        // Обробка натискання кнопки дорівнює "="
        binding.btnEquals.setOnClickListener {
            if (operation.isNotEmpty()) {
                val secondNumber = binding.tvDisplay.text.toString().toDoubleOrNull() ?: 0.0

                if (operation == "/" && secondNumber == 0.0) {
                    binding.tvDisplay.text = "Помилка"
                } else {
                    val result = when (operation) {
                        "+" -> firstNumber + secondNumber
                        "-" -> firstNumber - secondNumber
                        "*" -> firstNumber * secondNumber
                        "/" -> firstNumber / secondNumber
                        else -> 0.0
                    }
                    binding.tvDisplay.text = result.toString()
                }

                operation = ""
                isNewNumber = true
            }
        }

        binding.btnClear.setOnClickListener {
            firstNumber = 0.0
            operation = ""
            isNewNumber = true
            binding.tvDisplay.text = "0"
        }
    }
}