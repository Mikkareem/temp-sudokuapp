package com.techullurgy.games.sudoku.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techullurgy.games.sudoku.presentation.ui.container1Color
import com.techullurgy.games.sudoku.presentation.ui.container1ContentColor

@Composable
fun Container1(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier =
        modifier
            .drawBehind {
                drawRect(
                    color = Color.Black,
                    topLeft = Offset(10.dp.toPx(), 10.dp.toPx()),
                    size = size,
                )

                drawRect(
                    color = container1Color,
                )
            }.clickable(
                onClick = onClick,
            ).padding(16.dp),
    ) {
        Text(text, color = container1ContentColor, fontWeight = FontWeight.Bold)
    }
}
