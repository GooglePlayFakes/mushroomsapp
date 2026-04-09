package ru.topbun.mushrooms.presentation.screens.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil3.compose.AsyncImage
import ru.topbun.mushrooms.R
import ru.topbun.mushrooms.domain.entity.QuizQuestionEntity
import ru.topbun.mushrooms.presentation.screens.quiz.QuizState.QuizScreenState
import ru.topbun.mushrooms.presentation.theme.Colors

object QuizScreen : Tab {

    override val options
        @Composable get() = TabOptions(
            index = 3U,
            title = "Квиз",
            icon = painterResource(R.drawable.ic_tabs_quiz)
        )

    @Composable
    override fun Content() {
        val viewModel = viewModel<QuizViewModel>()
        val state by viewModel.state.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.BG_GREEN)
        ) {
            QuizHeader(state = state)
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
                    QuizScreenState.Loading -> QuizLoading()
                    QuizScreenState.Error -> QuizError(state.errorText, viewModel::startNewRound)
                    QuizScreenState.Playing -> QuizRoute(state, viewModel)
                    QuizScreenState.Finished -> QuizResult(state, viewModel::startNewRound)
                }
            }
        }
    }
}

@Composable
private fun QuizHeader(state: QuizState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Лесной квиз",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Colors.WHITE
                )
                Text(
                    text = "Фото, латынь и категории грибов",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Colors.WHITE.copy(0.72f)
                )
            }
            QuizBadge(
                title = "Споры",
                value = state.profile.spores.toString()
            )
        }
        if (state.screenState is QuizScreenState.Playing || state.screenState is QuizScreenState.Finished) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuizBadge(
                    modifier = Modifier.weight(1f),
                    title = "Точность",
                    value = "${state.correctAnswers}/${state.totalQuestions}"
                )
                QuizBadge(
                    modifier = Modifier.weight(1f),
                    title = "Маршрут",
                    value = "${minOf(state.currentIndex + 1, maxOf(state.totalQuestions, 1))}/${maxOf(state.totalQuestions, 1)}"
                )
                QuizBadge(
                    modifier = Modifier.weight(1f),
                    title = "Серия",
                    value = state.bestStreak.toString()
                )
            }
        }
    }
}

@Composable
private fun QuizBadge(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Colors.WHITE.copy(0.12f))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Colors.WHITE.copy(0.72f)
        )
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Colors.WHITE
        )
    }
}

@Composable
private fun QuizRoute(
    state: QuizState,
    viewModel: QuizViewModel
) {
    val question = state.currentQuestion ?: return
    val progress = if (state.totalQuestions == 0) 0f else (state.currentIndex + 1) / state.totalQuestions.toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Вопрос ${state.currentIndex + 1}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Colors.BLACK.copy(0.78f)
                )
                Text(
                    text = "+${state.sporesEarned} спор",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Colors.BG_GREEN
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Colors.GRAY.copy(0.18f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(Colors.BG_GREEN)
                )
            }
        }
        QuizQuestionCard(question)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            question.options.forEach { option ->
                QuizOption(
                    text = option,
                    isLocked = state.isAnswerLocked,
                    isSelected = state.selectedAnswer == option,
                    isCorrect = option == question.correctAnswer,
                    onClick = { viewModel.chooseAnswer(option) }
                )
            }
        }
        if (state.isAnswerLocked) {
            QuizExplanation(
                question = question,
                selectedAnswer = state.selectedAnswer,
                isCorrect = state.selectedAnswer == question.correctAnswer
            )
            AppActionButton(
                text = if (state.currentIndex == state.totalQuestions - 1) "Завершить маршрут" else "Следующий гриб",
                onClick = viewModel::moveNext
            )
        }
    }
}

@Composable
private fun QuizQuestionCard(question: QuizQuestionEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Colors.WHITE),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            model = question.preview,
            contentDescription = question.correctAnswer,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 2.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = question.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Colors.BLACK.copy(0.82f)
            )
            Text(
                text = question.subtitle,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Colors.GRAY
            )
        }
        Spacer(Modifier.height(2.dp))
    }
}

