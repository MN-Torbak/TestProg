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
                numberGenerated = UseCase().generateRandomNumber(1,3),
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
                        message = Message.SpecialMessage
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

    private fun buttonNumberForFirstGame (number: Int) {
        if (_gameState.value.previouslyClick == number-1) {
            _gameState.update { currentState ->
                currentState.copy(
                    previouslyClick = number,
                    message = when (number) {
                        1 -> Message.ButtonOnePressed
                        2 -> Message.ButtonTwoPressed
                        3 -> Message.ButtonThreePressed
                        else -> Message.Error
                    },
                    numberOfError = when (number) {
                        3 -> 0
                        else -> _gameState.value.numberOfError + 0
                    },
                    isFirstGameCompleted = when (number) {
                        3 -> true
                        else -> false
                    }
                )
            }
            when (number) {
                3 -> countdown(UseCase().generateRandomNumber(5,6))
            }
        } else {
            _gameState.update { currentState ->
                currentState.copy(
                    numberOfError = _gameState.value.numberOfError + 1,
                    message = Message.Error,
                )
            }
            if(_gameState.value.numberOfError == 3) {
                clickStartStopButton(STOP)
            }
        }
    }

    fun clickButtonNumber(number: Int) {
        if (_gameState.value.isStarted && !_gameState.value.isFirstGameCompleted) {
            buttonNumberForFirstGame(number)
        } else if (_gameState.value.isStarted && _gameState.value.isFirstGameCompleted) {
            if (_gameState.value.numberGenerated == number) {
                _gameState.update { currentState ->
                    currentState.copy(
                        isStarted = false,
                        isFirstGameCompleted = false,
                        previouslyClick = 0,
                        numberOfError = 0,
                        numberGenerated = 0,
                        message = Message.WellPlayed,
                    )
                }
            } else {
                _gameState.update { currentState ->
                    currentState.copy(
                        numberOfError = _gameState.value.numberOfError + 1,
                        message = Message.Error,
                    )
                }
                if(_gameState.value.numberOfError == 3) {
                    clickStartStopButton(STOP)
                }
            }
        }
    }

    fun clickStartStopButton(name: String) {
        if (name == STOP) {
            stop()
            _gameState.update { currentState ->
                currentState.copy(
                    isStarted = false,
                    isFirstGameCompleted = false,
                    previouslyClick = 0,
                    numberOfError = 0,
                    numberGenerated = 0,
                    message = Message.StopPressed,
                )
            }
        } else if (name == START) {
            _gameState.update { currentState ->
                currentState.copy(
                    isStarted = true,
                    isFirstGameCompleted = false,
                    previouslyClick = 0,
                    numberOfError = 0,
                    numberGenerated = 0,
                    message = Message.StartPressed,
                )
            }
        }
    }

    enum class Message{
        StartPressed,
        StopPressed,
        ButtonOnePressed,
        ButtonTwoPressed,
        ButtonThreePressed,
        Error,
        WellPlayed,
        SpecialMessage,
    }

    data class GameState(
        var isStarted: Boolean = false,
        var isFirstGameCompleted: Boolean = false,
        var previouslyClick: Int = 0,
        var numberOfError: Int = 0,
        var numberGenerated: Int = 0,
        var message: Message = Message.StopPressed,
    )

    companion object {
        private const val START = "Start"
        private const val STOP = "Stop"
    }
}