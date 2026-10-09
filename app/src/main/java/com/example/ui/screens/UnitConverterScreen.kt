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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConverterScreen(onBack: () -> Unit) {
    var selectedCategory by remember { mutableStateOf(CalculationUtils.UnitCategory.LENGTH) }
    var inputValue by remember { mutableStateOf("1") }

    var lengthFrom by remember { mutableStateOf(CalculationUtils.LengthUnit.METER) }
    var lengthTo by remember { mutableStateOf(CalculationUtils.LengthUnit.CENTIMETER) }

    var weightFrom by remember { mutableStateOf(CalculationUtils.WeightUnit.KILOGRAM) }
    var weightTo by remember { mutableStateOf(CalculationUtils.WeightUnit.GRAM) }

    var tempFrom by remember { mutableStateOf(CalculationUtils.TemperatureUnit.CELSIUS) }
    var tempTo by remember { mutableStateOf(CalculationUtils.TemperatureUnit.FAHRENHEIT) }

    Scaffold(
        topBar = {
            ToolTopBar(
                title = "Unit Converter",
                onBack = onBack,
                onReset = { inputValue = "1" }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(selectedTabIndex = selectedCategory.ordinal) {
                CalculationUtils.UnitCategory.entries.forEach { cat ->
                    Tab(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        text = { Text(cat.displayName) }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NumberInputField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = "Value to Convert",
                    testTag = "converter_input"
                )

                val valNum = inputValue.toDoubleOrNull()

                when (selectedCategory) {
                    CalculationUtils.UnitCategory.LENGTH -> {
                        Text("From", style = MaterialTheme.typography.labelLarge)
                        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                            CalculationUtils.LengthUnit.entries.take(4).forEachIndexed { index, unit ->
                                SegmentedButton(
                                    selected = lengthFrom == unit,
                                    onClick = { lengthFrom = unit },
                                    shape = SegmentedButtonDefaults.itemShape(index = index, count = 4)
                                ) { Text(unit.name) }
                            }
                        }

                        Text("To", style = MaterialTheme.typography.labelLarge)
                        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                            CalculationUtils.LengthUnit.entries.take(4).forEachIndexed { index, unit ->
                                SegmentedButton(
                                    selected = lengthTo == unit,
                                    onClick = { lengthTo = unit },
                                    shape = SegmentedButtonDefaults.itemShape(index = index, count = 4)
                                ) { Text(unit.name) }
                            }
                        }

                        if (valNum != null) {
                            val res = CalculationUtils.convertLength(valNum, lengthFrom, lengthTo)
                            val formatted = CalculationUtils.formatNumber(res)
                            ResultCard(
                                title = "Converted Result",
                                mainResult = "$formatted ${lengthTo.displayName}",
                                copyText = "$formatted ${lengthTo.displayName}"
                            )
                        }
                    }
                    CalculationUtils.UnitCategory.WEIGHT -> {
                        Text("From", style = MaterialTheme.typography.labelLarge)
                        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                            CalculationUtils.WeightUnit.entries.take(4).forEachIndexed { index, unit ->
                                SegmentedButton(
                                    selected = weightFrom == unit,
                                    onClick = { weightFrom = unit },
                                    shape = SegmentedButtonDefaults.itemShape(index = index, count = 4)
                                ) { Text(unit.name) }
                            }
                        }

                        Text("To", style = MaterialTheme.typography.labelLarge)
                        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                            CalculationUtils.WeightUnit.entries.take(4).forEachIndexed { index, unit ->
                                SegmentedButton(
                                    selected = weightTo == unit,
                                    onClick = { weightTo = unit },
                                    shape = SegmentedButtonDefaults.itemShape(index = index, count = 4)
                                ) { Text(unit.name) }
                            }
                        }

                        if (valNum != null) {
                            val res = CalculationUtils.convertWeight(valNum, weightFrom, weightTo)
                            val formatted = CalculationUtils.formatNumber(res)
                            ResultCard(
                                title = "Converted Result",
                                mainResult = "$formatted ${weightTo.displayName}",
                                copyText = "$formatted ${weightTo.displayName}"
                            )
                        }
                    }
                    CalculationUtils.UnitCategory.TEMPERATURE -> {
                        Text("From", style = MaterialTheme.typography.labelLarge)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CalculationUtils.TemperatureUnit.entries.forEach { unit ->
                                FilterChip(
                                    selected = tempFrom == unit,
                                    onClick = { tempFrom = unit },
                                    label = { Text(unit.displayName) }
                                )
                            }
                        }

                        Text("To", style = MaterialTheme.typography.labelLarge)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CalculationUtils.TemperatureUnit.entries.forEach { unit ->
                                FilterChip(
                                    selected = tempTo == unit,
                                    onClick = { tempTo = unit },
                                    label = { Text(unit.displayName) }
                                )
                            }
                        }

                        if (valNum != null) {
                            val res = CalculationUtils.convertTemperature(valNum, tempFrom, tempTo)
                            val formatted = CalculationUtils.formatNumber(res)
                            ResultCard(
                                title = "Converted Result",
                                mainResult = "$formatted ${tempTo.displayName}",
                                copyText = "$formatted ${tempTo.displayName}"
                            )
                        }
                    }
                }
            }
        }
    }
}
