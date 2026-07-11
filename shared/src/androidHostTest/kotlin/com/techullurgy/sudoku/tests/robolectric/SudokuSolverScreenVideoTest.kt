package com.techullurgy.sudoku.tests.robolectric

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RoboVideoOptions
import com.github.takahirom.roborazzi.RoboVideoRecorderScope
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.RoborazziTaskType
import com.github.takahirom.roborazzi.recordScreenRoboVideo
import com.techullurgy.games.sudoku.common.test.utils.RobolectricTest
import com.techullurgy.games.sudoku.di.DefaultDispatcher
import com.techullurgy.games.sudoku.presentation.screens.SudokuSolverScreen
import com.techullurgy.games.sudoku.presentation.screens.SudokuSolverViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Rule
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel
import org.koin.test.KoinTestRule
import org.robolectric.annotation.GraphicsMode
import kotlin.test.Test

@OptIn(ExperimentalRoborazziApi::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class SudokuSolverScreenVideoTest: RobolectricTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val koinRule = KoinTestRule.create {
        modules(
            module {
                single<CoroutineDispatcher>(
                    qualifier = qualifier<DefaultDispatcher>()
                ) {
                    StandardTestDispatcher(mainDispatcher.scheduler)
                }
                viewModel<SudokuSolverViewModel>()
            }
        )
    }

    @Test
    fun `video test`() {
        composeTestRule.setContent {
            SudokuSolverScreen {  }
        }

        recordScreenRoboVideo(
            composeRule = composeTestRule,
            videoOptions = RoboVideoOptions(
                fps = 20,
            ),
            roborazziOptions = RoborazziOptions(
                taskType = RoborazziTaskType.Record
            )
        ) {
            with(composeTestRule) {
                putOnTheBoardCell(4, 2, 3)
                mainDispatcher.scheduler.advanceUntilIdle()
                delay(3000)
                putOnTheBoardCell(7, 6, 3)
                mainDispatcher.scheduler.advanceUntilIdle()
                delay(3000)
                onNodeWithText("Solve").assertExists().performClick()
                mainDispatcher.scheduler.advanceUntilIdle()
                delay(3000)
            }
        }
    }

    context(scope: RoboVideoRecorderScope)
    private fun ComposeContentTestRule.putOnTheBoardCell(
        row: Int,
        col: Int,
        number: Int,
        delay: Long = 2000
    ) {
        onNodeWithTag("BoardCell($row,$col)")
            .assertExists()
            .performClick()
        scope.delay(delay)
        onNodeWithTag("Number($number)")
            .assertExists()
            .performClick()
    }
}