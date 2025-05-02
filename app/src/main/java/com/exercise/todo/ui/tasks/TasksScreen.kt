package com.exercise.todo.ui.tasks

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.exercise.todo.data.model.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    viewModel: TasksViewModel,
    state: UiState
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    showDialog = true
                },
                icon = { Icon(Icons.Filled.Create, "Add") },
                text = { Text(text = "Add Task") },
            )
            if (showDialog) {
                CustomDialog(
                    onDismissRequest = { showDialog = false },
                    onConfirm = { task ->
                        viewModel.handleEvent(
                            UiEvents.AddTask(task)
                        )
                        showDialog = false
                    }
                )
            }
        }

    ) { innerPadding ->
        when (state) {
            is UiState.Loading -> LoadingIndicator()
            is UiState.Success -> {
                TasksContent(
                    modifier = modifier
                        .padding(innerPadding)
                        .padding(top = 16.dp),
                    tasks = state.tasks?.filter { !it.isCompleted } ?: emptyList(),
                    viewModel = viewModel
                )
            }

            UiState.Idle -> TasksContent(
                modifier = modifier
                    .padding(innerPadding)
                    .padding(top = 16.dp),
                tasks = emptyList(),
                viewModel = viewModel
            )

            is UiState.Error -> ErrorDisplay(
                state = state,
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}


@Composable
fun TasksContent(modifier: Modifier, tasks: List<Task>, viewModel: TasksViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    if (tasks.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No tasks available")
        }
        return
    }

    LazyColumn {
        items(tasks) { task ->
            if (task.isCompleted) {
                return@items
            }
            TaskItem(
                modifier.animateItem(),
                task = task,
                onCheckedChange = { isChecked ->
                    viewModel.handleEvent(
                        UiEvents.UpdateTask(
                            task.copy(
                                isCompleted = isChecked
                            )
                        )
                    )
                },
                onConfirm = {
                    showDialog = true
                }
            )
            if (showDialog) {
                ConfirmDialog(
                    onDismissRequest = { showDialog = false },
                    onConfirm = {
                        viewModel.handleEvent(
                            UiEvents.DeleteTask(task.id)
                        )
                        showDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    onConfirm: () -> Unit
) {
    val context = LocalContext.current
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
            if (!task.isCompleted) {
                Checkbox(
                    checked = false,
                    onCheckedChange = {
                        onCheckedChange(it)
                        if (it) {
                            Toast.makeText(
                                context,
                                "${task.title} is Completed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }
            Column(
                Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text(text = task.title, style = MaterialTheme.typography.titleLarge)
                Text(text = task.description, style = MaterialTheme.typography.bodyMedium)
            }
            Icon(
                imageVector = Icons.Filled.Delete,
                tint = Color.Gray,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        onConfirm()
                    },
                contentDescription = "Delete"
            )
        }
    }
}

@Composable
fun ConfirmDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = "Delete Task") },
        text = { Text(text = "Are you sure you want to delete this task?") },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { onDismissRequest() }) {
                Text(text = "Cancel")
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (task: Task) -> Unit,
) {
    var task by remember { mutableStateOf(Task(0, "", "", false)) }
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text =
                        "Add A Task to your To Do",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
                OutlinedTextField(
                    label = { Text("Enter Title") },
                    value = task.title,
                    onValueChange = { task = task.copy(title = it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                OutlinedTextField(
                    label = { Text("Enter Description") },
                    value = task.description,
                    onValueChange = { value -> task = task.copy(description = value) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Button(
                        onClick = { onConfirm(task) },
                    ) {
                        Text("Add Task")
                    }
                    OutlinedButton(
                        onClick = { onDismissRequest() },
                    ) {
                        Text("Cancel")
                    }
                }


            }
        }
    }
}


@Composable
fun ErrorDisplay(state: UiState.Error, modifier: Modifier = Modifier) {
    Box {
        Text(
            text = state.message,
            modifier = modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
fun DialogPreview() {
    CustomDialog(
        onDismissRequest = {},
        onConfirm = {},
    )
}

@Preview
@Composable
fun TaskItemPreview() {
    TaskItem(
        task = Task(0, "Task Title", "Task Description", false),
        onCheckedChange = {},
        onConfirm = {}
    )
}

