package com.techullurgy.sudoku.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techullurgy.sudoku.presentation.ui.boardAccent

@OptIn(ExperimentalGridApi::class)
@Composable
internal fun NumberPad(enabledNumbers: Set<Int>, onNumberClick: (Int) -> Unit) {
    Grid(
        config = {
            repeat(9) {
                column(1f / 9)
            }
            row(70.dp)
            row(45.dp)
            rowGap(32.dp)
            columnGap(8.dp)
        },
        modifier =
        Modifier
            .fillMaxWidth(),
    ) {
        (1..9).forEach { number ->
            if (enabledNumbers.contains(number)) {
                Box(
                    Modifier
                        .testTag("Number($number)")
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(boardAccent)
                        .clickable(onClick = { onNumberClick(number) }),
                    Alignment.Center,
                ) {
                    Text(
                        text = number.toString(),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                    )
                }
            } else {
                Spacer(Modifier.fillMaxSize())
            }
        }

        Box(
            Modifier
                .testTag("NumberClear")
                .gridItem(
                    row = 2,
                    column = 3,
                    columnSpan = 5,
                ).fillMaxSize()
                .clip(CircleShape)
                .background(boardAccent)
                .clickable(onClick = { onNumberClick(0) }),
            Alignment.Center,
        ) {
            Text(
                text = "Clear",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
            )
        }
    }
}
