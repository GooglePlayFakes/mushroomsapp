package ru.topbun.mushrooms.presentation.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil3.compose.AsyncImage
import ru.topbun.mushrooms.R
import ru.topbun.mushrooms.domain.entity.MushroomProfileEntity
import ru.topbun.mushrooms.domain.entity.QuizMushroomEntity
import ru.topbun.mushrooms.presentation.screens.profile.ProfileState.ProfileScreenState
import ru.topbun.mushrooms.presentation.theme.Colors
import ru.topbun.mushrooms.presentation.theme.utils.rememberSvgPainter

object ProfileScreen : Tab {

    override val options
        @Composable get() = TabOptions(
            index = 4U,
            title = "Профиль",
            icon = painterResource(R.drawable.ic_tabs_profile)
        )

    @Composable
    override fun Content() {
        val viewModel = viewModel<ProfileViewModel>()
        val state by viewModel.state.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.BG_GREEN)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Профиль грибника",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Colors.WHITE
                )
                Text(
                    text = "Твой лесной прогресс и статистика маршрутов",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Colors.WHITE.copy(0.72f)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        Colors.BG_WHITE,
                        RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                    )
            ) {
                when (state.screenState) {
                    ProfileScreenState.Loading -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Colors.BG_GREEN)
                    }

                    ProfileScreenState.Success -> ProfileContent(state)
                }
            }
        }
    }
}

@Composable
private fun ProfileContent(state: ProfileState) {
    val rank = resolveRank(state.profile.spores)
    val achievements = buildAchievements(state.profile, state.favoriteCount)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            horizontal = 12.dp,
            vertical = 16.dp
        )
    ) {
        item {
            ProfileHero(
                profile = state.profile,
                rank = rank,
                coverMushroom = state.masteredMushrooms.firstOrNull()
            )
        }
        item {
            StatsSection(state = state)
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Достижения в лесу",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Colors.BLACK.copy(0.82f)
                )
                achievements.forEach { achievement ->
                    AchievementCard(achievement)
                }
            }
        }
        item {
            MasteredSection(
                state = state,
                rankTitle = rank.title
            )
        }
    }
}

@Composable
private fun ProfileHero(
    profile: MushroomProfileEntity,
    rank: ForestRank,
    coverMushroom: QuizMushroomEntity?
) {
    val remaining = rank.nextThreshold?.minus(profile.spores)?.coerceAtLeast(0)
    val progress = rank.progress(profile.spores)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .shadow(4.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(Colors.BG_GREEN)
    ) {
        coverMushroom?.let {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = it.preview,
                contentDescription = it.title,
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Colors.BG_GREEN.copy(0.82f))
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = rank.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Colors.WHITE
                )
                Text(
                    text = "${profile.spores} спор в кармане",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Colors.WHITE
                )
                if (remaining != null) {
                    Text(
                        text = "До следующего ранга ещё $remaining спор",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Colors.WHITE.copy(0.74f)
                    )
                } else {
                    Text(
                        text = "Максимальный ранг взят. Лес тебя уже знает.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Colors.WHITE.copy(0.74f)
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(Colors.WHITE.copy(0.18f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .background(Colors.WHITE)
                    )
                }
                coverMushroom?.let {
                    Text(
                        text = "Коронный гриб: ${it.title}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Colors.WHITE.copy(0.8f)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsSection(state: ProfileState) {
    val accuracy = state.profile.accuracy()
    val masteredCount = state.profile.masteredMushroomIds.size

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Экспедиции",
                value = state.profile.quizzesCompleted.toString()
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Точность",
                value = "$accuracy%"
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Лучшая серия",
                value = state.profile.bestStreak.toString()
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Идеальные",
                value = state.profile.perfectRounds.toString()
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Освоено видов",
                value = masteredCount.toString()
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "В избранном",
                value = state.favoriteCount.toString()
            )
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .shadow(3.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(Colors.WHITE)
            .padding(horizontal = 14.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Colors.GRAY
        )
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Colors.BG_GREEN
        )
    }
}

@Composable
private fun AchievementCard(achievement: ProfileAchievement) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(Colors.WHITE)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Colors.BG_GREEN.copy(0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(achievement.iconRes),
                contentDescription = null,
                tint = Colors.BG_GREEN
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = achievement.title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Colors.BLACK.copy(0.82f)
            )
            Text(
                text = achievement.subtitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Colors.GRAY
            )
        }
    }
}

