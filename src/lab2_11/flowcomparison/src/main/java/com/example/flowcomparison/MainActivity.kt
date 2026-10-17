package com.example.flowcomparison

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.flowcomparison.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.math.BigInteger

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<CalculationViewModel>()
    private var fibonacciJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel  // create viewModel to start flow

        binding.btnSubscribe.setOnClickListener {
            if (fibonacciJob != null) {
                Toast.makeText(this, "Already subscribed", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            fibonacciJob = lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.fib.collect { elem: BigInteger ->
                        Log.d("EEEE", "Activity : $elem")
                    }
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            fibonacciJob?.cancel()
            fibonacciJob = null
        }





    }

}