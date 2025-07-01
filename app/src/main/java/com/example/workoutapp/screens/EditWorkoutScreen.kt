package com.example.workoutapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDefaults.dateFormatter
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workoutapp.viewmodel.WorkoutViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.workoutapp.DatePickerDialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun EditWorkoutScreen(
    navController: NavController,
    workoutId: Int,
    viewModel: WorkoutViewModel
) {
    val workoutState = viewModel.getWorkoutById(workoutId).collectAsState(initial = null)
    val workout = workoutState.value

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    LaunchedEffect(workout) {
        if (workout != null) {
            name = workout.name
            date = workout.date
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Edit Workout", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Workout Name") },
            modifier = Modifier.fillMaxWidth()
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
                val updatedWorkout = workout?.copy(name = name, date = selectedDate?.format(dateFormatter) ?: "N/A")
                if (updatedWorkout != null) {
                    viewModel.updateWorkout(updatedWorkout)
                    navController.popBackStack() // go back after save
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save")
        }
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                onDateChange = { newDate ->
                    selectedDate = newDate
                    showDatePicker = false
                }
            )
        }
    }
}