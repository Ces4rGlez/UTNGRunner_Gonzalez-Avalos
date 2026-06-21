package mx.utng.cfga.utngrunner.presentation.data.repository

import mx.utng.cfga.utngrunner.presentation.data.datasource.PreferencesDataSource
import mx.utng.cfga.utngrunner.presentation.domain.repository.ScoreRepository

class ScoreRepositoryImpl(
    private val dataSource: PreferencesDataSource,
) : ScoreRepository {
    override suspend fun getHighScore(): Int = dataSource.getHighScore()
    override suspend fun saveHighScore(score: Int) = dataSource.saveHighScore(score)
}