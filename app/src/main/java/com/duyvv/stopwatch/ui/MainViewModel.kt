package com.duyvv.stopwatch.ui

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duyvv.stopwatch.domain.Stopwatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _stopwatch = MutableStateFlow(mutableListOf<Stopwatch>())
    val stopwatch = _stopwatch.asStateFlow()

    fun startStopwatch(
        stopwatch: Stopwatch,
        onStarted: () -> Unit
    ) {
        stopwatch.isRunning = true
        val startTime = SystemClock.elapsedRealtime() - stopwatch.time
        stopwatch.job = viewModelScope.launch(Dispatchers.Main) {
            while (stopwatch.isRunning) {
                delay(10)
                stopwatch.time = SystemClock.elapsedRealtime() - startTime
                onStarted.invoke()
            }
        }
    }

    fun stopStopwatch(stopwatch: Stopwatch) {
        stopwatch.isRunning = false
        stopwatch.job?.cancel()
        stopwatch.job = null
    }

    fun resetStopwatch(stopwatch: Stopwatch, onStopped: () -> Unit) {
        if (!stopwatch.isRunning) {
            stopwatch.time = 0
            stopwatch.job?.cancel()
            stopwatch.job = null
            onStopped.invoke()
        }
    }
}