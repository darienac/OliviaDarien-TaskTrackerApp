package com.example.tasktracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsProperties.ToggleableState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.tasktracker.ui.theme.TaskTrackerTheme
import java.time.format.TextStyle

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
fun TaskEntry(label: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {

    Row(modifier = Modifier
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically){
        Checkbox(
            checked = isChecked,
            // will update the tasks state
            onCheckedChange = onCheckedChange
        )
        Text(
            text = label,
            style = if (isChecked) {
                androidx.compose.ui.text.TextStyle(textDecoration = TextDecoration.LineThrough)
            } else {
                androidx.compose.ui.text.TextStyle()
            }
        )
    }

}

@Composable
fun App(modifier: Modifier = Modifier) {

    var newTask by remember {mutableStateOf("")}
    // holds name of task and whether it is completed or not
    val tasks = remember {mutableStateListOf<Pair<String, Boolean>>()}
    // whether or not the error dialog is shown for empty tasks name
    var error by remember {mutableStateOf(false)}

    fun clearCompletedTasks(){
        // list of pairs of tasks that have not been completed/checked
        val tasksToKeep = mutableListOf<Pair<String, Boolean>>()
        for (index in 0..(tasks.size-1)){
            val (task, checked) = tasks[index]
            if (!checked){
                tasksToKeep.add(tasks[index])
            }
        }

        // reassigning tasks did not work so cleared and added all of tasksToKeep to tasks instead
        tasks.clear()
        tasks.addAll(tasksToKeep)
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Transparent
            ) {
                Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
                ){
                    // Clear button
                    Button(onClick= {clearCompletedTasks()}
//            modifier =  Modifier
//                .padding(8.dp)
                    ) {
                        Text("Clear Completed")
                    }
                }

            }
        },
    ){ innerPadding ->
        Column(modifier = modifier
            .displayCutoutPadding()
            .padding(start = 16.dp, end = 16.dp, bottom = innerPadding.calculateBottomPadding()),
            horizontalAlignment = Alignment.CenterHorizontally,
            ) {
            if (error) {
                // error dialog
                Dialog(onDismissRequest = { error = false }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(innerPadding)
                            .height(200.dp)
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text(
                            text = "Please enter a task.",
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }

            // User input
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newTask,
                    onValueChange = { newTask = it },
                    label = { Text("Task Name") },
                    modifier = Modifier.weight(1f)
                        .padding(8.dp)
                )
                Button(
                    onClick = {
                        if (newTask.isEmpty()) {
                            // insert a error pop up telling user to insert a valid task
                            error = true
                        } else {
                            tasks.add(Pair(newTask, false))
                        }
                        newTask = ""
                    },
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Text("Add Task")
                }
            }
            // Tasks
            Column(
                modifier = Modifier.fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                for (index in 0..(tasks.size - 1)) {
                    val (task, checked) = tasks[index]
                    // whenever the checkboxes are interacted with, tasks state gets updated, updating the checkboxes
                    TaskEntry(task, checked, { tasks[index] = Pair(task, it) })
                }
            }
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