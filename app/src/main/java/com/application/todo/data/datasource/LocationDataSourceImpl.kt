package com.application.todo.data.datasource

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LocationDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationDataSource {
    override suspend fun getDeviceLocation(): Location? {
        return try {
            getLocation()
        } catch (error: Exception) {
            null
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLocation(): Location = suspendCoroutine {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val locationResult = fusedLocationProviderClient.lastLocation
        locationResult.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                it.resume(task.result)
            } else {
                it.resumeWithException(task.exception!!)
            }
        }
    }
}


