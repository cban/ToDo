package com.application.todo.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.application.todo.R
import com.application.todo.ui.home.tasks.CompletedTasksScreen
import com.application.todo.ui.home.tasks.TasksScreen
import com.application.todo.ui.home.weather.WeatherWidget
import com.application.todo.ui.theme.Purple40


@Composable
fun HomeTabScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val taskState by viewModel.tasksState.collectAsStateWithLifecycle()
    val weatherState by viewModel.weatherState.collectAsStateWithLifecycle()
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(stringResource(R.string.to_do), stringResource(R.string.completed))


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.getWeatherInfo()
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.location_permission_denied), Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            viewModel.getWeatherInfo()
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        WeatherWidget(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            weatherState = weatherState
        )

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
            0 -> TasksScreen(viewModel = viewModel, state = taskState)
            1 -> CompletedTasksScreen(modifier = Modifier, viewModel = viewModel, state = taskState)
        }
    }
}
