package ru.topbun.mushrooms.presentation.screens.category.fragments.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.topbun.mushrooms.domain.entity.CategoryEntity
import ru.topbun.mushrooms.presentation.screens.category.fragments.categoryMushroom.CategoryMushroomFragment
import ru.topbun.mushrooms.presentation.theme.Colors
import ru.topbun.mushrooms.presentation.theme.utils.rememberSvgPainter

object CategoryFragment: Screen {

    @Composable
    override fun Content() {
        val viewModel = viewModel<CategoryFragmentViewModel>()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.BG_WHITE),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 20.dp)
        ) {
            items(items = state.categories, key = { it.name }){
                CategoryItem(it){
                    navigator.push(CategoryMushroomFragment(it.id))
                }
            }
        }
    }

}

@Composable
private fun CategoryItem(category: CategoryEntity, onClickItem: () -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier.fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Colors.WHITE)
            .clickable(interaction, ripple()){
                onClickItem()
            }.padding(20.dp, 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ){
        Box(Modifier.size(64.dp), Alignment.Center){
            Image(
                modifier = Modifier.scale(3.5f),
                painter = rememberSvgPainter(category.icon),
                contentDescription = null
            )
        }
        Text(
            text = category.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Colors.BLACK.copy(0.7f)
        )
    }
}