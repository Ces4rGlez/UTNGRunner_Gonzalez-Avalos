package mx.utng.cfga.utngrunner.presentation.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.utng.cfga.utngrunner.presentation.game.GameScreen
import mx.utng.cfga.utngrunner.presentation.game.GameViewModel
import mx.utng.cfga.utngrunner.presentation.game.GameViewModelFactory

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: GameViewModel = viewModel(
                factory = GameViewModelFactory(applicationContext)
            )
            GameScreen(viewModel = vm)
        }
    }
}