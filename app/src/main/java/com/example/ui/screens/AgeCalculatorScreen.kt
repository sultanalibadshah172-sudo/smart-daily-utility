package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.model.CalculationUtils
import com.example.ui.common.*
import java.time.LocalDate

@Composable
fun AgeCalculatorScreen(onBack: () -> Unit) {
    var birthYear by remember { mutableStateOf("2000") }
    var birthMonth by remember { mutableStateOf("1") }
    var birthDay by remember { mutableStateOf("1") }

    var targetYear by remember { mutableStateOf(LocalDate.now().year.toString()) }
    var targetMonth by remember { mutableStateOf(LocalDate.now().monthValue.toString()) }
    var targetDay by remember { mutableStateOf(LocalDate.now().dayOfMonth.toString()) }

    Scaffold(
        topBar = {
            ToolTopBar(
                title = "Age Calculator",
                onBack = onBack,
                onReset = {
                    birthYear = "2000"; birthMonth = "1"; birthDay = "1"
                    targetYear = LocalDate.now().year.toString()
                    targetMonth = LocalDate.now().monthValue.toString()
                    targetDay = LocalDate.now().dayOfMonth.toString()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Date of Birth", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NumberInputField(value = birthDay, onValueChange = { birthDay = it }, label = "Day", modifier = Modifier.weight(1f), testTag = "dob_day")
                NumberInputField(value = birthMonth, onValueChange = { birthMonth = it }, label = "Month", modifier = Modifier.weight(1f), testTag = "dob_month")
                NumberInputField(value = birthYear, onValueChange = { birthYear = it }, label = "Year", modifier = Modifier.weight(1.5f), testTag = "dob_year")
            }

            Text("Target Date", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NumberInputField(value = targetDay, onValueChange = { targetDay = it }, label = "Day", modifier = Modifier.weight(1f), testTag = "target_day")
                NumberInputField(value = targetMonth, onValueChange = { targetMonth = it }, label = "Month", modifier = Modifier.weight(1f), testTag = "target_month")
                NumberInputField(value = targetYear, onValueChange = { targetYear = it }, label = "Year", modifier = Modifier.weight(1.5f), testTag = "target_year")
            }

            val bY = birthYear.toIntOrNull()
            val bM = birthMonth.toIntOrNull()
            val bD = birthDay.toIntOrNull()

            val tY = targetYear.toIntOrNull()
            val tM = targetMonth.toIntOrNull()
            val tD = targetDay.toIntOrNull()

            if (bY != null && bM != null && bD != null && tY != null && tM != null && tD != null) {
                runCatching {
                    val dob = LocalDate.of(bY, bM, bD)
                    val target = LocalDate.of(tY, tM, tD)
                    CalculationUtils.calculateAge(dob, target)
                }.getOrNull()?.fold(
                    onSuccess = { res ->
                        ResultCard(
                            title = "Age Breakdown",
                            mainResult = "${res.years} Years, ${res.months} Months, ${res.days} Days",
                            copyText = "${res.years} Years, ${res.months} Months, ${res.days} Days",
                            extraContent = {
                                KeyValueRow(label = "Total Months", value = "${res.totalMonths} months")
                                KeyValueRow(label = "Total Days", value = "${res.totalDays} days")
                                res.nextBirthdayDays?.let {
                                    KeyValueRow(label = "Next Birthday In", value = "$it days", valueColor = MaterialTheme.colorScheme.primary)
                                }
                            }
                        )
                    },
                    onFailure = { err ->
                        Text(text = err.message ?: "Invalid dates", color = MaterialTheme.colorScheme.error)
                    }
                )
            }
        }
    }
}
