package com.example.tasktracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tasktracker.ui.theme.TaskTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TaskEntry(label: String) {
    Text(label)
}

@Composable
fun App(modifier: Modifier = Modifier) {
    var newTask by remember {mutableStateOf("")}
    val tasks = remember {mutableStateListOf<String>()}

    fun addTask(task: String) {
        tasks.add(task)
    }

    Column(modifier = modifier.displayCutoutPadding()) {
        Row() {
            OutlinedTextField(
                value = newTask,
                onValueChange = {newTask = it},
                label = {Text("Task Name")},
                modifier = Modifier.weight(1f)
            )
            Button(onClick={
                tasks.add(newTask)
            }) {
                Text("Create")
            }
        }

        for (item in tasks) {
            TaskEntry(item)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TaskTrackerTheme {
        App()
    }
}