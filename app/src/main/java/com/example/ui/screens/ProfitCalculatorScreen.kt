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
import com.example.ui.theme.LossRed
import com.example.ui.theme.SuccessGreen

@Composable
fun ProfitCalculatorScreen(onBack: () -> Unit) {
    var tabIndex by remember { mutableIntStateOf(0) }

    var costInput by remember { mutableStateOf("") }
    var sellingInput by remember { mutableStateOf("") }

    var costForTarget by remember { mutableStateOf("") }
    var targetProfitPercent by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            ToolTopBar(
                title = "Profit Calculator",
                onBack = onBack,
                onReset = {
                    costInput = ""; sellingInput = ""; costForTarget = ""; targetProfitPercent = ""
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
                Tab(selected = tabIndex == 0, onClick = { tabIndex = 0 }, text = { Text("Profit & Loss") })
                Tab(selected = tabIndex == 1, onClick = { tabIndex = 1 }, text = { Text("Target Price") })
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (tabIndex == 0) {
                    NumberInputField(
                        value = costInput,
                        onValueChange = { costInput = it },
                        label = "Cost Price",
                        prefix = "$",
                        testTag = "profit_cost_input"
                    )
                    NumberInputField(
                        value = sellingInput,
                        onValueChange = { sellingInput = it },
                        label = "Selling Price",
                        prefix = "$",
                        testTag = "profit_selling_input"
                    )

                    val cost = costInput.toDoubleOrNull()
                    val selling = sellingInput.toDoubleOrNull()

                    if (cost != null && selling != null) {
                        CalculationUtils.calculateProfitOrLoss(cost, selling).fold(
                            onSuccess = { res ->
                                val statusText = when {
                                    res.isBreakEven -> "Break Even"
                                    res.isProfit -> "Profit: $${res.formattedDifference}"
                                    else -> "Loss: $${res.formattedDifference}"
                                }
                                val statusColor = when {
                                    res.isBreakEven -> MaterialTheme.colorScheme.onSurface
                                    res.isProfit -> SuccessGreen
                                    else -> LossRed
                                }

                                ResultCard(
                                    title = if (res.isProfit) "Total Profit" else if (res.isBreakEven) "Break Even" else "Total Loss",
                                    mainResult = statusText,
                                    subtitle = "Percentage: ${res.formattedPercentage}",
                                    accentColor = statusColor,
                                    copyText = statusText,
                                    extraContent = {
                                        KeyValueRow(label = "Gross Margin", value = res.formattedMargin)
                                        KeyValueRow(label = "Markup", value = res.formattedMarkup)
                                    }
                                )
                            },
                            onFailure = { err ->
                                Text(text = err.message ?: "Invalid calculation", color = MaterialTheme.colorScheme.error)
                            }
                        )
                    }
                } else {
                    NumberInputField(
                        value = costForTarget,
                        onValueChange = { costForTarget = it },
                        label = "Cost Price",
                        prefix = "$",
                        testTag = "target_cost_input"
                    )
                    NumberInputField(
                        value = targetProfitPercent,
                        onValueChange = { targetProfitPercent = it },
                        label = "Desired Profit (%)",
                        suffix = "%",
                        testTag = "target_profit_percent"
                    )

                    val cost = costForTarget.toDoubleOrNull()
                    val desired = targetProfitPercent.toDoubleOrNull()

                    if (cost != null && desired != null) {
                        CalculationUtils.calculateTargetSellingPrice(cost, desired).fold(
                            onSuccess = { res ->
                                ResultCard(
                                    title = "Target Selling Price",
                                    mainResult = "$${res.formattedSellingPrice}",
                                    subtitle = "Profit Amount: $${res.formattedProfitAmount}",
                                    copyText = "$${res.formattedSellingPrice}"
                                )
                            },
                            onFailure = { err ->
                                Text(text = err.message ?: "Invalid calculation", color = MaterialTheme.colorScheme.error)
                            }
                        )
                    }
                }
            }
        }
    }
}
