package com.techullurgy.games.sudoku.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techullurgy.games.sudoku.presentation.components.Container2
import com.techullurgy.games.sudoku.presentation.components.NumberPad
import com.techullurgy.games.sudoku.presentation.components.SudokuBoard
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

@Composable
internal fun SudokuSolverScreen(
    modifier: Modifier = Modifier,
    onClose: () -> Unit
) {
    val viewModel = koinViewModel<SudokuSolverViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    val isSolvableBoard by remember {
        derivedStateOf {
            state.board.all { it.all { k -> k == 0 } }.not()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(64.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Container2(
                    modifier = Modifier.clickable(
                        onClick = onClose
                    )
                ) {
                    Text(
                        text = "Close",
                        fontSize = 18.sp,
                        color = Color.White,
                    )
                }

                AnimatedVisibility(
                    visible = isSolvableBoard
                ) {
                    Container2(
                        modifier = Modifier.clickable(
                            enabled = state.isBoardValid,
                            onClick = {
                                if(state.isSolving) {
                                    // Cancellable
                                    viewModel.onAction(SudokuSolverAction.OnCancelSolveClick)
                                } else {
                                    // Solvable
                                    viewModel.onAction(SudokuSolverAction.OnSolveClick)
                                }
                            }
                        )
                    ) {
                        Text(
                            text = if(state.isSolving) "Cancel" else "Solve",
                            fontSize = 18.sp,
                            color = Color.White,
                        )
                    }
                }
            }
            SudokuBoard(
                board = state.board,
                emptyCells = state.emptyCells,
                selections = state.selections,
                selectedCell = state.selectedCell,
                onCellSelect = { viewModel.onAction(SudokuSolverAction.OnCellSelected(it)) },
            )
            NumberPad(
                enabledNumbers = state.enabledNumbers,
                onNumberClick = { viewModel.onAction(SudokuSolverAction.OnNumberClick(it)) },
            )
        }
    }
}

@Preview
@Composable
private fun SudokuSolverScreenPreview() {
    KoinApplicationPreview(
        application = {
            modules(
                module { viewModel<SudokuSolverViewModel>() }
            )
        }
    ) {
        SudokuSolverScreen(
            onClose = {}
        )
    }
}