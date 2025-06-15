package com.example.buddyapp.model

data class ShoppingItem(
    val name: String,
    val quantity: Int = 1,
    val isChecked: Boolean = false
)
