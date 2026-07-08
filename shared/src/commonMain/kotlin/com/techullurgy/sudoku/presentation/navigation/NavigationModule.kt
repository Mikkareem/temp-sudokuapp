package com.techullurgy.sudoku.presentation.navigation

import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.techullurgy.sudoku.game.GameLevel
import com.techullurgy.sudoku.presentation.screens.GameOverDialogScreen
import com.techullurgy.sudoku.presentation.screens.GameWonDialogScreen
import com.techullurgy.sudoku.presentation.screens.HomeScreen
import com.techullurgy.sudoku.presentation.screens.SudokuGameLevelSelectionScreen
import com.techullurgy.sudoku.presentation.screens.SudokuGameScreen
import com.techullurgy.sudoku.presentation.screens.SudokuSolverScreen
import kotlinx.serialization.Serializable
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import org.koin.plugin.module.dsl.single

@Serializable
internal data object Home : NavKey

@Serializable
internal data object SudokuSolver : NavKey

@Serializable
internal data object SudokuGameLevelSelection : NavKey

@Serializable
internal data class SudokuGame(val level: GameLevel) : NavKey

@Serializable
internal data object GameOverDialog : NavKey

@Serializable
internal data class GameWonDialog(val timer: String) : NavKey

class Navigator internal constructor(startDestination: NavKey) {
    val backStack = mutableStateListOf(startDestination)

    fun goTo(destination: NavKey) {
        backStack.add(destination)
    }

    fun goBack() {
        backStack.removeLastOrNull()
    }
}

@OptIn(KoinExperimentalAPI::class)
private val homeNavigationModule =
    module {
        navigation<Home> {
            val navigator: Navigator = get()
            HomeScreen(
                onGameClick = { navigator.goTo(SudokuGameLevelSelection) },
                onSolverClick = { navigator.goTo(SudokuSolver) },
            )
        }
    }

@OptIn(KoinExperimentalAPI::class)
private val sudokuSolverNavigationModule =
    module {
        navigation<SudokuSolver> {
            val navigator = get<Navigator>()

            SudokuSolverScreen(
                onClose = navigator::goBack
            )
        }
    }

@OptIn(KoinExperimentalAPI::class)
private val sudokuGameNavigationModule =
    module {
        navigation<SudokuGameLevelSelection> {
            val navigator = get<Navigator>()

            SudokuGameLevelSelectionScreen(
                onSelect = { navigator.goTo(SudokuGame(it)) },
            )
        }

        navigation<SudokuGame> {
            val navigator = get<Navigator>()

            SudokuGameScreen(
                it.level,
                onGameWin = { navigator.goTo(GameWonDialog(it)) },
                onGameOver = { navigator.goTo(GameOverDialog) },
            )
        }

        navigation<GameOverDialog>(
            metadata =
            NavDisplay.transitionSpec {
                scaleIn() togetherWith scaleOut()
            } + CustomOverlaySceneStrategy.customOverlay(),
        ) { GameOverDialogScreen() }

        navigation<GameWonDialog>(
            metadata =
            NavDisplay.transitionSpec {
                scaleIn() togetherWith scaleOut()
            } + CustomOverlaySceneStrategy.customOverlay(),
        ) { GameWonDialogScreen(it.timer) }
    }

@OptIn(KoinExperimentalAPI::class)
val navigationModule =
    module {
        includes(homeNavigationModule, sudokuSolverNavigationModule, sudokuGameNavigationModule)
        single<Navigator>()
    }