@Composable
private fun QuizOption(
    text: String,
    isLocked: Boolean,
    isSelected: Boolean,
    isCorrect: Boolean,
    onClick: () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(14.dp)
    val bgColor = when {
        isLocked && isCorrect -> Color(0xffF2F8F1)
        isLocked && isSelected -> Color(0xffFFF3EF)
        else -> Colors.WHITE
    }
    val borderColor = when {
        isLocked && isCorrect -> Colors.BG_GREEN.copy(0.32f)
        isLocked && isSelected -> Color(0xffE0B1A7)
        else -> Color(0xffE7E8E3)
    }
    val titleColor = when {
        isLocked && isCorrect -> Colors.BG_GREEN
        isLocked && isSelected -> Color(0xff9A3D3D)
        else -> Colors.BLACK.copy(0.82f)
    }
    val indicatorBgColor = when {
        isLocked && isCorrect -> Colors.BG_GREEN
        isLocked && isSelected -> Color(0xffC56A58)
        else -> Colors.GRAY.copy(0.14f)
    }
    val indicatorText = when {
        isLocked && isCorrect -> "✓"
        isLocked && isSelected -> "×"
        else -> "•"
    }
    val indicatorTextColor = when {
        isLocked && isCorrect || isLocked && isSelected -> Colors.WHITE
        else -> Colors.GRAY
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isLocked && (isCorrect || isSelected)) 1.dp else 3.dp,
                shape = shape
            )
            .clip(shape)
            .background(bgColor)
            .border(1.dp, borderColor, shape)
            .clickable(
                enabled = !isLocked,
                interactionSource = interaction,
                indication = ripple()
            ) { onClick() }
            .padding(horizontal = 14.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(indicatorBgColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = indicatorText,
                fontSize = if (indicatorText == "•") 18.sp else 15.sp,
                fontWeight = FontWeight.Bold,
                color = indicatorTextColor
            )
        }
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = titleColor
        )
    }
}

@Composable
private fun QuizExplanation(
    question: QuizQuestionEntity,
    selectedAnswer: String?,
    isCorrect: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isCorrect) Colors.BG_GREEN.copy(0.12f) else Color(0xffFFF1ED))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = if (isCorrect) "Точный ответ" else "Не тот след",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (isCorrect) Colors.BG_GREEN else Color(0xff9A3D3D)
        )
        if (!isCorrect && selectedAnswer != null) {
            Text(
                text = "Ты выбрал: $selectedAnswer",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Colors.BLACK.copy(0.64f)
            )
        }
        Text(
            text = "Правильный ответ: ${question.correctAnswer}",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Colors.BLACK.copy(0.78f)
        )
        Text(
            text = question.explanation,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Colors.GRAY
        )
    }
}

@Composable
private fun QuizResult(
    state: QuizState,
    onRestart: () -> Unit
) {
    val perfectRoute = state.correctAnswers == state.totalQuestions && state.totalQuestions > 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(18.dp))
                .clip(RoundedCornerShape(18.dp))
                .background(Colors.WHITE)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = if (perfectRoute) "Идеальный маршрут" else "Экспедиция завершена",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Colors.BLACK.copy(0.82f)
            )
            Text(
                text = "${state.correctAnswers} из ${state.totalQuestions} ответов верные",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Colors.BG_GREEN
            )
            Text(
                text = "Заработано ${state.sporesEarned} спор. Всего в профиле: ${state.profile.spores}.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Colors.GRAY
            )
            if (perfectRoute) {
                Text(
                    text = "Бонус за безошибочный проход уже добавлен в профиль грибника.",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Colors.BLACK.copy(0.72f)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ResultStatCard(
                modifier = Modifier.weight(1f),
                title = "Лучшая серия",
                value = state.bestStreak.toString()
            )
            ResultStatCard(
                modifier = Modifier.weight(1f),
                title = "Новые виды",
                value = state.masteredMushroomIds.size.toString()
            )
        }
        AppActionButton(
            text = "Новый маршрут",
            onClick = onRestart
        )
    }
}

@Composable
private fun ResultStatCard(
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
private fun QuizLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Colors.BG_GREEN)
    }
}

@Composable
private fun QuizError(
    errorText: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = errorText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Colors.BLACK.copy(0.78f),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(14.dp))
        AppActionButton(
            text = "Собрать заново",
            onClick = onRetry
        )
    }
}

@Composable
private fun AppActionButton(
    text: String,
    onClick: () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Colors.BG_GREEN)
            .clickable(
                interactionSource = interaction,
                indication = ripple()
            ) { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Colors.WHITE
        )
    }
}
