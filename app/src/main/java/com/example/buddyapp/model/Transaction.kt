package com.example.buddyapp.model

data class Transaction(
    val label: String,
    val amount: Double,
    val isExpense: Boolean
)
