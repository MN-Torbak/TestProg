package com.example.testprog.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.Timer
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TestProgViewModel : ViewModel() {

    //COUNTDOWN TIMER

    private var time: Duration = Duration.ZERO
    private lateinit var timer: Timer

    var seconds by mutableStateOf("00")
    private var isPlaying by mutableStateOf(false)

    fun countdown(duration: Int) {
        time = Duration.ZERO
        time = time.plus(duration.seconds)
        timer = fixedRateTimer(initialDelay = 0L, period = 1000L) {
            time = time.minus(1.seconds)
            updateTimeStates()
            if (time == Duration.ZERO) {
                stop()
            }
        }
        isPlaying = true
    }

    private fun updateTimeStates() {
        time.toComponents { _, _, seconds, _ ->
            this.seconds = seconds.toString()
        }
    }

    private fun pause() {
        if(this::timer.isInitialized) {
            timer.cancel()
        }
        isPlaying = false
    }

    private fun stop() {
        pause()
        time = Duration.ZERO
        updateTimeStates()
    }


}