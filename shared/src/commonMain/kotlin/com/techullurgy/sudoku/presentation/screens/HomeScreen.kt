package com.techullurgy.sudoku.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.layout.FlexAlignItems
import androidx.compose.foundation.layout.FlexBox
import androidx.compose.foundation.layout.FlexDirection
import androidx.compose.foundation.layout.FlexJustifyContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techullurgy.sudoku.presentation.components.Container1

@OptIn(ExperimentalFlexBoxApi::class)
@Composable
fun HomeScreen(onGameClick: () -> Unit, onSolverClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier =
        modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        FlexBox(
            config = {
                direction(FlexDirection.Column)
                justifyContent(FlexJustifyContent.Center)
                alignItems(FlexAlignItems.Center)
                gap(64.dp)
            },
        ) {
            Container1(
                text = "Sudoku Game",
                onClick = onGameClick,
            )

            Container1(
                text = "Sudoku Solver",
                onClick = onSolverClick,
            )
        }
    }
}
