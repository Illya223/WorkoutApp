package com.example.workoutapp

sealed class Screen(val route: String) {
    object WorkoutList : Screen("workout_list")
    object AddWorkout : Screen("add_workout")
    object WorkoutDetail : Screen("workout_detail/{workoutId}") {
        fun createRoute(workoutId: Int) = "workout_detail/$workoutId"
    }
    object WorkoutEdit : Screen("editWorkout/{workoutId}") {
        fun createRoute(workoutId: Int) = "editWorkout/$workoutId"
    }
}