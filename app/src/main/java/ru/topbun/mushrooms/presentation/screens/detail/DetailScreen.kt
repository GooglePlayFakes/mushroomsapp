package ru.topbun.mushrooms.presentation.screens.detail

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import ru.topbun.mushrooms.R
import ru.topbun.mushrooms.domain.entity.DescriptionType.*
import ru.topbun.mushrooms.domain.entity.MushroomEntity
import ru.topbun.mushrooms.presentation.theme.Colors
import ru.topbun.mushrooms.presentation.theme.utils.rememberSvgPainter

class DetailScreen(private val mushroomId: Int): Screen {

    @Composable
    override fun Content() {
        val activity = LocalActivity.currentOrThrow
        val provider = DetailViewModel.Factory(mushroomId, activity.application)
        val viewModel = viewModel<DetailViewModel>(factory = provider)
        val state by viewModel.state.collectAsState()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.BG_WHITE)
        ){
            state.mushroom?.let { mushroom ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(top = 80.dp, start = 12.dp, end = 12.dp, bottom = 20.dp)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.3f)
                            .clip(RoundedCornerShape(20.dp)),
                        model = mushroom.preview,
                        contentDescription = mushroom.title,
                        contentScale = ContentScale.FillBounds
                    )
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = mushroom.title,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Colors.BLACK.copy(0.8f)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = mushroom.latTitle,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color = Colors.BLACK.copy(0.5f),
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(Modifier.height(10.dp))
                    CategoryList(mushroom)
                    Spacer(Modifier.height(20.dp))
                    SynonymsList(mushroom)
                    Spacer(Modifier.height(20.dp))
                    OthersList(mushroom)
                    Spacer(Modifier.height(20.dp))
                    DescriptionList(mushroom)
                }
            }
            Header(viewModel, state)
        }
    }

}

@Composable
private fun DescriptionList(mushroom: MushroomEntity) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        mushroom.description.forEach {
            when(it.type){
                Title -> DescriptionTitle(it.content)
                Text -> DescriptionText(it.content)
                Image -> DescriptionImage(it.content)
            }
        }
    }
}

@Composable
private fun DescriptionTitle(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Colors.BG_GREEN,
    )
}

@Composable
private fun DescriptionText(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = Colors.BLACK.copy(0.6f),
        textAlign = TextAlign.Justify
    )
}

@Composable
private fun DescriptionImage(url: String) {
    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.3f)
            .clip(RoundedCornerShape(20.dp)),
        model = url,
        contentDescription = null,
        contentScale = ContentScale.FillBounds
    )
}

@Composable
private fun OthersList(mushroom: MushroomEntity) {
    Text(
        text = "Характеристики",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Colors.BLACK.copy(0.8f),
    )
    Spacer(Modifier.height(8.dp))
    Column(
        modifier = Modifier.padding(start = 10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        mushroom.other.forEach {
            Text(
                text = buildAnnotatedString {
                    append(it.title + ": ")
                    withStyle(SpanStyle(color = Colors.BG_GREEN)){
                        append(it.value)
                    }
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Colors.BLACK.copy(0.8f),
            )
        }
    }
}

@Composable
private fun SynonymsList(mushroom: MushroomEntity) {
    Text(
        text = "Синонимы",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Colors.BLACK.copy(0.8f),
    )
    Spacer(Modifier.height(8.dp))
    Column(
        modifier = Modifier.padding(start = 10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        mushroom.synonyms.forEach {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(Colors.BLACK.copy(0.8f))
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = it,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Colors.BLACK.copy(0.6f),
                )
            }
        }
    }
}

@Composable
private fun CategoryList(mushroom: MushroomEntity) {
    Column(
        modifier = Modifier.padding(start = 10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        mushroom.categories.forEach {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                    Image(
                        modifier = Modifier.scale(2f),
                        painter = rememberSvgPainter(it.icon),
                        contentDescription = it.name
                    )
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    text = it.name,
                    color = Colors.BLACK.copy(0.6f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun Header(viewModel: DetailViewModel, state: DetailState) {
    val navigator = LocalNavigator.currentOrThrow
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
        Spacer(Modifier.weight(1f))
        state.mushroom?.let {
            IconButton(
                onClick = { viewModel.switchFavorite() },
                colors = IconButtonDefaults.iconButtonColors(containerColor = Colors.BG_GREEN)
            ) {
                Icon(
                    modifier = Modifier.size(18.dp, 16.dp),
                    painter = painterResource(
                        if (it.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outline
                    ), contentDescription = null,
                    tint = Colors.WHITE
                )
            }
        }
    }
}