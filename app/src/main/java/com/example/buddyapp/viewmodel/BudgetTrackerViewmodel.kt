package com.example.buddyapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.buddyapp.model.BudgetItem

class BudgetTrackerViewModel : ViewModel() {
    val budgetItems = mutableStateListOf<BudgetItem>()

    fun addItem(name: String, amount: Double, isExpense: Boolean) {
        budgetItems.add(BudgetItem(name, amount, isExpense))
    }

    fun removeItem(index: Int) {
        budgetItems.removeAt(index)
    }

    fun updateItem(index: Int, name: String, amount: Double, isExpense: Boolean) {
        budgetItems[index] = BudgetItem(name, amount, isExpense)
    }

    fun total(): Double {
        return budgetItems.sumOf { if (it.isExpense) -it.amount else it.amount }
    }
}
