package com.example.buddyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import com.example.buddyapp.ui.theme.BuddyAppTheme
import com.example.buddyapp.apps.HomeScreen
import com.example.buddyapp.apps.ShoppingList
import com.example.buddyapp.apps.BudgetTracker
import com.example.buddyapp.apps.UnitConverter
import com.example.buddyapp.apps.TipCalculator
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BuddyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BuddyApp()
                }
            }
        }
    }
}

@Composable
fun BuddyApp() {
    var currentScreen by remember { mutableStateOf("home") }

    when (currentScreen) {
        "home" -> HomeScreen(onNavigate = { currentScreen = it })
        "shopping" -> ShoppingList(onBack = { currentScreen = "home" })
        "budget" -> BudgetTracker(onBack = { currentScreen = "home" })
        "converter" -> UnitConverter(onBack = { currentScreen = "home" })
        "tip" -> TipCalculator (onBack = {currentScreen= "home"})
    }
}
