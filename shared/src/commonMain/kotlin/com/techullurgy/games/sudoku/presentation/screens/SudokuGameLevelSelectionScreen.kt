package com.techullurgy.games.sudoku.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.layout.FlexAlignItems
import androidx.compose.foundation.layout.FlexBox
import androidx.compose.foundation.layout.FlexDirection
import androidx.compose.foundation.layout.FlexJustifyContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techullurgy.games.sudoku.game.GameLevel
import com.techullurgy.games.sudoku.presentation.components.Container1
import org.jetbrains.compose.resources.Font
import sudokuapp.shared.generated.resources.Orbitron_ExtraBold
import sudokuapp.shared.generated.resources.Res

@OptIn(ExperimentalFlexBoxApi::class)
@Composable
fun SudokuGameLevelSelectionScreen(onSelect: (GameLevel) -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier =
        modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Think. Solve. Win.",
                style = LocalTextStyle.current.copy(
                    fontFamily = FontFamily(
                        Font(Res.font.Orbitron_ExtraBold)
                    ),
                    color = Color.White,
                    fontSize = 64.sp
                )
            )
            FlexBox(
                config = {
                    direction(FlexDirection.Column)
                    justifyContent(FlexJustifyContent.Center)
                    alignItems(FlexAlignItems.Center)
                    gap(64.dp)
                },
            ) {
                Container1(
                    text = "Easy",
                    onClick = { onSelect(GameLevel.EASY) },
                )

                Container1(
                    text = "Medium",
                    onClick = { onSelect(GameLevel.MEDIUM) },
                )

                Container1(
                    text = "Hard",
                    onClick = { onSelect(GameLevel.HARD) },
                )
            }
        }
    }
}
