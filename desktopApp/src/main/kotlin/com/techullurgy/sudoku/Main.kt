package com.techullurgy.sudoku

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val state =
        rememberWindowState(
            width = 480.dp,
            height = 920.dp,
        )
    Window(
        state = state,
        onCloseRequest = ::exitApplication,
        title = "SudokuApp",
    ) {
        App()
    }
}
