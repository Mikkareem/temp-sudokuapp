package com.techullurgy.sudoku.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState

@Composable
fun GameOverDialogScreen(modifier: Modifier = Modifier) {
    val navigationEventState =
        rememberNavigationEventState(
            currentInfo = object : NavigationEventInfo() {},
        )

    NavigationBackHandler(
        state = navigationEventState,
        onBackCompleted = { /* No-Op on BackPress */ },
    )

    Box(
        modifier =
        modifier
            .fillMaxSize()
            .background(
                color = Color.Black.copy(alpha = 0.3f),
            )
            // To By-Pass the Main Section Clickables
            .clickable(
                onClick = {},
                indication = null,
                interactionSource = null,
            ),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides Color.White) {
            Box(
                modifier =
                Modifier
                    .background(Color.Black, RoundedCornerShape(25.dp))
                    .padding(40.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Game\nOver", autoSize = TextAutoSize.StepBased(maxFontSize = 40.sp))
                    Box(Modifier) {
                        Text("New Game")
                    }
                }
            }
        }
    }
}
