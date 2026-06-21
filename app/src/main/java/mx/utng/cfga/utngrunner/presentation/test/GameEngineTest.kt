package mx.utng.cfga.utngrunner.presentation.test

import mx.utng.cfga.utngrunner.presentation.domain.model.*
import mx.utng.cfga.utngrunner.presentation.game.GameEngine
import org.junit.Assert.*
import org.junit.Test

class GameEngineTest {

    @Test
    fun `player falls due to gravity constraints`() {
        val state = GameState(phase = GamePhase.PLAYING, player = Player(y = 100f, velocityY = 0f))
        val nextState = GameEngine.update(state, frame = 1)
        assertTrue(nextState.player.y > 100f)
    }

    @Test
    fun `score increases dynamically every single frame`() {
        val state = GameState(phase = GamePhase.PLAYING, score = 10)
        val nextState = GameEngine.update(state, frame = 1)
        assertEquals(11, nextState.score)
    }

    @Test
    fun `level scaling caps and updates properly`() {
        val state = GameState(phase = GamePhase.PLAYING, score = 399)
        val nextState = GameEngine.update(state, frame = 1)
        assertEquals(2, nextState.level)
    }

    @Test
    fun `game transitions to dead phase when lives are depleted`() {
        val state = GameState(phase = GamePhase.PLAYING, lives = 1)
        val criticalObstacle = Obstacle(x = Player().x, width = 20, height = 30, type = ObstacleType.TAREA)
        val crashState = state.copy(obstacles = listOf(criticalObstacle))

        val result = GameEngine.update(crashState, frame = 75)
        assertEquals(GamePhase.DEAD, result.phase)
    }
}