package com.example.dice

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var imageViews: Array<ImageView>

    // Екземпляр DiceViewModel через делегат by viewModels()
    private val viewModel: DiceViewModel by viewModels()

    private val drawables = arrayOf(  // drawable for the dice
        R.drawable.die_1, R.drawable.die_2,
        R.drawable.die_3, R.drawable.die_4,
        R.drawable.die_5, R.drawable.die_6
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding for view object references
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageViews = arrayOf(   // Views: dice
            binding.die1,
            binding.die2,
            binding.die3,
            binding.die4,
            binding.die5
        )

        binding.rollButton.setOnClickListener {
            rollTheDice()
        }

        // Коли кості крутяться (isRolling == true), кнопка стає неактивною
        viewModel.isRolling.observe(this) { isRolling ->
            binding.rollButton.isEnabled = !isRolling
        }

        viewModel.diceValues.observe(this) { values ->
            values.forEachIndexed { index, value ->
                if (index < imageViews.size) {
                    // індекси масиву 0-5, а кубики 1-6
                    imageViews[index].setImageResource(drawables[value - 1])
                }
            }
        }
    }

    private fun rollTheDice() {
        viewModel.rollDice()
    }
}