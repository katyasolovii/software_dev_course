package com.example.private24

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.WorkInfo

@Composable
fun ExchangeRatesScreen(viewModel: ExchangeArchiveViewModel = viewModel()) {
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val rates by viewModel.ratesState.collectAsStateWithLifecycle()
    val workState by viewModel.workInfoState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Архів курсів валют (Room + WorkManager)",
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = selectedDate,
                onValueChange = { viewModel.onDateSelected(it) },
                label = { Text("Дата (dd.MM.yyyy)") },
                modifier = Modifier.weight(1f)
            )

            Button(onClick = { viewModel.refreshRatesForSelectedDate() }) {
                Text("Оновити")
            }
        }

        when (workState) {
            WorkInfo.State.RUNNING, WorkInfo.State.ENQUEUED -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator()
                    Text("Синхронізація у фоні...")
                }
            }
            WorkInfo.State.FAILED -> {
                Text(
                    text = "Помилка оновлення. Показано збережені дані.",
                    color = MaterialTheme.colorScheme.error
                )
            }
            else -> {}
        }

        if (rates.isEmpty()) {
            Text(
                text = "Курси за цю дату ще не завантажені.",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(rates, key = { "${it.date}_${it.currency}" }) { rate ->
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text(
                            text = "${rate.currency} / ${rate.baseCurrency}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Купівля: ${rate.purchaseRate?.let { "$it UAH" } ?: "—"}")
                        Text("Продаж: ${rate.saleRate?.let { "$it UAH" } ?: "—"}")
                    }
                }
            }
        }
    }
}