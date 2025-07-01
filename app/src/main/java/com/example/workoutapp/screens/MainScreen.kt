package com.example.workoutapp.screens


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.workoutapp.Screen
import com.example.workoutapp.viewmodel.WorkoutViewModel


@Composable
fun MainScreen(navController: NavHostController
               , viewModel: WorkoutViewModel) {
    NavHost(navController = navController, startDestination = Screen.WorkoutList.route) {
        composable(Screen.WorkoutList.route) {
            WorkoutListScreen(navController, viewModel)
        }
        composable(Screen.AddWorkout.route) {
            AddWorkoutScreen(navController, viewModel)
        }
        composable(
            Screen.WorkoutDetail.route,
            arguments = listOf(navArgument("workoutId") { type = NavType.IntType })
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getInt("workoutId") ?: 0
            WorkoutDetailScreen(navController, workoutId, viewModel)
        }
        composable(
            Screen.WorkoutEdit.route,
            arguments = listOf(navArgument("workoutId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("workoutId") ?: -1
            EditWorkoutScreen(navController, workoutId = id, viewModel)
        }
        composable("progress") {
            ProgressScreen(navController, viewModel)
        }
    }
}


