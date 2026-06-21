package mx.utng.cfga.utngrunner.presentation.game

import mx.utng.cfga.utngrunner.presentation.domain.model.*
import kotlin.random.Random

object GameEngine {

    fun update(state: GameState, frame: Long): GameState {
        if (state.phase != GamePhase.PLAYING) return state

        val updatedPlayer = updatePlayer(state.player)
        val newScore = state.score + 1
        val newLevel = (1 + newScore / 400).coerceAtMost(5)
        val newSpeed = 3.5f + newLevel * 0.5f

        val updatedObs = updateObstacles(state.obstacles, newSpeed, frame)
        val updatedCoins = updateCoins(state.coins, newSpeed, frame)

        val afterCollision = checkCollisions(updatedPlayer, updatedObs, updatedCoins, state.lives)

        return state.copy(
            player = afterCollision.player,
            score = newScore,
            level = newLevel,
            lives = afterCollision.lives,
            gameSpeed = newSpeed,
            obstacles = afterCollision.obstacles,
            coins = afterCollision.coins,
            phase = if (afterCollision.lives <= 0) GamePhase.DEAD else GamePhase.PLAYING
        )
    }

    private fun updatePlayer(p: Player): Player {
        val newVelY = p.velocityY + Player.GRAVITY
        val newY = (p.y + newVelY).coerceAtMost(Player.FLOOR_Y)
        val landed = newY >= Player.FLOOR_Y

        return p.copy(
            y = newY,
            velocityY = if (landed) 0f else newVelY,
            isJumping = !landed,
            isSliding = p.slideFrames > 0,
            slideFrames = (p.slideFrames - 1).coerceAtLeast(0),
            isInvincible = p.invincibleFrames > 0,
            invincibleFrames = (p.invincibleFrames - 1).coerceAtLeast(0)
        )
    }

    private fun updateObstacles(obstacles: List<Obstacle>, speed: Float, frame: Long): List<Obstacle> {
        val moved = obstacles.map { it.copy(x = it.x - speed) }.filter { it.x > -40f }

        // Spawn dinámico cada 75 frames si pasa la probabilidad
        return if (frame % 75 == 0L && Random.nextFloat() < 0.7f) {
            val type = ObstacleType.values().random()
            moved + Obstacle(x = 220f, width = type.w, height = type.h, type = type)
        } else moved
    }

    private fun updateCoins(coins: List<Coin>, speed: Float, frame: Long): List<Coin> {
        val moved = coins.map { it.copy(x = it.x - speed) }.filter { it.x > -30f }
        return if (frame % 120 == 0L && Random.nextFloat() < 0.5f) {
            moved + Coin(x = 220f, y = Player.FLOOR_Y - 40f)
        } else moved
    }

    private fun checkCollisions(player: Player, obstacles: List<Obstacle>, coins: List<Coin>, currentLives: Int): CollisionResult {
        var lives = currentLives
        val pLeft = player.x
        val pRight = player.x + 20f
        val pTop = player.y - (if (player.isSliding) 10f else 28f)
        val pBottom = player.y + 24f

        var hitObstacle = false
        val cleanObstacles = obstacles.map { o ->
            val oLeft = o.x
            val oRight = o.x + o.width
            val oTop = Player.FLOOR_Y + 24f - o.height
            val oBottom = Player.FLOOR_Y + 24f

            val collisionX = pRight > oLeft && pLeft < oRight
            val collisionY = pBottom > oTop && pTop < oBottom

            if (!player.isInvincible && collisionX && collisionY) {
                hitObstacle = true
                o.copy(x = -999f) // Remover de pantalla
            } else o
        }

        val updatedPlayer = if (hitObstacle) {
            lives = (lives - 1).coerceAtLeast(0)
            player.copy(invincibleFrames = Player.INVINCIBLE_FRAMES, isInvincible = true)
        } else player

        val updatedCoins = coins.map { c ->
            val distance = kotlin.math.hypot(player.x - c.x, player.y - c.y)
            if (!c.collected && distance < 25f) c.copy(collected = true) else c
        }

        return CollisionResult(updatedPlayer, lives, cleanObstacles, updatedCoins)
    }
}

data class CollisionResult(
    val player: Player,
    val lives: Int,
    val obstacles: List<Obstacle>,
    val coins: List<Coin>
)