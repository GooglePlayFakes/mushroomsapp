package ru.topbun.mushrooms.presentation.screens.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import ru.topbun.mushrooms.presentation.screens.category.CategoryScreen
import ru.topbun.mushrooms.presentation.screens.favorite.FavoriteScreen
import ru.topbun.mushrooms.presentation.screens.main.MainScreen
import ru.topbun.mushrooms.presentation.theme.Colors

object TabsScreen : Screen {

    @Composable
    override fun Content() {
        TabNavigator(tab = MainScreen) {
            Scaffold(
                modifier = Modifier.Companion
                    .background(Colors.BG_GREEN)
                    .navigationBarsPadding(),
                content = {
                    Box(Modifier.Companion
                        .fillMaxSize()
                        .padding(it)) {
                        CurrentTab()
                    }
                },
                bottomBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Colors.BG_GREEN)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Companion.CenterVertically
                    ) {
                        BottomNavigationItem(MainScreen)
                        BottomNavigationItem(CategoryScreen)
                        BottomNavigationItem(FavoriteScreen)
                    }
                }
            )
        }
    }
}

@Composable
fun RowScope.BottomNavigationItem(
    tab: Tab,
) {
    val tabNavigator = LocalTabNavigator.current
    val selected = tabNavigator.current == tab
    val interaction = remember { MutableInteractionSource() }
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(indication = null, interactionSource = interaction) {
                tabNavigator.current = tab
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val color = if (selected) Colors.WHITE else Colors.WHITE.copy(0.5f)
        tab.options.icon?.let {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = it,
                contentDescription = tab.options.title,
                tint = color
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = tab.options.title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = color,
        )

    }
}
