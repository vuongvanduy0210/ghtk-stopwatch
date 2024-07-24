package com.duyvv.stopwatch.utils

import android.annotation.SuppressLint
import java.util.concurrent.TimeUnit

@SuppressLint("DefaultLocale")
fun convertMillisToMMSS(millis: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
    val hundredths = (millis % 1000) / 10
    return String.format("%02d:%02d,%02d", minutes, seconds, hundredths)
}
