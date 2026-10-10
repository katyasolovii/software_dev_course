package com.example.task2_9

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.task2_9.databinding.ActivityMainBinding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "CoroutineLab"
    private var calculationJob: Job? = null

    val ceh = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "Глобальне перехоплення крашу: ${throwable.message}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            calculationJob = lifecycleScope.launch(Dispatchers.Default) {
                try {
                    for (i in 1..100) {
                        Thread.sleep(50)
                        ensureActive()
                        Log.d(TAG, "Обробка: $i%")
                    }
                    Log.d(TAG, "Роботу успішно завершено!")
                } catch (e: CancellationException) {
                    Log.d(TAG, "Обробку скасовано користувачем!")
                    throw e
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            calculationJob?.cancel()
        }
//        Блок 2
//        binding.btnTestCrash.setOnClickListener {
//            // Батьківська корутина
//            lifecycleScope.launch {
//                // Перша дочірня корутина
//                launch {
//                    delay(1000)
//                    throw RuntimeException("Банер не завантажився!")
//                }
//                // Друга дочірня корутина
//                launch {
//                    delay(2000)
//                    Log.d("CoroutineLab", "Стрічка новин завантажена")
//                }
//            }
//        }

        binding.btnTestCrash.setOnClickListener {
            lifecycleScope.launch {
                supervisorScope {
                    launch {
                        delay(1000)
                        try {
                            throw RuntimeException("Банер не завантажився!")
                        } catch (e: Exception) {
                            Log.e("CoroutineLab", "Помилка завантаження банера: ${e.message}")
                        }
                    }
                    launch {
                        delay(2000)
                        // Через supervisorScope ця корутина НЕ скасовується
                        Log.d("CoroutineLab", "Стрічка новин завантажена")
                    }
                }
            }
        }

        binding.btnGlobalCrash.setOnClickListener {
            val safeScope = CoroutineScope(SupervisorJob() + Dispatchers.Main + ceh)

            // Запуск корутину без try-catch
            safeScope.launch {
                delay(500)
                throw IllegalArgumentException("Ой!")
            }
        }

    }

}