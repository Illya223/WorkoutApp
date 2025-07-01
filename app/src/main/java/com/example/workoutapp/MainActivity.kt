package com.example.workoutapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.workoutapp.data.WorkoutDatabase
import com.example.workoutapp.repository.WorkoutRepository
import com.example.workoutapp.screens.MainScreen
import com.example.workoutapp.ui.WorkoutApp
import com.example.workoutapp.ui.theme.WorkoutAppTheme
import com.example.workoutapp.viewmodel.WorkoutViewModel
import com.example.workoutapp.viewmodel.WorkoutViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = WorkoutDatabase.getDatabase(this)
        val repository = WorkoutRepository(db.workoutDao())
        val factory = WorkoutViewModelFactory(repository)
        val viewModel: WorkoutViewModel = ViewModelProvider(this, factory)[WorkoutViewModel::class.java]


        setContent {
            WorkoutApp(viewModel)
        }
    }
}

