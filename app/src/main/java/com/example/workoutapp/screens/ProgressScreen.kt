package com.example.workoutapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workoutapp.viewmodel.WorkoutViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.workoutapp.WeeklyWorkoutSummary
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.collections.mapIndexed


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    navController: NavController,
    viewModel: WorkoutViewModel
) {
    val workoutState = viewModel.workouts.collectAsState(emptyList())
    val workouts = workoutState.value
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val modifier: Modifier = Modifier

    val validWorkouts = workouts.filter {
        try {
            LocalDate.parse(it.date)
            true
        } catch (e: Exception) {
            false
        }
    }
    Column(modifier = modifier.padding(16.dp)) {
        TopAppBar(
            title = { Text("Workout Progress") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )

        WeeklyWorkoutSummary(
            workouts = validWorkouts,
            modifier = Modifier.fillMaxWidth()
        ) { clickedDate ->
            selectedDate = clickedDate
        }

        Spacer(modifier = Modifier.height(24.dp))

        selectedDate?.let { date ->
            val formatter = DateTimeFormatter.ofPattern("EEE, MMM d", Locale.getDefault())
            Text(
                "Workouts on ${date.format(formatter)}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val workoutsOnSelectedDate = validWorkouts.filter {
                LocalDate.parse(it.date) == date
            }

            if (workoutsOnSelectedDate.isEmpty()) {
                Text("No workouts on this day.", style = MaterialTheme.typography.bodyMedium)
            } else {
                Column {
                    workoutsOnSelectedDate.forEach { workout ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(workout.name, style = MaterialTheme.typography.titleMedium)
                                Text("Date: ${workout.date}", style = MaterialTheme.typography.bodySmall)
                                // Add more workout info here if you want
                            }
                        }
                    }
                }
            }
        } ?: Text(
            "Select a day to see workouts",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 32.dp)
        )
    }
}