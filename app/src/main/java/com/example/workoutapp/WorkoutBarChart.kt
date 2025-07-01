package com.example.workoutapp

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.draw.scale
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch


@Composable
fun WeeklyWorkoutSummary(
    workouts: List<Workout>,
    modifier: Modifier = Modifier,
    maxBarHeight: Dp = 40.dp,
    onDayClick: (LocalDate) -> Unit = {}
) {
    val formatterDay = DateTimeFormatter.ofPattern("EEE", Locale.getDefault())
    val formatterDate = DateTimeFormatter.ofPattern("d", Locale.getDefault())
    val coroutineScope = rememberCoroutineScope()
    val today = LocalDate.now()
    val weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val workoutCounts = workouts.groupingBy { LocalDate.parse(it.date) }.eachCount()
    val maxCount = (workoutCounts.values.maxOrNull() ?: 1).coerceAtLeast(1)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0..6) {
            val day = weekStart.plusDays(i.toLong())
            val count = workoutCounts[day] ?: 0
            val isToday = day == today

            val circleColor = when {
                isToday -> MaterialTheme.colorScheme.secondary
                count > 0 -> MaterialTheme.colorScheme.primary
                else -> Color.LightGray
            }
            val textColor = if (count > 0 || isToday) Color.White else Color.Black

            val targetHeight = (count.toFloat() / maxCount) * maxBarHeight.value
            val animatedBarHeight by animateDpAsState(targetValue = targetHeight.dp)

            // Animation state for scale on click
            var clicked by remember { mutableStateOf(false) }
            val scale by animateFloatAsState(targetValue = if (clicked) 1.1f else 1f)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp)
                    .scale(scale)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                clicked = true
                                onDayClick(day)
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(150)
                                    clicked = false
                                }
                            }
                        )
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = day.format(formatterDay).uppercase(Locale.getDefault()),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (isToday) MaterialTheme.colorScheme.secondary else Color.Gray
                )
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .padding(vertical = 4.dp)
                        .shadow(4.dp, CircleShape)
                        .background(color = circleColor, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.format(formatterDate),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = textColor
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(8.dp)
                        .height(animatedBarHeight)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.small
                        )
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (count > 0) {
                    Text(
                        text = "$count",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}