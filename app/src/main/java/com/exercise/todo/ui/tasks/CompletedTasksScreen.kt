package com.exercise.todo.ui.tasks


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exercise.todo.data.model.Task

@Composable
fun CompletedTasksScreen(
    modifier: Modifier = Modifier,
    viewModel: TasksViewModel,
    state: UiState
) {
    Scaffold { innerPadding ->
        when (state) {
            is UiState.Loading -> LoadingIndicator()
            is UiState.Success -> {
                CompletedTasksContent(
                    modifier = modifier
                        .padding(innerPadding)
                        .padding(top = 24.dp),
                    viewModel = viewModel,
                    tasks = state.tasks?.filter { it.isCompleted } ?: emptyList()
                )
            }

            is UiState.Error -> TODO()
            UiState.Idle -> TODO()
        }
    }
}

@Composable
fun CompletedTasksContent(
    modifier: Modifier = Modifier,
    tasks: List<Task>, viewModel: TasksViewModel
) {
    var taskToDelete by remember { mutableStateOf<Task?>(null) }
    
    if (tasks.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No Completed tasks available")
        }
        return
    }


    LazyColumn {
        items(tasks, key = { task -> task.id }) { task ->
            TaskItem(
                task = task,
                onCheckedChange = {
                },
                onConfirm = {
                    taskToDelete = task
                },
            )
            taskToDelete?.let { task ->
                ConfirmDialog(
                    onDismissRequest = {
                        taskToDelete = null
                    },
                    onConfirm = {
                        viewModel.handleEvent(UiEvents.DeleteTask(task.id))
                        taskToDelete = null
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun CompletedTasksScreenPreview() {
}



