package com.example.buddyapp.apps

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment

@Composable
fun TipCalculator(onBack: () -> Unit) {
    var amountInput by remember { mutableStateOf("") }
    var tipPercent by remember { mutableStateOf(15f) }
    var peopleCount by remember { mutableStateOf("1") }

    val bill = amountInput.toFloatOrNull() ?: 0f
    val tip = bill * (tipPercent / 100)
    val total = bill + tip
    val people = peopleCount.toIntOrNull()?.coerceAtLeast(1) ?: 1
    val perPerson = total / people

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(" Tip Calculator", fontSize = 24.sp)

        OutlinedTextField(
            value = amountInput,
            onValueChange = { amountInput = it },
            label = { Text("Bill Amount (€)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Tip Percentage: ${tipPercent.toInt()}%")

        Slider(
            value = tipPercent,
            onValueChange = { tipPercent = it },
            valueRange = 0f..30f,
            steps = 5
        )

        OutlinedTextField(
            value = peopleCount,
            onValueChange = {
                if (it.all { ch -> ch.isDigit() }) peopleCount = it
            },
            label = { Text("Number of People") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Divider()

        Text("💶 Tip Amount: €${"%.2f".format(tip)}")
        Text("🧾 Total to Pay: €${"%.2f".format(total)}")
        Text("🧍 Per Person: €${"%.2f".format(perPerson)}")

        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("⬅ Back")
        }
    }
}
