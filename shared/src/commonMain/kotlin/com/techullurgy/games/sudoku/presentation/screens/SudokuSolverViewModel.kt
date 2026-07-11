package com.techullurgy.games.sudoku.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.games.sudoku.di.DefaultDispatcher
import com.techullurgy.games.sudoku.game.Board
import com.techullurgy.games.sudoku.game.SudokuSolver
import com.techullurgy.games.sudoku.game.SudokuValidator
import com.techullurgy.games.sudoku.utils.getRelevantBoardIndicesFor
import com.techullurgy.games.sudoku.utils.toEnabledNumbers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import org.koin.core.annotation.Provided
import kotlin.time.Duration.Companion.milliseconds

@KoinViewModel
internal class SudokuSolverViewModel(
    @Provided @param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
): ViewModel() {
    private val solver = SudokuSolver()

    val state: StateFlow<SudokuSolverState> field = MutableStateFlow(SudokuSolverState())

    private var solveJob: Job? = null

    init {
        solver.boardState
            .debounce(200.milliseconds)
            .onEach {
                state.value = state.value.copy(
                    board = it,
                    emptyCells = solver.emptyCells,
                    enabledNumbers = it.toEnabledNumbers()
                )
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: SudokuSolverAction) {
        when(action) {
            SudokuSolverAction.OnCancelSolveClick -> onCancelSolve()
            is SudokuSolverAction.OnCellSelected -> onCellSelected(action)
            is SudokuSolverAction.OnNumberClick -> onNumberClick(action)
            SudokuSolverAction.OnSolveClick -> onSolveClick()
        }
    }

    private fun onNumberClick(action: SudokuSolverAction.OnNumberClick) {
        val selectedCell = state.value.selectedCell
        val selectedNumber = action.number

        val isFinished = state.value.isFinished

        if (!isFinished && solver.isEmptyCell(selectedCell)) {
            solver.setBoardAt(selectedCell, selectedNumber)
            checkSolverState()
        }
    }

    private fun onCellSelected(action: SudokuSolverAction.OnCellSelected) {
        state.value =
            state.value.copy(
                selectedCell = action.cell,
                selections = getRelevantBoardIndicesFor(action.cell),
            )
    }

    private fun onSolveClick() {
        solveJob?.cancel()
        solveJob = viewModelScope.launch(defaultDispatcher) {
            state.update {
                it.copy(isSolving = true)
            }
            solver.solve()
        }.also {
            it.invokeOnCompletion { e ->
                state.update {
                    it.copy(isSolving = false)
                }

                if(e == null) {
                    state.update {
                        it.copy(
                            solveReason = SolvedReason.Complete,
                        )
                    }
                } else if(e !is CancellationException) {
                    state.update {
                        it.copy(
                            board = List(9) { List(9) { 0 } },
                            enabledNumbers = setOf(1,2,3,4,5,6,7,8,9),
                        )
                    }
                } else {
                    state.update {
                        it.copy(
                            solveReason = SolvedReason.Cancel,
                            board = List(9) { List(9) { 0 } },
                            enabledNumbers = setOf(1,2,3,4,5,6,7,8,9),
                        )
                    }
                }
            }
        }
    }

    private fun onCancelSolve() {
        if(solveJob?.isActive == true) {
            solveJob!!.cancel()
        }
        solveJob = null
    }

    private fun checkSolverState() {
        state.update {
            it.copy(
                isBoardValid = SudokuValidator.isValid(solver.boardState.value)
            )
        }
    }
}

data class SudokuSolverState(
    val board: Board = emptyList(),
    val emptyCells: Set<Int> = emptySet(),
    val isBoardValid: Boolean = true,
    val selectedCell: Int = -1,
    val selections: Set<Int> = emptySet(),
    val enabledNumbers: Set<Int> = setOf(1,2,3,4,5,6,7,8,9),
    val solveReason: SolvedReason? = null,
    val isSolving: Boolean = false,
) {
    val isFinished: Boolean get() = solveReason != null
}

sealed interface SolvedReason {
    data object Complete: SolvedReason
    data object Cancel: SolvedReason
}

sealed interface SudokuSolverAction {
    data class OnCellSelected(val cell: Int) : SudokuSolverAction
    data class OnNumberClick(val number: Int) : SudokuSolverAction
    data object OnCancelSolveClick: SudokuSolverAction
    data object OnSolveClick: SudokuSolverAction
}