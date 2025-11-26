package ru.topbun.mushrooms.presentation.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import ru.topbun.mushrooms.R
import ru.topbun.mushrooms.domain.entity.MushroomEntity
import ru.topbun.mushrooms.presentation.theme.Colors
import ru.topbun.mushrooms.presentation.theme.utils.rememberSvgPainter

@Composable
fun MushroomItem(mushroom: MushroomEntity, onClickItem: () -> Unit, onClickFavorite: () -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    Column(
        modifier = Modifier.fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Colors.WHITE)
            .clickable(interaction, ripple()){
                onClickItem()
            }.padding(12.dp, 8.dp)
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(1.3f)
                .clip(RoundedCornerShape(7.dp)),
            model = mushroom.preview,
            contentDescription = mushroom.title,
            contentScale = ContentScale.FillBounds
        )
        Spacer(Modifier.height(8.dp))
        Row{
            Text(
                modifier = Modifier.weight(1f),
                text = mushroom.title,
                color = Colors.BLACK.copy(0.8f),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.width(10.dp))
            Icon(
                modifier = Modifier.size(18.dp, 16.dp)
                    .clickable(null, null){ onClickFavorite() },
                painter = painterResource(
                    if (mushroom.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outline
                ), contentDescription = null,
                tint = Colors.BG_GREEN
            )
        }
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically){
            val category = mushroom.categories.firstOrNull()
            category?.let {
                Image(
                    modifier = Modifier.scale(1.2f),
                    painter = rememberSvgPainter(category.icon),
                    contentDescription = category.name
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = category.name,
                    color = Colors.BLACK.copy(0.4f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}