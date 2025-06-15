package com.example.buddyapp.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buddyapp.model.Transaction

@Composable
fun BudgetTracker(onBack: () -> Unit) {
    var label by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    var isExpense by remember { mutableStateOf(true) }

    val transactions = remember { mutableStateListOf<Transaction>() }

    val total = transactions.sumOf {
        if (it.isExpense) -it.amount else it.amount
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("💸 Budget Tracker", fontSize = 24.sp, modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(16.dp))

        Text("Total: €${"%.2f".format(total)}", fontSize = 20.sp,
            color = if (total < 0) Color.Red else Color(0xFF388E3C),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = label,
            onValueChange = { label = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = amountInput,
            onValueChange = { amountInput = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isExpense, onCheckedChange = { isExpense = it })
            Text("Is Expense?")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val amount = amountInput.toDoubleOrNull()
                if (label.isNotBlank() && amount != null) {
                    transactions.add(Transaction(label.trim(), amount, isExpense))
                    label = ""
                    amountInput = ""
                    isExpense = true
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(transactions) { txn ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF1F1F1))
                        .padding(12.dp)
                ) {
                    Text(txn.label)
                    Text(
                        text = (if (txn.isExpense) "-€" else "+€") + "%.2f".format(txn.amount),
                        color = if (txn.isExpense) Color.Red else Color(0xFF388E3C)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("⬅ Back")
        }
    }
}
