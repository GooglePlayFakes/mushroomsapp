package ru.topbun.mushrooms.presentation.theme.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.mushrooms.presentation.theme.Colors

@Composable
fun PaginationLoader(isEndList: Boolean, isLoading: Boolean, isEmpty: Boolean, key: Any, onLoad: () -> Unit) {
    if (!isEndList) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = Colors.BG_GREEN)
            }
        } else {
            LaunchedEffect(key) {
                onLoad()
            }
        }
    } else {
        if (isEmpty){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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