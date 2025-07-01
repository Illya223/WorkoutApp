package com.example.workoutapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.workoutapp.screens.MainScreen
import com.example.workoutapp.viewmodel.WorkoutViewModel

@Composable
fun WorkoutApp(viewModel: WorkoutViewModel) {
    val navController = rememberNavController()
    MainScreen(navController, viewModel)
}