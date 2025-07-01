package com.example.workoutapp.repository

import com.example.workoutapp.data.WorkoutDao
import com.example.workoutapp.Workout
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(private val workoutDao: WorkoutDao) {
    val allWorkouts: Flow<List<Workout>> = workoutDao.getAllWorkouts()
    suspend fun insert(workout: Workout) = workoutDao.insertWorkout(workout)
    fun getWorkoutById(id: Int): Flow<Workout?> = workoutDao.getWorkoutById(id)
}