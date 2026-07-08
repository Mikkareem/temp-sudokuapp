package com.techullurgy.games.sudoku.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techullurgy.games.sudoku.game.GameLevel
import com.techullurgy.games.sudoku.presentation.components.Container2
import com.techullurgy.games.sudoku.presentation.components.NumberPad
import com.techullurgy.games.sudoku.presentation.components.SudokuBoard
import com.techullurgy.games.sudoku.presentation.viewmodels.FinishedReason
import com.techullurgy.games.sudoku.presentation.viewmodels.SudokuGameAction
import com.techullurgy.games.sudoku.presentation.viewmodels.SudokuGameViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
@Preview
internal fun SudokuGameScreen(
    gameLevel: GameLevel = GameLevel.EASY,
    onGameOver: () -> Unit = {},
    onGameWin: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val viewModel =
        koinViewModel<SudokuGameViewModel>(
            parameters = { parametersOf(gameLevel) },
        )

    val state by viewModel.state.collectAsStateWithLifecycle()

    val updatedOnGameOver by rememberUpdatedState(onGameOver)
    val updatedOnGameWin by rememberUpdatedState(onGameWin)

    LaunchedEffect(state.finishedReason) {
        state.finishedReason?.let {
            when (it) {
                FinishedReason.GameOver -> updatedOnGameOver()
                FinishedReason.Success -> updatedOnGameWin(state.timer)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(64.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Container2 {
                    Text(
                        text = "Mistakes: ${state.mistakeCount}/3",
                        fontSize = 18.sp,
                        color = Color.White,
                    )
                }

                Container2 {
                    Text(
                        text = state.timer,
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                    )
                }
            }
            SudokuBoard(
                board = state.board,
                emptyCells = state.emptyCells,
                selections = state.selections,
                selectedCell = state.selectedCell,
                onCellSelect = { viewModel.onAction(SudokuGameAction.OnCellSelected(it)) },
            )
            NumberPad(
                enabledNumbers = state.enabledNumbers,
                onNumberClick = { viewModel.onAction(SudokuGameAction.OnNumberClick(it)) },
            )
        }
    }
}
