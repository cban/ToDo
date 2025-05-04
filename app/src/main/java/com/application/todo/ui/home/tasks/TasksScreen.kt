package com.application.todo.ui.home.tasks

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.application.todo.R
import com.application.todo.data.model.Task
import com.application.todo.ui.home.TodoViewModel
import com.application.todo.ui.home.UiEvents
import com.application.todo.ui.home.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel,
    state: UiState<List<Task>>
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    showDialog = true
                },
                icon = { Icon(Icons.Filled.Create, stringResource(R.string.add)) },
                text = { Text(text = stringResource(R.string.add_task)) },
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
                    tasks = viewModel.getFilteredTasks(state.data, isCompleted = false),
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
fun TasksContent(modifier: Modifier, tasks: List<Task>, viewModel: TodoViewModel) {
    var taskToDelete by remember { mutableStateOf<Task?>(null) }

    val context = LocalContext.current

    if (tasks.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(R.string.no_tasks_available))
        }
        return
    }

    LazyColumn {
        items(tasks) { task ->
            if (task.isCompleted) {
                return@items
            }
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                visible = true
            }

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(),
                exit = shrinkVertically() + fadeOut(),
            ) {
                TaskItem(
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
                        taskToDelete = task
                    }
                )
            }
            taskToDelete?.let { task ->
                ConfirmDialog(
                    onDismissRequest = { taskToDelete = null },
                    onConfirm = {
                        viewModel.handleEvent(
                            UiEvents.DeleteTask(task.id)
                        )
                        taskToDelete = null
                    }
                )
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
                                context.getString(R.string.task_completed, task.title),
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
                tint = Color.Black,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        onConfirm()
                    },
                contentDescription = stringResource(R.string.delete)
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
        title = { Text(text = stringResource(R.string.delete_task)) },
        text = { Text(text = stringResource(R.string.are_you_sure_you_want_to_delete_this_task)) },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text(text = stringResource(R.string.delete))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { onDismissRequest() }) {
                Text(text = stringResource(R.string.cancel))
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
                        stringResource(R.string.add_a_task_to_your_to_do_list),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
                OutlinedTextField(
                    label = { Text(stringResource(R.string.enter_title)) },
                    value = task.title,
                    onValueChange = { task = task.copy(title = it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                OutlinedTextField(
                    label = { Text(stringResource(R.string.enter_description)) },
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
                        Text(stringResource(R.string.add_task))
                    }
                    OutlinedButton(
                        onClick = { onDismissRequest() },
                    ) {
                        Text(stringResource(R.string.cancel))

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

