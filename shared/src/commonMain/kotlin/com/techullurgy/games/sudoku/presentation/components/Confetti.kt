package com.techullurgy.games.sudoku.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private val ConfettiColors =
    listOf(
        Color(168, 100, 253),
        Color(41, 205, 255),
        Color(120, 255, 68),
        Color(255, 113, 141),
        Color(253, 255, 106),
    )

@Composable
fun Confetti(count: Int, modifier: Modifier = Modifier) {
    val particles = remember(count) { List(count) { ConfettiParticle() } }

    LaunchedEffect(particles) {
        while (particles.any { it.isPending() }) {
            withFrameMillis {
                particles.forEach { it.update() }
            }
        }
    }

    Box(
        modifier =
        modifier
            .drawBehind {
                particles.forEach { it.draw() }
            },
    )
}

@Stable
private class ConfettiParticle {
    private var position by mutableStateOf(Offset(0f, 0f))

    private var progress = 0f
    private var progressJob: Job? = null

    private val angleInRadians = Random.nextFloat() * (2 * PI).toFloat()
    private val distance = Random.nextFloat() * 30f + 40f
    private val rotation = Random.nextFloat() * 360f

    private val direction get() = Offset(cos(angleInRadians), sin(angleInRadians))

    context(scope: CoroutineScope)
    fun update() {
        if (progressJob == null) {
            progressJob =
                scope.launch {
                    animate(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = tween(300, easing = FastOutSlowInEasing),
                    ) { value, _ ->
                        progress = value
                    }
                }
        }

        if (progressJob!!.isActive) {
            position += (direction * distance * progress)
        }
    }

    context(scope: DrawScope)
    fun draw() {
        with(scope) {
            withTransform(
                transformBlock = {
                    translate(position.x, position.y)
                    rotate(rotation)
                },
            ) {
                drawRect(
                    color = ConfettiColors.random(),
                    size = Size(12.dp.toPx(), 5.dp.toPx()),
                )
            }
        }
    }

    fun isPending(): Boolean = progressJob == null || progressJob!!.isActive
}

@Composable
@Preview
private fun ConfettiPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Confetti(20)
    }
}
