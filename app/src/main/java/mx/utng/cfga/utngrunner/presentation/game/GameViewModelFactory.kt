package mx.utng.cfga.utngrunner.presentation.game

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.health.services.client.HealthServices
import mx.utng.cfga.utngrunner.presentation.data.datasource.PreferencesDataSource
import mx.utng.cfga.utngrunner.presentation.data.health.HeartRateDataSource
import mx.utng.cfga.utngrunner.presentation.data.repository.ScoreRepositoryImpl
import mx.utng.cfga.utngrunner.presentation.domain.usecase.GetHighScoreUseCase
import mx.utng.cfga.utngrunner.presentation.domain.usecase.SaveHighScoreUseCase

class GameViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val healthClient = HealthServices.getClient(context)
        val heartRateDs = HeartRateDataSource(healthClient)
        val prefsDs = PreferencesDataSource(context)
        val repo = ScoreRepositoryImpl(prefsDs)

        return GameViewModel(
            getHighScore = GetHighScoreUseCase(repo),
            saveHighScore = SaveHighScoreUseCase(repo),
            heartRateSource = heartRateDs
        ) as T
    }
}