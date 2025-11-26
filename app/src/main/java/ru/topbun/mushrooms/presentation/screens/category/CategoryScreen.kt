package ru.topbun.mushrooms.presentation.screens.category

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import ru.topbun.mushrooms.R
import ru.topbun.mushrooms.presentation.screens.category.fragments.category.CategoryFragment

object CategoryScreen: Tab {

    override val options
        @Composable get() = TabOptions(
            index = 0U,
            title = "Категории",
            icon = painterResource(R.drawable.ic_tabs_category)
        )

    @Composable
    override fun Content() {
        Navigator(CategoryFragment)
    }
}