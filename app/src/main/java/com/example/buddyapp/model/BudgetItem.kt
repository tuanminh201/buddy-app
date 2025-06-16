package com.example.buddyapp.model

data class BudgetItem(
    val name: String,
    val amount: Double,
    val isExpense: Boolean = false
)
