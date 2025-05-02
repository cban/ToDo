package com.exercise.todo.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exercise.todo.ui.tasks.CompletedTasksScreen
import com.exercise.todo.ui.tasks.TasksScreen
import com.exercise.todo.ui.tasks.TasksViewModel
import com.exercise.todo.ui.theme.Purple40

@Composable
fun HomeTabScreen(
    modifier: Modifier = Modifier,
    viewModel: TasksViewModel = hiltViewModel()
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("To Do", "Completed")
    val state by viewModel.tasksState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        TabRow(
            selectedTabIndex = tabIndex,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            containerColor = Purple40,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                    height = 2.dp,
                    color = Color.White,
                )
            }
        ) {

            tabs.forEachIndexed { index, title ->
                Tab(
                    text = {
                        Text(
                            text = title, color = Color.White,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                        )
                    },
                    selected = tabIndex == index,
                    onClick = {
                        tabIndex = index
                    }
                )
            }
        }
        when (tabIndex) {
            0 -> TasksScreen(viewModel = viewModel, state = state)
            1 -> CompletedTasksScreen(modifier = Modifier, viewModel = viewModel, state = state)
        }
    }

}