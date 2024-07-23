package com.duyvv.stopwatch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duyvv.stopwatch.domain.Stopwatch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _stopwatch = MutableStateFlow(mutableListOf<Stopwatch>())
    val stopwatch = _stopwatch.asStateFlow()

    fun startStopwatch(stopwatch: Stopwatch) {
        viewModelScope.launch {
            delay(1000)
            stopwatch.time += 1000
        }
    }
}