package com.example.workoutapp.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.workoutapp.Workout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.DatePickerDialog
import com.example.workoutapp.WorkoutNameDropdown
import com.example.workoutapp.viewmodel.WorkoutViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutScreen(navController: NavHostController,
                     viewModel: WorkoutViewModel) {
    var workoutName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    val workoutNames = listOf("Push-ups","Pull-ups","Crunches", "Squats", "Running", "Plank", "Bench Press")
    var duration by remember { mutableStateOf("") }
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Workout") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            WorkoutNameDropdown(
                selectedName = workoutName,
                onNameSelected = { workoutName = it },
                workoutOptions = workoutNames,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duration (minutes)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { showDatePicker = true }) {
                Text(
                    text = selectedDate?.format(dateFormatter) ?: "Select Date"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val parsedDuration = duration.toIntOrNull() ?: 0
                    if (workoutName.isBlank()) {
                        errorMessage = "Please select a workout name."
                    } else if (selectedDate == null) {
                        errorMessage = "Please select a date."
                    } else if (duration.isBlank() || duration.toIntOrNull() == null) {
                        errorMessage = "Please enter a valid duration."
                    } else {
                        errorMessage = null
                        val workout = Workout(
                            name = workoutName,
                            date = selectedDate?.format(dateFormatter) ?: "N/A",
                            durationMinutes = parsedDuration
                        )
                        viewModel.addWorkout(workout)
                        navController.popBackStack()
                    }},
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
            ) {
                Text("Back")
            }

            // Date Picker Dialog
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    onDateChange = { newDate ->
                        selectedDate = newDate
                        showDatePicker = false
                    }
                )
            }
            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}