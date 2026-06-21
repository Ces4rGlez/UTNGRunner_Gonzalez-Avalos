package mx.utng.cfga.utngrunner.presentation.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import kotlinx.coroutines.delay
import mx.utng.cfga.utngrunner.presentation.domain.model.GamePhase

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val state by viewModel.state.collectAsState()
    var frameTick by remember { mutableLongStateOf(0L) }
    val focusRequester = remember { FocusRequester() }
    val hapticFeedback = LocalHapticFeedback.current

    // Ciclo local exclusivamente para refrescar efectos visuales (parpadeos)
    LaunchedEffect(state.phase) {
        while (state.phase == GamePhase.PLAYING) {
            delay(16L)
            frameTick++
        }
    }

    // Solicitar foco para capturar eventos físicos del reloj
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // Escucha de Haptics asíncronos lanzados por el ViewModel
    LaunchedEffect(Unit) {
        viewModel.hapticEvents.collect { event ->
            when (event) {
                HapticType.JUMP -> hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                HapticType.HIT -> hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .focusRequester(focusRequester)
            .onRotaryScrollEvent {
                // Invertimos la lógica o la simplificamos para que cualquier movimiento significativo salte
                if (kotlin.math.abs(it.verticalScrollPixels) > 1f) {
                    viewModel.onJump()
                }
                true
            }
            .focusable()
            .clickable { viewModel.onJump() },
        contentAlignment = Alignment.Center
    ) {
        // Lienzo principal de actualización gráfica
        Canvas(modifier = Modifier.fillMaxSize()) {
            GameRenderer.draw(this, state, frameTick)
        }

        // HUD Superior (Información vital y Score actual)
        Column(
            modifier = Modifier.fillMaxSize().padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "❤️ ${state.heartRate} BPM", color = Color(0xFFF87171), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(text = "💚 Vidas: ${state.lives}", color = Color(0xFF34D399), fontSize = 11.sp)
            }
            Text(
                text = "${state.score} PTS",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        // Overlays de flujo de interacción
        when (state.phase) {
            GamePhase.IDLE -> {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color(0xCC000000)).clickable { viewModel.startGame() },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "UTNG RUNNER", color = Color(0xFF0EA5E9), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Toca para correr", color = Color.Gray, fontSize = 11.sp)
                    }
                }
            }
            GamePhase.DEAD -> {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color(0xDD000000)).clickable { viewModel.startGame() },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "¡FIN DEL CUATRI!", color = Color(0xFFEF4444), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Score: ${state.score}", color = Color.White, fontSize = 13.sp)
                        Text(text = "Récord: ${state.highScore}", color = Color(0xFFFBBF24), fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Toca para reintentar", color = Color.LightGray, fontSize = 10.sp)
                    }
                }
            }
            else -> Unit
        }
    }
}