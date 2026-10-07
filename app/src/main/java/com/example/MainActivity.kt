package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.screens.*
import com.example.ui.theme.SmartDailyUtilityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartDailyUtilityTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SmartDailyUtilityApp()
                }
            }
        }
    }
}

@Composable
fun SmartDailyUtilityApp() {
    var currentScreen by remember { mutableStateOf(ToolScreen.HOME) }

    BackHandler(enabled = currentScreen != ToolScreen.HOME) {
        currentScreen = ToolScreen.HOME
    }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "screen_transition"
    ) { screen ->
        when (screen) {
            ToolScreen.HOME -> HomeScreen(onNavigateToTool = { currentScreen = it })
            ToolScreen.PERCENTAGE -> PercentageCalculatorScreen(onBack = { currentScreen = ToolScreen.HOME })
            ToolScreen.DISCOUNT -> DiscountCalculatorScreen(onBack = { currentScreen = ToolScreen.HOME })
            ToolScreen.PROFIT -> ProfitCalculatorScreen(onBack = { currentScreen = ToolScreen.HOME })
            ToolScreen.AGE -> AgeCalculatorScreen(onBack = { currentScreen = ToolScreen.HOME })
            ToolScreen.UNIT_CONVERTER -> UnitConverterScreen(onBack = { currentScreen = ToolScreen.HOME })
        }
    }
}
