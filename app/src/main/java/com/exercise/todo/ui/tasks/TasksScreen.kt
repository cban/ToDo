package com.exercise.todo.ui.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exercise.todo.data.model.Task

@Composable
fun TasksScreen(modifier: Modifier = Modifier) {
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { },
                icon = { Icon(Icons.Filled.Create, "Add") },
                text = { Text(text = "Add Task") },
            )
        }

    ) { innerPadding ->
        TasksContent(
            modifier = modifier.padding(innerPadding)

        )
    }
}

@Composable
fun TasksContent(modifier: Modifier) {
    LazyColumn {
        items(5) { index ->
            TaskItem(
                task = Task(
                    title = "Task $index",
                    description = "Description $index",
                    isCompleted = false
                )
            )
        }

    }
}

@Composable
fun TaskItem(task: Task) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(intrinsicSize = IntrinsicSize.Min),
    ) {
        Row {
            VerticalDivider(
                color = if (task.isCompleted) Color.Green else Color.Red,
                thickness = 4.dp,
            )
            Column(Modifier.padding(16.dp).weight(1f)) {
                Text(text = task.title, style = MaterialTheme.typography.titleLarge)
                Text(text = task.description, style = MaterialTheme.typography.bodyMedium)
            }

            if(!task.isCompleted) {
                Checkbox(
                    checked = false,
                    onCheckedChange = { },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TasksScreenPreview() {
    TasksScreen()
}
