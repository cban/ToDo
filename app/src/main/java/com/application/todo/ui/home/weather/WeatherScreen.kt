package com.application.todo.ui.home.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.application.todo.R
import com.application.todo.data.model.CurrentWeather
import com.application.todo.ui.home.UiState
import com.application.todo.ui.home.tasks.LoadingIndicator

@Composable
fun WeatherWidget(
    modifier: Modifier = Modifier,
    weatherState: UiState<CurrentWeather>?
) {
    when (weatherState) {
        is UiState.Loading -> {
            LoadingIndicator()
        }

        is UiState.Success -> {
            WeatherContent(
                modifier = modifier,
                weather = weatherState.data
            )
        }

        is UiState.Error -> {
            Text(text = weatherState.message)
        }

        UiState.Idle -> {
            Text(stringResource(R.string.no_weather_data_available))
        }

        else -> {
            Text(stringResource(R.string.no_weather_data_available))
        }
    }
}

@Composable
fun WeatherContent(
    modifier: Modifier = Modifier,
    weather: CurrentWeather,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(148.dp)
                    .padding(8.dp),
                model = "https:${weather.icon}",
                contentDescription = stringResource(R.string.weather_icon_desc),
            )

            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    text = stringResource(R.string.current_temperature_c),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    text = stringResource(R.string.temperature_c, weather.temperature),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {

            Text(
                stringResource(R.string.sunrise_time, weather.sunriseTime),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                stringResource(R.string.sunset_time, weather.sunsetTime),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}