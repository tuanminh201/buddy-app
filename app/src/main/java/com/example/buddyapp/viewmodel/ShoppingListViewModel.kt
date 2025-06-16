package com.example.buddyapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.buddyapp.model.ShoppingItem

class ShoppingListViewModel : ViewModel() {
    val shoppingList = mutableStateListOf<ShoppingItem>()

    fun addItem(name: String, quantity: Int) {
        shoppingList.add(ShoppingItem(name.trim(), quantity))
    }

    fun removeItem(index: Int) {
        shoppingList.removeAt(index)
    }

    fun updateItem(index: Int, name: String, quantity: Int) {
        shoppingList[index] = shoppingList[index].copy(name = name.trim(), quantity = quantity)
    }

    fun toggleItem(index: Int, checked: Boolean) {
        shoppingList[index] = shoppingList[index].copy(isChecked = checked)
    }
}
