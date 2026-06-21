package mx.utng.cfga.utngrunner.presentation.domain.usecase

import mx.utng.cfga.utngrunner.presentation.domain.repository.ScoreRepository

class GetHighScoreUseCase(private val repository: ScoreRepository) {
    suspend operator fun invoke(): Int = repository.getHighScore()
}

class SaveHighScoreUseCase(private val repository: ScoreRepository) {
    suspend operator fun invoke(score: Int) {
        val current = repository.getHighScore()
        if (score > current) {
            repository.saveHighScore(score)
        }
    }
}