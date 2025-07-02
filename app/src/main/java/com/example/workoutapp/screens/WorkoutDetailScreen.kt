package com.example.workoutapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.workoutapp.Workout
import com.example.workoutapp.viewmodel.WorkoutViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color


@Composable
fun WorkoutDetailScreen(navController: NavHostController, workoutId: Int,
                        viewModel: WorkoutViewModel) {
    val workoutState = viewModel.getWorkoutById(workoutId).collectAsState(initial = null)
    val workout = workoutState.value

    Column(modifier = Modifier.padding(16.dp)) {
        if (workout != null) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Workout: ${workout.name}", style = MaterialTheme.typography.titleLarge)
                Text("Date: ${workout.date}")
                Text("Duration: ${workout.durationMinutes} minutes")
                Text(" ${workout} ")
            }
        } else {
            Text("Loading...")
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate("editWorkout/${workoutId}") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Edit")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
        ) {
            Text("Back", color = Color.Black)
        }
    }



}