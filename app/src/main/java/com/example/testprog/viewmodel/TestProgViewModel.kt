package com.example.testprog.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.testprog.usecase.UseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Timer
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TestProgViewModel : ViewModel() {

    private var time: Duration = Duration.ZERO
    private lateinit var timer: Timer

    var seconds by mutableStateOf("0")
    private var isPlaying by mutableStateOf(false)

    private var _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()


    private fun countdown(duration: Int) {
        _gameState.update { currentState ->
            currentState.copy(
                numberGenerated = UseCase.generateRandomNumberBetweenOneAndThree(),
            )
        }
        time = Duration.ZERO
        time = time.plus(duration.seconds)
        timer = fixedRateTimer(initialDelay = 0L, period = 1000L) {
            time = time.minus(1.seconds)
            updateTimeStates()
            if (time == Duration.ZERO) {
                stop()
                _gameState.update { currentState ->
                    currentState.copy(
                        message = "Press the ${_gameState.value.numberGenerated}",
                    )
                }
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
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        isPlaying = false
    }

    private fun stop() {
        pause()
        time = Duration.ZERO
        updateTimeStates()
    }

    fun clickButtonNumber(number: Int) {
        if (_gameState.value.isStarted && !_gameState.value.isFirstGameCompleted) {
            when (number) {
                1 -> {
                    if (_gameState.value.previouslyClick == 0) {
                        _gameState.update { currentState ->
                            currentState.copy(
                                previouslyClick = 1,
                                message = "Great 1/3",
                            )
                        }
                    } else {
                        _gameState.update { currentState ->
                            currentState.copy(
                                numberOfError = _gameState.value.numberOfError + 1,
                                message = "Error, after three errors, game end",
                            )
                        }
                        if(_gameState.value.numberOfError == 3) {
                            clickStartStopButton("Stop")
                        }
                    }
                }

                2 -> {
                    if (_gameState.value.previouslyClick == 1) {
                        _gameState.update { currentState ->
                            currentState.copy(
                                previouslyClick = 2,
                                message = "Great 2/3",
                            )
                        }
                    } else {
                        _gameState.update { currentState ->
                            currentState.copy(
                                numberOfError = _gameState.value.numberOfError + 1,
                                message = "Error, after three errors, game end",
                            )
                        }
                        if(_gameState.value.numberOfError == 3) {
                            clickStartStopButton("Stop")
                        }
                    }
                }

                3 -> {
                    if (_gameState.value.previouslyClick == 2) {
                        _gameState.update { currentState ->
                            currentState.copy(
                                isFirstGameCompleted = true,
                                message = "Great 3/3, New Game Begin",
                            )
                        }
                        countdown(UseCase.generateRandomNumberForCountdown())
                    } else {
                        _gameState.update { currentState ->
                            currentState.copy(
                                numberOfError = _gameState.value.numberOfError + 1,
                                message = "Error, after three errors, game end",
                            )
                        }
                        if(_gameState.value.numberOfError == 3) {
                            clickStartStopButton("Stop")
                        }
                    }
                }
            }
        } else if (_gameState.value.isStarted && _gameState.value.isFirstGameCompleted) {
            if (_gameState.value.numberGenerated == number) {
                _gameState.update { currentState ->
                    currentState.copy(
                        isStarted = false,
                        isFirstGameCompleted = false,
                        previouslyClick = 0,
                        numberOfError = 0,
                        numberGenerated = 0,
                        message = "Well Played !",
                    )
                }
            } else {
                _gameState.update { currentState ->
                    currentState.copy(
                        numberOfError = _gameState.value.numberOfError + 1,
                        message = "Error, after three errors, game end",
                    )
                }
                if(_gameState.value.numberOfError == 3) {
                    clickStartStopButton("Stop")
                }
            }
        }
    }

    fun clickStartStopButton(name: String) {
        if (name == "Stop") {
            stop()
            _gameState.update { currentState ->
                currentState.copy(
                    isStarted = false,
                    isFirstGameCompleted = false,
                    previouslyClick = 0,
                    numberOfError = 0,
                    numberGenerated = 0,
                    message = "Click Start",
                )
            }
        } else if (name == "Start") {
            _gameState.update { currentState ->
                currentState.copy(
                    isStarted = true,
                    isFirstGameCompleted = false,
                    previouslyClick = 0,
                    numberOfError = 0,
                    numberGenerated = 0,
                    message = "Click numbers in the correct order, 1 > 2 > 3",
                )
            }
        }
    }


    data class GameState(
        var isStarted: Boolean = false,
        var isFirstGameCompleted: Boolean = false,
        var previouslyClick: Int = 0,
        var numberOfError: Int = 0,
        var numberGenerated: Int = 0,
        var message: String = "Click Start",
    )
}