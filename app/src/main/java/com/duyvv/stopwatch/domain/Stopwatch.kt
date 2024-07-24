package com.duyvv.stopwatch.domain

import kotlinx.coroutines.Job

data class Stopwatch(
    var time: Long,
    var isRunning: Boolean,
    var job: Job? = null
)
