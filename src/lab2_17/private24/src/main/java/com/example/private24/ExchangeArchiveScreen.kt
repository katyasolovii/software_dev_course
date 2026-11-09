package com.example.private24

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Calendar

@Composable
fun ExchangeArchiveScreen(
    viewModel: ExchangeArchiveViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Налаштування DatePickerDialog
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val formattedDate = String.format("%02d.%02d.%d", dayOfMonth, month + 1, year)
            viewModel.onDateChanged(formattedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        // Забороняємо вибір майбутніх дат (архів діє до сьогоднішнього дня)
        datePicker.maxDate = System.currentTimeMillis()
    }

    val isLoading = uiState is ExchangeArchiveUiState.Loading

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "ПриватБанк — Архів курсів",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        // Блок вводу та вибору дати
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = selectedDate,
                onValueChange = { viewModel.onDateChanged(it) },
                label = { Text("Дата (ДД.ММ.РРРР)") },
                singleLine = true,
                enabled = !isLoading,
                modifier = Modifier.weight(1f)
            )

            OutlinedButton(
                onClick = { datePickerDialog.show() },
                enabled = !isLoading
            ) {
                Text("Календар 📅")
            }
        }

        // Кнопка завантаження
        Button(
            onClick = { viewModel.fetchArchiveRates() },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) "Завантаження..." else "Показати курси")
        }

        // Відображення стану екрана
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is ExchangeArchiveUiState.Initial -> {
                    Text(
                        text = "Оберіть дату та натисніть «Показати курси»",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                is ExchangeArchiveUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is ExchangeArchiveUiState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Button(onClick = { viewModel.fetchArchiveRates() }) {
                            Text("Повторити запит")
                        }
                    }
                }

                is ExchangeArchiveUiState.Success -> {
                    if (state.rates.isEmpty()) {
                        Text(
                            text = "На дату ${state.requestedDate} дані про курси відсутні.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "Курси валют на дату: ${state.requestedDate}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(state.rates) { item ->
                                    CurrencyRateCard(item = item)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CurrencyRateCard(item: ExchangeRateItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            val currencyCode = item.currency ?: "—"
            val baseCurrencyCode = item.baseCurrency ?: "UAH"

            Text(
                text = "$currencyCode / $baseCurrencyCode",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Форматування курсів ПриватБанку (якщо null показуємо «—»)
            val buyRateText = item.purchaseRate?.let { String.format("%.2f", it) } ?: "—"
            val saleRateText = item.saleRate?.let { String.format("%.2f", it) } ?: "—"

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Банк купує: $buyRateText $baseCurrencyCode")
                Text(text = "Банк продає: $saleRateText $baseCurrencyCode")
            }

            // Додатково курс НБУ (якщо є)
            if (item.purchaseRateNB != null || item.saleRateNB != null) {
                val nbRateText = item.purchaseRateNB?.let { String.format("%.2f", it) } ?: "—"
                Text(
                    text = "Курс НБУ: $nbRateText $baseCurrencyCode",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}