package mx.utng.cfga.utngrunner.presentation.game

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import mx.utng.cfga.utngrunner.presentation.domain.model.GamePhase
import mx.utng.cfga.utngrunner.presentation.domain.model.GameState
import mx.utng.cfga.utngrunner.presentation.domain.model.Player

object GameRenderer {

    fun draw(drawScope: DrawScope, state: GameState, frame: Long) {
        with(drawScope) {
            // 1. Fondo del Campus UTNG Nocturno
            drawRect(color = Color(0xFF0F172A), size = size)

            // 2. Línea de Suelo (Piso del campus)
            val floorLineY = Player.FLOOR_Y + 24f
            drawLine(
                color = Color(0xFF0284C7),
                start = Offset(0f, floorLineY),
                end = Offset(size.width, floorLineY),
                strokeWidth = 4f
            )

            // 3. Dibujar Créditos Académicos (Monedas)
            state.coins.filter { !it.collected }.forEach { coin ->
                drawCircle(
                    color = Color(0xFFEAB308),
                    radius = 6f,
                    center = Offset(coin.x, coin.y)
                )
            }

            // 4. Dibujar Obstáculos Académicos
            state.obstacles.forEach { obs ->
                drawRect(
                    color = when(obs.type.label) {
                        "TAREA" -> Color(0xFFEF4444)
                        "EXAMEN" -> Color(0xFFA855F7)
                        "BUG" -> Color(0xFFF97316)
                        else -> Color(0xFF64748B)
                    },
                    topLeft = Offset(obs.x, floorLineY - obs.height),
                    size = Size(obs.width.toFloat(), obs.height.toFloat())
                )
            }

            // 5. Dibujar al Ingeniero Corredor
            val player = state.player
            val isBlinking = player.isInvincible && (frame / 5) % 2 == 0L
            if (!isBlinking) {
                val pHeight = if (player.isSliding) 14f else 28f
                // Cuerpo del personaje
                drawRect(
                    color = Color(0xFF10B981),
                    topLeft = Offset(player.x, floorLineY - pHeight),
                    size = Size(20f, pHeight)
                )
                // Casco distintivo institucional
                drawRect(
                    color = Color(0xFF1E3A8A),
                    topLeft = Offset(player.x + 2f, floorLineY - pHeight - 6f),
                    size = Size(16f, 6f)
                )
            }

            // 6. HUD - Cabecera e Información del Reloj Circular
            drawCircle(
                color = Color(0xFFEF4444),
                radius = 4f,
                center = Offset(size.width / 2f - 40f, 25f)
            )
            // Indicador de Sensor de Pulso Real
            // Nota: El texto del HUD se gestiona mediante componentes Compose en el archivo Overlay para evitar problemas de fuentes nativas en DrawScope
        }
    }
}