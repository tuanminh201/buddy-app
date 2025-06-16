package com.example.buddyapp.apps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.buddyapp.viewmodel.BudgetTrackerViewModel

@Composable
fun BudgetTracker(
    onBack: () -> Unit,
    viewModel: BudgetTrackerViewModel = viewModel()
) {
    // State variables for input fields and edit state
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var isExpense by remember { mutableStateOf(true) }
    var editingIndex by remember { mutableStateOf<Int?>(null) }

    // Get current list and total from ViewModel
    val items = viewModel.budgetItems
    val totalAmount = viewModel.total()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        //  Title
        Text(
            text = "Budget Tracker",
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Total display with dynamic color (green if positive, red if negative)
        Text(
            text = "Total: €${String.format("%.2f", totalAmount)}",
            fontSize = 20.sp,
            color = if (totalAmount >= 0) Color(0xFF2E7D32) else Color.Red,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        //  Name input
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Item name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Amount input
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount (€)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        //  Switch to choose between expense or income
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Expense")
            Switch(
                checked = !isExpense,
                onCheckedChange = { isExpense = !it }
            )
            Text("Income")
        }

        Spacer(modifier = Modifier.height(8.dp))

        //  Add or ️ Update button
        Button(
            onClick = {
                val value = amount.toDoubleOrNull()
                if (name.isNotBlank() && value != null) {
                    if (editingIndex != null) {
                        viewModel.updateItem(editingIndex!!, name, value, isExpense)
                        editingIndex = null
                    } else {
                        viewModel.addItem(name, value, isExpense)
                    }
                    // Reset input
                    name = ""
                    amount = ""
                    isExpense = true
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(if (editingIndex != null) "Update" else "Add")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // List of budget entries
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(items) { index, item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val amountText = (if (item.isExpense) "-€" else "+€") + item.amount
                    val amountColor = if (item.isExpense) Color.Red else Color(0xFF2E7D32)  // color

                    //  Display item name and value
                    Text(
                        text = "${item.name}: $amountText",
                        fontSize = 16.sp,
                        color = amountColor
                    )

                    // Edit and Delete actions
                    Row {
                        IconButton(onClick = {
                            name = item.name
                            amount = item.amount.toString()
                            isExpense = item.isExpense
                            editingIndex = index
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = { viewModel.removeItem(index) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back button
        Button(
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Back")
        }
    }
}
