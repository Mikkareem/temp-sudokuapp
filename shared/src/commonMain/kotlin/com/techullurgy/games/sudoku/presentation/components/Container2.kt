package com.techullurgy.games.sudoku.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
internal fun Container2(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .decorateContainer2()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        content = { content() },
    )
}

private fun Modifier.decorateContainer2(): Modifier = drawBehind {
    drawRoundRect(
        color = Color.Black,
        cornerRadius = CornerRadius(25.dp.toPx()),
        topLeft = Offset(10.dp.toPx(), 10.dp.toPx()),
        size = size,
    )

    drawRoundRect(
        color = Color.DarkGray,
        cornerRadius = CornerRadius(25.dp.toPx()),
    )
    drawRoundRect(
        color = Color.White,
        cornerRadius = CornerRadius(25.dp.toPx()),
        style = Stroke(4.dp.toPx()),
    )
}
