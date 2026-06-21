package mx.utng.cfga.utngrunner.presentation.domain.repository

interface ScoreRepository {
    suspend fun getHighScore(): Int
    suspend fun saveHighScore(score: Int)
}