package ru.topbun.mushrooms.presentation

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import cafe.adriel.voyager.navigator.Navigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ru.topbun.mushrooms.data.local.AppDatabase
import ru.topbun.mushrooms.presentation.screens.tabs.TabsScreen
import ru.topbun.mushrooms.presentation.theme.Colors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@Composable
private fun App() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(Colors.BG_GREEN, false)
    Box(
        modifier = Modifier.fillMaxSize().background(Colors.BG_GREEN).systemBarsPadding()
    ){
        Navigator(TabsScreen)
    }
}
