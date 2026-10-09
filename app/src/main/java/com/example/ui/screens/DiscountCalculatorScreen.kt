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
fun DiscountCalculatorScreen(onBack: () -> Unit) {
    var priceInput by remember { mutableStateOf("") }
    var discountInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            ToolTopBar(
                title = "Discount Calculator",
                onBack = onBack,
                onReset = { priceInput = ""; discountInput = "" }
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
            NumberInputField(
                value = priceInput,
                onValueChange = { priceInput = it },
                label = "Original Price",
                prefix = "$",
                testTag = "discount_price_input"
            )

            NumberInputField(
                value = discountInput,
                onValueChange = { discountInput = it },
                label = "Discount (%)",
                suffix = "%",
                testTag = "discount_percent_input"
            )

            val price = priceInput.toDoubleOrNull()
            val discount = discountInput.toDoubleOrNull()

            if (price != null && discount != null) {
                CalculationUtils.calculateDiscount(price, discount).fold(
                    onSuccess = { res ->
                        ResultCard(
                            title = "Final Price",
                            mainResult = "$${res.formattedFinalPrice}",
                            subtitle = "Discount Amount: $${res.formattedDiscountAmount}",
                            copyText = "$${res.formattedFinalPrice}",
                            extraContent = {
                                KeyValueRow(label = "Original Price", value = "$${CalculationUtils.formatNumber(res.originalPrice)}")
                                KeyValueRow(label = "Discount", value = "${CalculationUtils.formatNumber(res.discountPercentage)}%")
                                KeyValueRow(label = "You Save", value = "$${res.formattedSaved}", valueColor = MaterialTheme.colorScheme.primary)
                            }
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
