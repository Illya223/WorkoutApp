package com.example.workoutapp

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.foundation.layout.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutNameDropdown(
    selectedName: String,
    onNameSelected: (String) -> Unit,
    workoutOptions: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedName) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            value = selectedName,
            onValueChange = {
                selectedText = it
                onNameSelected(it) // live update to parent
            },
            label = { Text("Workout Name") },
            modifier = Modifier.menuAnchor(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            workoutOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedText = option
                        onNameSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}