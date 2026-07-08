package com.techullurgy.sudoku.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techullurgy.sudoku.presentation.ui.bgColor

private const val TRUCHET_TILESIZE = 80f

@Preview
@Composable
internal fun TruchetTiles() {
    val tiles = remember { mutableStateListOf<Tile>() }

//    LaunchedEffect(Unit) {
//        while (true) {
//            withFrameMillis {
//                tiles.forEach {
//                    it.update()
//                }
//            }
//        }
//    }

    Canvas(Modifier.fillMaxSize().background(bgColor)) {
        if (tiles.isEmpty()) {
            val tileSize = Size(TRUCHET_TILESIZE, TRUCHET_TILESIZE)
            val list = mutableListOf<Tile>()
            val rows = (size.width / tileSize.width).toInt() + 1
            val cols = (size.height / tileSize.height).toInt() + 1

            (0..<rows).forEach { row ->
                (0..<cols).forEach { col ->
                    val x = row * tileSize.width
                    val y = col * tileSize.height
                    list.add(Tile(x, y, tileSize))
                }
            }

            tiles.addAll(list)
        }

        tiles.forEach { it.draw() }
    }
}

private class Tile(private val x: Float, private val y: Float, private val tileSize: Size) {
    private val type: RotationType = RotationType.entries.random()

    private var rotation by mutableFloatStateOf(0f)

    context(scope: DrawScope)
    fun draw() {
        with(scope) {
            withTransform({
                translate(x, y)
                rotate(rotation, pivot = tileSize.center)
            }) {
                drawRect(
                    color = bgColor,
                    size = tileSize,
                )

                when (type) {
                    RotationType.Type1 -> {
                        arcBetween(center = Offset(-tileSize.width / 2, -tileSize.height / 2), 0f, 90f)
                        arcBetween(center = Offset(tileSize.width / 2, tileSize.height / 2), 180f, 90f)
                    }

                    RotationType.Type2 -> {
                        arcBetween(center = Offset(-tileSize.width / 2, tileSize.height / 2), 0f, -90f)
                        arcBetween(center = Offset(tileSize.width / 2, -tileSize.height / 2), 180f, -90f)
                    }
                }
            }
        }
    }

    context(scope: DrawScope)
    private fun arcBetween(center: Offset, startAngle: Float, sweepAngle: Float) {
        with(scope) {
            translate(center.x, center.y) {
                drawArc(
                    color = Color.Magenta,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    size = tileSize,
                    useCenter = false,
                    style = Stroke(width = 3.dp.toPx()),
                )
            }
        }
    }
}

private enum class RotationType {
    Type1,
    Type2,
}
