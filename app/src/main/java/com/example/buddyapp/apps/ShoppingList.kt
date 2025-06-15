package com.example.buddyapp.apps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buddyapp.model.ShoppingItem

@Composable
fun ShoppingList(onBack: () -> Unit) {
    var itemName by remember { mutableStateOf("") }
    var itemQty by remember { mutableStateOf("1") }
    val shoppingList = remember { mutableStateListOf<ShoppingItem>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "🛒 Shopping List",
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = itemName,
                onValueChange = { itemName = it },
                label = { Text("Item name") },
                singleLine = true,
                modifier = Modifier.weight(2f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = itemQty,
                onValueChange = {
                    if (it.all { ch -> ch.isDigit() }) itemQty = it
                },
                label = { Text("Qty") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                val qty = itemQty.toIntOrNull() ?: 1
                if (itemName.isNotBlank() && qty > 0) {
                    shoppingList.add(ShoppingItem(itemName.trim(), qty))
                    itemName = ""
                    itemQty = "1"
                }
            }) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (shoppingList.isEmpty()) {
            Text("No items yet 🥲", modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(shoppingList) { index, item ->
                    var isEditing by remember { mutableStateOf(false) }
                    var editName by remember { mutableStateOf(item.name) }
                    var editQty by remember { mutableStateOf(item.quantity.toString()) }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = item.isChecked,
                                onCheckedChange = {
                                    shoppingList[index] = item.copy(isChecked = it)
                                }
                            )

                            if (isEditing) {
                                Column {
                                    OutlinedTextField(
                                        value = editName,
                                        onValueChange = { editName = it },
                                        label = { Text("Name") },
                                        singleLine = true,
                                        modifier = Modifier.width(150.dp)
                                    )
                                    OutlinedTextField(
                                        value = editQty,
                                        onValueChange = {
                                            if (it.all { ch -> ch.isDigit() }) editQty = it
                                        },
                                        label = { Text("Qty") },
                                        singleLine = true,
                                        modifier = Modifier.width(80.dp),
                                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                                    )
                                }
                            } else {
                                Text(
                                    text = "${item.name} (x${item.quantity})",
                                    style = if (item.isChecked)
                                        MaterialTheme.typography.bodyLarge.copy(
                                            textDecoration = TextDecoration.LineThrough,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    else MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        Row {
                            if (isEditing) {
                                IconButton(onClick = {
                                    val qty = editQty.toIntOrNull() ?: item.quantity
                                    if (editName.isNotBlank() && qty > 0) {
                                        shoppingList[index] = item.copy(name = editName.trim(), quantity = qty)
                                        isEditing = false
                                    }
                                }) {
                                    Icon(Icons.Default.Check, contentDescription = "Save")
                                }
                            } else {
                                IconButton(onClick = { isEditing = true }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                                }
                            }

                            IconButton(onClick = { shoppingList.removeAt(index) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("⬅ Back")
        }
    }
}
