package mx.utng.cfga.utngrunner.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.cfga.utngrunner.presentation.data.health.HeartRateDataSource
import mx.utng.cfga.utngrunner.presentation.domain.model.GamePhase
import mx.utng.cfga.utngrunner.presentation.domain.model.GameState
import mx.utng.cfga.utngrunner.presentation.domain.model.Player
import mx.utng.cfga.utngrunner.presentation.domain.usecase.GetHighScoreUseCase
import mx.utng.cfga.utngrunner.presentation.domain.usecase.SaveHighScoreUseCase

enum class HapticType { JUMP, HIT }

class GameViewModel(
    private val getHighScore: GetHighScoreUseCase,
    private val saveHighScore: SaveHighScoreUseCase,
    private val heartRateSource: HeartRateDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    private val _hapticEvents = Channel<HapticType>(Channel.BUFFERED)
    val hapticEvents = _hapticEvents.receiveAsFlow()

    private var gameFrame = 0L
    private var gameJob: Job? = null

    init {
        loadHighScore()
        observeHeartRate()
        heartRateSource.startMonitoring()
    }

    fun startGame() {
        gameJob?.cancel()
        _state.value = GameState(phase = GamePhase.PLAYING, highScore = _state.value.highScore)
        gameFrame = 0L

        gameJob = viewModelScope.launch {
            // Ciclo estable a 60fps (~16.6ms por tick)
            while (_state.value.phase == GamePhase.PLAYING) {
                delay(16L)
                _state.update { GameEngine.update(it, gameFrame++) }
            }
            if (_state.value.phase == GamePhase.DEAD) {
                saveHighScore(_state.value.score)
                _hapticEvents.trySend(HapticType.HIT)
            }
        }
    }

    fun onJump() {
        val current = _state.value
        if (current.phase == GamePhase.IDLE || current.phase == GamePhase.DEAD) {
            startGame()
            return
        }

        if (!current.player.isJumping && !current.player.isSliding) {
            _state.update {
                it.copy(player = it.player.copy(velocityY = Player.JUMP_VELOCITY, isJumping = true))
            }
            _hapticEvents.trySend(HapticType.JUMP)
        }
    }

    fun onSlide() {
        val current = _state.value
        if (current.phase == GamePhase.PLAYING && !current.player.isJumping) {
            _state.update {
                it.copy(player = it.player.copy(slideFrames = Player.SLIDE_DURATION, isSliding = true))
            }
        }
    }

    private fun loadHighScore() {
        viewModelScope.launch {
            val hs = getHighScore()
            _state.update { it.copy(highScore = hs) }
        }
    }

    private fun observeHeartRate() {
        viewModelScope.launch {
            heartRateSource.heartRate.collect { bpm ->
                _state.update { it.copy(heartRate = bpm) }
            }
        }
    }

    override fun onCleared() {
        gameJob?.cancel()
        super.onCleared()
    }
}