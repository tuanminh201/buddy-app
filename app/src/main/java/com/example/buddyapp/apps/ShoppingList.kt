package com.example.buddyapp.apps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.buddyapp.viewmodel.ShoppingListViewModel

@Composable
fun ShoppingList(
    onBack: () -> Unit,
    viewModel: ShoppingListViewModel = viewModel()
) {
    // Text field input state
    var itemName by remember { mutableStateOf("") }
    var itemQty by remember { mutableStateOf("1") }

    // Editing index
    var editingIndex by remember { mutableStateOf<Int?>(null) }

    // Shopping list from ViewModel
    val shoppingList = viewModel.shoppingList

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Title
        Text(
            text = "Shopping List",
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input section: name and quantity
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = itemName,
                onValueChange = { itemName = it },
                label = { Text("Item name") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                modifier = Modifier.weight(2f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = itemQty,
                onValueChange = { itemQty = it },
                label = { Text("Qty") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Add or Update button
        Button(
            onClick = {
                val qty = itemQty.toIntOrNull() ?: 1
                if (itemName.isNotBlank()) {
                    if (editingIndex != null) {
                        viewModel.updateItem(editingIndex!!, itemName, qty)
                        editingIndex = null
                    } else {
                        viewModel.addItem(itemName, qty)
                    }
                    itemName = ""
                    itemQty = "1"
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(if (editingIndex != null) "Update Item" else "Add Item")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Empty list message
        if (shoppingList.isEmpty()) {
            Text(
                text = "No items yet",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            // List of items
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(shoppingList) { index, item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // ✅ Checkbox for item
                            Checkbox(
                                checked = item.isChecked,
                                onCheckedChange = {
                                    viewModel.toggleItem(index, it)
                                }
                            )
                            // 📄 Item text with optional strikethrough
                            Text(
                                "${item.name} x${item.quantity}",
                                style = if (item.isChecked)
                                    MaterialTheme.typography.bodyLarge.copy(
                                        textDecoration = TextDecoration.LineThrough,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                else MaterialTheme.typography.bodyLarge
                            )
                        }
                        // Edit and  Delete buttons
                        Row {
                            IconButton(onClick = {
                                itemName = item.name
                                itemQty = item.quantity.toString()
                                editingIndex = index
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit")
                            }
                            IconButton(onClick = {
                                viewModel.removeItem(index)
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //  Back button
        Button(
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Back")
        }
    }
}
