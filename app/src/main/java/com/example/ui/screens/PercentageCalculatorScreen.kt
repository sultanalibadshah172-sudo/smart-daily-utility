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

@Composable
fun PercentageCalculatorScreen(onBack: () -> Unit) {
    var tabIndex by remember { mutableIntStateOf(0) }
    
    var pVal by remember { mutableStateOf("") }
    var totalVal by remember { mutableStateOf("") }
    
    var part1 by remember { mutableStateOf("") }
    var total1 by remember { mutableStateOf("") }

    var oldVal by remember { mutableStateOf("") }
    var newVal by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            ToolTopBar(
                title = "Percentage Calculator",
                onBack = onBack,
                onReset = {
                    pVal = ""; totalVal = ""; part1 = ""; total1 = ""; oldVal = ""; newVal = ""
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(selectedTabIndex = tabIndex) {
                Tab(selected = tabIndex == 0, onClick = { tabIndex = 0 }, text = { Text("% of Value") })
                Tab(selected = tabIndex == 1, onClick = { tabIndex = 1 }, text = { Text("What %") })
                Tab(selected = tabIndex == 2, onClick = { tabIndex = 2 }, text = { Text("% Change") })
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (tabIndex) {
                    0 -> {
                        NumberInputField(value = pVal, onValueChange = { pVal = it }, label = "Percentage (%)", suffix = "%", testTag = "pct_val")
                        NumberInputField(value = totalVal, onValueChange = { totalVal = it }, label = "Total Amount", testTag = "pct_total")

                        val p = pVal.toDoubleOrNull()
                        val t = totalVal.toDoubleOrNull()
                        if (p != null && t != null) {
                            val res = CalculationUtils.calculatePercentageOf(p, t)
                            ResultCard(
                                title = "Result",
                                mainResult = res.formatted,
                                subtitle = res.explanation,
                                copyText = res.formatted
                            )
                        }
                    }
                    1 -> {
                        NumberInputField(value = part1, onValueChange = { part1 = it }, label = "Part Value (X)", testTag = "part_val")
                        NumberInputField(value = total1, onValueChange = { total1 = it }, label = "Total Value (Y)", testTag = "part_total")

                        val x = part1.toDoubleOrNull()
                        val y = total1.toDoubleOrNull()
                        if (x != null && y != null) {
                            CalculationUtils.calculateWhatPercentage(x, y).fold(
                                onSuccess = { res ->
                                    ResultCard(
                                        title = "Percentage",
                                        mainResult = res.formatted,
                                        subtitle = res.explanation,
                                        copyText = res.formatted
                                    )
                                },
                                onFailure = { err ->
                                    Text(text = err.message ?: "Invalid Input", color = MaterialTheme.colorScheme.error)
                                }
                            )
                        }
                    }
                    2 -> {
                        NumberInputField(value = oldVal, onValueChange = { oldVal = it }, label = "Original Value", testTag = "old_val")
                        NumberInputField(value = newVal, onValueChange = { newVal = it }, label = "New Value", testTag = "new_val")

                        val o = oldVal.toDoubleOrNull()
                        val n = newVal.toDoubleOrNull()
                        if (o != null && n != null) {
                            CalculationUtils.calculatePercentageChange(o, n).fold(
                                onSuccess = { res ->
                                    ResultCard(
                                        title = "Percentage Change",
                                        mainResult = res.formatted,
                                        subtitle = res.explanation,
                                        copyText = res.formatted
                                    )
                                },
                                onFailure = { err ->
                                    Text(text = err.message ?: "Invalid Input", color = MaterialTheme.colorScheme.error)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
