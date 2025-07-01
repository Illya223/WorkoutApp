package com.example.workoutapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDefaults.dateFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.input.KeyboardType
import com.example.workoutapp.DatePickerDialog
import com.example.workoutapp.WorkoutNameDropdown
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
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
    var duration by remember { mutableStateOf("") }
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    val workoutNames = listOf("Push-ups","Pull-ups","Crunches", "Squats", "Running", "Plank", "Bench Press")

    LaunchedEffect(workout) {
        if (workout != null) {
            name = workout.name
            date = workout.date
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        TopAppBar(
            title = { Text("Edit Workout") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )


        Spacer(modifier = Modifier.height(16.dp))

        WorkoutNameDropdown(
            selectedName = name,
            onNameSelected = { name = it },
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