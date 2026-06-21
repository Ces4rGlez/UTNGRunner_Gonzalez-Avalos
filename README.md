# UTNG Runner 🏃‍♂️🎓

**UTNG Runner** es un videojuego estilo "infinite runner" desarrollado específicamente para dispositivos **Wear OS**. El juego está diseñado para ser jugado en relojes inteligentes, aprovechando tanto la pantalla táctil como los controles físicos (corona giratoria).

## 📝 Descripción
En este juego, asumes el papel de un estudiante de la **UTNG** que debe sobrevivir al cuatrimestre. Tu objetivo es correr la mayor distancia posible esquivando obstáculos académicos como "Tareas", "Exámenes" y "Bugs", mientras recolectas créditos académicos (monedas) y monitoreas tu ritmo cardíaco en tiempo real.

## ✨ Características
- **Optimizado para Wear OS**: Interfaz circular diseñada para aprovechar el espacio de la pantalla del reloj.
- **Integración de Sensores**: Muestra tu frecuencia cardíaca (BPM) real durante el juego.
- **Dificultad Progresiva**: La velocidad del juego aumenta cada 400 puntos (hasta el nivel 5).
- **Controles Duales**: Juega usando la pantalla táctil o la corona física del reloj.
- **Persistencia**: Guarda automáticamente tu récord más alto (High Score) usando DataStore.
- **Feedback Háptico**: Vibración al saltar o al recibir daño.

## 🎮 Controles

### En el Reloj:
*   **Pantalla (Tap)**: Saltar.
*   **Corona (Girar)**:
    *   Hacia Arriba: Saltar.
    *   Hacia Abajo: Deslizarse (Slide).

### En el Emulador (Android Studio):
*   **Clic Izquierdo**: Saltar / Iniciar Juego.
*   **Rueda del Ratón**:
    *   Scroll Arriba: Saltar.
    *   Scroll Abajo: Deslizarse.

## 🚀 Tecnologías Utilizadas
- **Kotlin**: Lenguaje principal.
- **Jetpack Compose Wear OS**: Para la interfaz de usuario moderna y declarativa.
- **Health Services**: Para el monitoreo de frecuencia cardíaca.
- **DataStore**: Para guardar los récords de puntuación.
- **Coroutines**: Para el motor del juego y actualizaciones a 60 FPS.

## 🛠️ Instalación y Compilación
1. Clona este repositorio.
2. Abre el proyecto en **Android Studio (Koala o superior)**.
3. Asegúrate de tener instalado el SDK de Android 35 o superior.
4. Conecta un reloj Wear OS o usa un emulador de Wear OS.
5. Ejecuta la tarea: `./gradlew installDebug`.

---
*Desarrollado como proyecto de práctica para Wear OS - UTNG.-- González Avalos César Fernando*
