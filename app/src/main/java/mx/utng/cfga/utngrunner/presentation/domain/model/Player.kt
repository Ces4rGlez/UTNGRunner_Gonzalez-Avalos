package mx.utng.cfga.utngrunner.presentation.domain.model

data class Player(
    val x: Float = 45f,
    val y: Float = FLOOR_Y,
    val velocityY: Float = 0f,
    val isJumping: Boolean = false,
    val isSliding: Boolean = false,
    val slideFrames: Int = 0,
    val isInvincible: Boolean = false,
    val invincibleFrames: Int = 0
) {
    companion object {
        const val FLOOR_Y = 150f
        const val JUMP_VELOCITY = -11f
        const val GRAVITY = 0.55f
        const val SLIDE_DURATION = 35
        const val INVINCIBLE_FRAMES = 75
    }
}

data class Obstacle(
    val x: Float,
    val width: Int,
    val height: Int,
    val type: ObstacleType
)

enum class ObstacleType(val label: String, val w: Int, val h: Int) {
    TAREA("TAREA", 20, 32),
    EXAMEN("EXAMEN", 16, 48),
    BUG("BUG", 24, 24),
    REPO("REPO", 32, 16)
}

data class Coin(
    val x: Float,
    val y: Float,
    val collected: Boolean = false
)