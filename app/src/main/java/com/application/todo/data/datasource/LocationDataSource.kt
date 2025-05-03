package com.application.todo.data.datasource

import android.location.Location

interface LocationDataSource {
   suspend fun getDeviceLocation(): Location?
}