@Composable
private fun MasteredSection(
    state: ProfileState,
    rankTitle: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Освоенные виды",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Colors.BLACK.copy(0.82f)
        )
        if (state.masteredMushrooms.isEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(14.dp))
                    .clip(RoundedCornerShape(14.dp))
                    .background(Colors.WHITE)
                    .padding(horizontal = 14.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Colors.BG_GREEN.copy(0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_tabs_quiz),
                        contentDescription = null,
                        tint = Colors.BG_GREEN
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Атлас пока пуст",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Colors.BLACK.copy(0.82f)
                    )
                    Text(
                        text = "Пройди первый квиз, чтобы начать собирать собственный лесной архив.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Colors.GRAY
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                state.masteredMushrooms.forEach { mushroom ->
                    MasteredMushroomCard(mushroom)
                }
            }
        }
        Text(
            text = "Текущий титул: $rankTitle",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Colors.GRAY
        )
    }
}

@Composable
private fun MasteredMushroomCard(mushroom: QuizMushroomEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(Colors.WHITE)
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(width = 88.dp, height = 76.dp)
                .clip(RoundedCornerShape(12.dp)),
            model = mushroom.preview,
            contentDescription = mushroom.title,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = mushroom.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Colors.BLACK.copy(0.82f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = mushroom.latTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Italic,
                color = Colors.GRAY,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            mushroom.category?.let { category ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.scale(1.2f),
                        painter = rememberSvgPainter(category.icon),
                        contentDescription = category.name
                    )
                    Text(
                        text = category.name,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Colors.BG_GREEN,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

private fun MushroomProfileEntity.accuracy(): Int {
    if (answeredQuestions == 0) return 0
    return (correctAnswers * 100f / answeredQuestions).toInt()
}

private data class ForestRank(
    val title: String,
    val minThreshold: Int,
    val nextThreshold: Int?
) {
    fun progress(spores: Int): Float {
        val next = nextThreshold ?: return 1f
        return ((spores - minThreshold).toFloat() / (next - minThreshold).toFloat())
            .coerceIn(0f, 1f)
    }
}

private data class ProfileAchievement(
    val title: String,
    val subtitle: String,
    val iconRes: Int
)

private fun resolveRank(spores: Int) = when {
    spores < 60 -> ForestRank("Наблюдатель мицелия", 0, 60)
    spores < 160 -> ForestRank("Тихий охотник", 60, 160)
    spores < 320 -> ForestRank("Следопыт спор", 160, 320)
    spores < 520 -> ForestRank("Хранитель атласа", 320, 520)
    else -> ForestRank("Миколог-радар", 520, null)
}

private fun buildAchievements(
    profile: MushroomProfileEntity,
    favoriteCount: Int
): List<ProfileAchievement> {
    val masteredCount = profile.masteredMushroomIds.size
    return listOf(
        ProfileAchievement(
            title = if (profile.quizzesCompleted > 0) "Маршрут открыт" else "Маршрут ждёт",
            subtitle = if (profile.quizzesCompleted > 0) {
                "Уже пройдено ${profile.quizzesCompleted} лесных экспедиций."
            } else {
                "Первый квиз откроет твою личную тропу грибника."
            },
            iconRes = R.drawable.ic_tabs_quiz
        ),
        ProfileAchievement(
            title = if (profile.bestStreak >= 3) "Безошибочный след" else "След ещё сбивается",
            subtitle = if (profile.bestStreak >= 3) {
                "Лучшая серия уже ${profile.bestStreak} грибов подряд."
            } else {
                "Собери серию хотя бы из 3 верных ответов подряд."
            },
            iconRes = R.drawable.ic_tabs_main
        ),
        ProfileAchievement(
            title = if (masteredCount >= 5) "Коллекционер видов" else "Атлас в сборке",
            subtitle = if (masteredCount >= 5) {
                "В личном атласе уже $masteredCount освоенных видов."
            } else {
                "Освоено $masteredCount видов, добавь ещё грибов в атлас."
            },
            iconRes = R.drawable.ic_tabs_category
        ),
        ProfileAchievement(
            title = if (favoriteCount >= 3) "Корзина собрана" else "Избранное растёт",
            subtitle = if (favoriteCount >= 3) {
                "В избранном уже $favoriteCount находок."
            } else {
                "Помечай интересные грибы, чтобы собрать собственную корзину."
            },
            iconRes = R.drawable.ic_tabs_favorite
        )
    )
}
