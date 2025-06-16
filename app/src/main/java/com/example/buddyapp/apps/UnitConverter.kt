package com.example.buddyapp.apps

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UnitConverter(onBack: () -> Unit) {
    val categories = listOf("Length", "Weight", "Temperature", "Currency")
    val unitMap = mapOf(
        "Length" to listOf("Meters", "Kilometers"),
        "Weight" to listOf("Kilograms", "Grams"),
        "Temperature" to listOf("Celsius", "Fahrenheit"),
        "Currency" to listOf("Euros", "Dollars")
    )

    var selectedCategory by remember { mutableStateOf(categories[0]) }
    val units = unitMap[selectedCategory] ?: emptyList()
    var fromUnit by remember { mutableStateOf(units.first()) }
    var toUnit by remember { mutableStateOf(units.last()) }
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("🔄 Unit Converter", fontSize = 24.sp)

        // Dropdown category
        Text("Category:")
        DropdownSelector(
            options = categories,
            selectedOption = selectedCategory,
            onOptionSelected = {
                selectedCategory = it
                fromUnit = unitMap[it]?.firstOrNull() ?: ""
                toUnit = unitMap[it]?.lastOrNull() ?: ""
                result = null
            }
        )

        Text("From:")
        DropdownSelector(
            options = units,
            selectedOption = fromUnit,
            onOptionSelected = { fromUnit = it }
        )

        Text("To:")
        DropdownSelector(
            options = units,
            selectedOption = toUnit,
            onOptionSelected = { toUnit = it }
        )

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Enter value") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            val inputValue = input.toDoubleOrNull()
            result = if (inputValue != null) {
                when (selectedCategory) {
                    "Length" -> convertLength(inputValue, fromUnit, toUnit)
                    "Weight" -> convertWeight(inputValue, fromUnit, toUnit)
                    "Temperature" -> convertTemperature(inputValue, fromUnit, toUnit)
                    "Currency" -> convertCurrency(inputValue, fromUnit, toUnit)
                    else -> "N/A"
                }
            } else {
                "Invalid input"
            }
        }) {
            Text("Convert")
        }

        result?.let {
            Text("Result: $it", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("⬅ Back")
        }
    }
}

@Composable
fun DropdownSelector(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selectedOption)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun convertLength(value: Double, from: String, to: String): String {
    val meters = when (from) {
        "Kilometers" -> value * 1000
        "Meters" -> value
        else -> value
    }
    val result = when (to) {
        "Kilometers" -> meters / 1000
        "Meters" -> meters
        else -> meters
    }
    return "$result $to"
}

fun convertWeight(value: Double, from: String, to: String): String {
    val grams = when (from) {
        "Kilograms" -> value * 1000
        "Grams" -> value
        else -> value
    }
    val result = when (to) {
        "Kilograms" -> grams / 1000
        "Grams" -> grams
        else -> grams
    }
    return "$result $to"
}

fun convertTemperature(value: Double, from: String, to: String): String {
    val result = when {
        from == "Celsius" && to == "Fahrenheit" -> value * 9 / 5 + 32
        from == "Fahrenheit" && to == "Celsius" -> (value - 32) * 5 / 9
        else -> value
    }
    return "$result $to"
}

fun convertCurrency(value: Double, from: String, to: String): String {
    val rate = 1.15
    val result = when {
        from == "Euros" && to == "Dollars" -> value * rate
        from == "Dollars" && to == "Euros" -> value / rate
        else -> value
    }
    return String.format("%.2f %s", result, to)
}