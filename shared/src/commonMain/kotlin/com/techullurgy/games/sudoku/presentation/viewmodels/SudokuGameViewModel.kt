package com.techullurgy.games.sudoku.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.games.sudoku.game.Board
import com.techullurgy.games.sudoku.game.GameLevel
import com.techullurgy.games.sudoku.game.GameProviderFactory
import com.techullurgy.games.sudoku.game.SudokuValidator
import com.techullurgy.games.sudoku.utils.getRelevantBoardIndicesFor
import com.techullurgy.games.sudoku.utils.toEnabledNumbers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import org.koin.core.annotation.Qualifier
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

@Qualifier
annotation class InitialGameBoard

@KoinViewModel
internal class SudokuGameViewModel(@InjectedParam level: GameLevel, @InitialGameBoard initialBoard: String? = null) :
    ViewModel() {
    private val game =
        GameProviderFactory(level, viewModelScope).run {
            if (initialBoard != null) {
                initWithInitialBoard(initialBoard)
            } else {
                generateDefault()
            }
        }

    private val tickerFlow =
        flow {
            val currentTime = { Clock.System.now().toEpochMilliseconds() }
            val startTime = currentTime()
            while (true) {
                delay(200.milliseconds)
                emit(currentTime() - startTime)
            }
        }.map { elapsedTime ->
            val elapsedSeconds = (elapsedTime / 1000).coerceAtLeast(0)

            val hours = elapsedSeconds / 3600
            val minutes = (elapsedSeconds % 3600) / 60
            val seconds = elapsedSeconds % 60

            if (hours > 0) {
                "${hours.toString().padStart(
                    2,
                    '0',
                )}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
            } else {
                "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
            }
        }

    val state: StateFlow<SudokuGameState> field = MutableStateFlow(SudokuGameState(emptyCells = game.emptyCells))

    private var tickerJob: Job? = null

    init {
        game.boardState
            .debounce(200.milliseconds)
            .onEach {
                state.value =
                    state.value.copy(
                        board = it,
                        enabledNumbers = it.toEnabledNumbers(),
                    )
            }.launchIn(viewModelScope)

        initTickerJob()
    }

    fun onAction(action: SudokuGameAction) {
        when (action) {
            is SudokuGameAction.OnCellSelected -> onCellSelected(action)
            is SudokuGameAction.OnNumberClick -> onNumberClick(action)
        }
    }

    private fun onCellSelected(action: SudokuGameAction.OnCellSelected) {
        state.value =
            state.value.copy(
                selectedCell = action.cell,
                selections = getRelevantBoardIndicesFor(action.cell),
            )
    }

    private fun onNumberClick(action: SudokuGameAction.OnNumberClick) {
        val selectedCell = state.value.selectedCell
        val selectedNumber = action.number

        val isFinished = state.value.isFinished

        if (!isFinished && game.isEmptyCell(selectedCell)) {
            game.setBoardAt(selectedCell, selectedNumber)
            checkGameState()
        }
    }

    private fun checkGameState() {
        if (!SudokuValidator.isValid(game.boardState.value)) {
            val newMistakeCount = state.value.mistakeCount + 1
            val newFinishedReason = if (newMistakeCount == 3) FinishedReason.GameOver else null
            state.value =
                state.value.copy(
                    mistakeCount = newMistakeCount,
                    finishedReason = newFinishedReason,
                )
            if (newFinishedReason != null) {
                cancelTickerJob()
            }
            return
        }

        if (game.isGridSuccessfulDone()) {
            state.value =
                state.value
                    .copy(
                        finishedReason = FinishedReason.Success,
                    ).also { cancelTickerJob() }
        }
    }

    private fun initTickerJob() {
        tickerJob?.cancel()
        tickerJob =
            tickerFlow
                .onEach {
                    state.value =
                        state.value.copy(
                            timer = it,
                        )
                }.launchIn(viewModelScope)
    }

    private fun cancelTickerJob() {
        tickerJob?.cancel()
        tickerJob = null
    }
}

data class SudokuGameState(
    val board: Board = emptyList(),
    val emptyCells: Set<Int> = emptySet(),
    val selectedCell: Int = -1,
    val selections: Set<Int> = emptySet(),
    val enabledNumbers: Set<Int> = setOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
    val finishedReason: FinishedReason? = null,
    val mistakeCount: Int = 0,
    val timer: String = "00:00",
) {
    val isFinished: Boolean get() = finishedReason != null
}

sealed interface FinishedReason {
    data object Success : FinishedReason

    data object GameOver : FinishedReason
}

sealed interface SudokuGameAction {
    data class OnCellSelected(val cell: Int) : SudokuGameAction

    data class OnNumberClick(val number: Int) : SudokuGameAction
}
