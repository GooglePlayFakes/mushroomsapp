package ru.topbun.mushrooms.presentation.screens.category.fragments.categoryMushroom

import android.text.TextUtils.isEmpty
import android.widget.Space
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImagePainter.State.Empty.painter
import ru.topbun.mushrooms.R
import ru.topbun.mushrooms.domain.entity.CategoryEntity
import ru.topbun.mushrooms.presentation.screens.category.fragments.category.CategoryFragmentState
import ru.topbun.mushrooms.presentation.screens.category.fragments.category.CategoryFragmentViewModel
import ru.topbun.mushrooms.presentation.screens.category.fragments.categoryMushroom.CategoryMushroomFragmentState.CategoryScreenState
import ru.topbun.mushrooms.presentation.screens.detail.DetailScreen
import ru.topbun.mushrooms.presentation.screens.main.MainState.MainScreenState
import ru.topbun.mushrooms.presentation.theme.Colors
import ru.topbun.mushrooms.presentation.theme.components.MushroomItem
import ru.topbun.mushrooms.presentation.theme.components.PaginationLoader
import ru.topbun.mushrooms.presentation.theme.utils.rememberSvgPainter

class CategoryMushroomFragment(private val categoryId: Int): Screen {

    @Composable
    override fun Content() {
        val activity = LocalActivity.currentOrThrow
        val provider = CategoryMushroomFragmentViewModel.Factory(categoryId, activity.application)
        val viewModel = viewModel<CategoryMushroomFragmentViewModel>(factory = provider)
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        Column{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Colors.BG_WHITE)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(
                    onClick = { navigator.pop() },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Colors.BG_GREEN)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_back),
                        contentDescription = null,
                        tint = Colors.WHITE
                    )
                }
                Spacer(Modifier.width(20.dp))
                Text(
                    text = state.category?.name ?: "",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Colors.BLACK.copy(0.7f)
                )
            }
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
                        onClickItem = { navigator.parent?.parent?.push(DetailScreen(it.id)) },
                        onClickFavorite = { viewModel.switchFavorite(it.id) }
                    )
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    PaginationLoader(
                        isEndList = state.isEndList,
                        isLoading = state.screenState is CategoryScreenState.Loading,
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
