package ru.topbun.mushrooms.presentation.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil3.compose.AsyncImage
import ru.topbun.mushrooms.R
import ru.topbun.mushrooms.domain.entity.MushroomEntity
import ru.topbun.mushrooms.presentation.screens.detail.DetailScreen
import ru.topbun.mushrooms.presentation.screens.main.MainState.MainScreenState
import ru.topbun.mushrooms.presentation.theme.Colors
import ru.topbun.mushrooms.presentation.theme.components.MushroomItem
import ru.topbun.mushrooms.presentation.theme.components.PaginationLoader
import ru.topbun.mushrooms.presentation.theme.utils.rememberSvgPainter
import java.util.Locale.getDefault

object MainScreen : Tab {

    override val options
        @Composable get() = TabOptions(
            index = 0U,
            title = "Главная",
            icon = painterResource(R.drawable.ic_tabs_main)
        )

    @Composable
    override fun Content() {
        val viewModel = viewModel<MainViewModel>()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.BG_GREEN)
        ) {
            Header(viewModel, state)
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
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
                item(span = { GridItemSpan(maxLineSpan) }) {
                    PaginationLoader(
                        isEndList = state.isEndList,
                        isLoading = state.screenState is MainScreenState.Loading,
                        isEmpty = state.mushrooms.isEmpty(),
                        key = state.mushrooms
                    ){
                        viewModel.loadMushrooms()
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(viewModel: MainViewModel, state: MainState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AppTextField(state.query, viewModel::changeQuery)
        SortedTypeTabs(
            items = state.tabs.map { it.toUiString() },
            selectedIndex = state.selectedIndex,
            onChangeItem = viewModel::changeTabSelected
        )
    }
}

@Composable
private fun SortedTypeTabs(
    items: List<String>,
    selectedIndex: Int,
    onChangeItem: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(38.dp)
    ) {
        items.forEachIndexed { index, item ->
            val bgColor = Colors.WHITE.takeIf { index == selectedIndex } ?: Colors.BG_GREEN
            val textColor = Colors.BG_GREEN.takeIf { index == selectedIndex } ?: Color(0xffE9ECE5)

            val isFirst = index == 0
            val isLast = items.size - 1 == index

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clip(
                        RoundedCornerShape(
                            topStart = if (isFirst) 6.dp else 0.dp,
                            bottomStart = if (isFirst) 6.dp else 0.dp,
                            topEnd = if (isLast) 6.dp else 0.dp,
                            bottomEnd = if (isLast) 6.dp else 0.dp,
                        )
                    )
                    .background(color = bgColor)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = ripple()
                    ) { onChangeItem(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.uppercase(getDefault()),
                    style = TextStyle(
                        color = textColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    ),
                )
            }
        }
    }
}

@Composable
private fun AppTextField(value: String, onValueChange: (String) -> Unit) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = Colors.GRAY,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        ),
        singleLine = true,
    ) { textField ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Colors.WHITE, RoundedCornerShape(8.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
                tint = Colors.GRAY
            )
            Box {
                if (value.isBlank()) {
                    Text(
                        text = "Поиск",
                        color = Colors.GRAY.copy(0.5f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                textField()

            }
        }
    }
}