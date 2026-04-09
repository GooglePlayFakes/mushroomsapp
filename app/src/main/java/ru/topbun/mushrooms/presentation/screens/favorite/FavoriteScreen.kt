package ru.topbun.mushrooms.presentation.screens.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import ru.topbun.mushrooms.R
import ru.topbun.mushrooms.presentation.screens.detail.DetailScreen
import ru.topbun.mushrooms.presentation.theme.Colors
import ru.topbun.mushrooms.presentation.theme.components.MushroomItem

object FavoriteScreen: Tab {

    override val options
        @Composable get() = TabOptions(
            index = 2U,
            title = "Избранное",
            icon = painterResource(R.drawable.ic_tabs_favorite)
        )

    @Composable
    override fun Content() {
        val viewModel = viewModel<FavoriteViewModel>()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize()
                .background(
                    Colors.BG_WHITE,
                    RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                ),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
        ) {
            items(items = state.mushrooms, key = { it.id }) {
                MushroomItem(
                    mushroom = it,
                    onClickItem = { navigator.parent?.push(DetailScreen(it.id)) },
                    onClickFavorite = { viewModel.switchFavorite(it.id) }
                )
            }
        }
        if (state.mushrooms.isEmpty()){
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Список пуст!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Colors.BLACK.copy(0.7f)
                )
            }
        }
    }

}
