package com.techullurgy.sudoku.tests.robolectric

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import com.techullurgy.sudoku.common.test.utils.RobolectricTest
import com.techullurgy.sudoku.presentation.screens.SudokuSolverScreen
import com.techullurgy.sudoku.presentation.screens.SudokuSolverViewModel
import com.techullurgy.sudoku.utils.col
import com.techullurgy.sudoku.utils.row
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel
import org.koin.test.KoinTestRule
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class, ExperimentalCoroutinesApi::class)
class SudokuSolverScreenTest: RobolectricTest() {

    @get:Rule
    val koinRule = KoinTestRule.create {
        modules(
            module {
                viewModel<SudokuSolverViewModel>()
            }
        )
    }

    @Test
    fun `Sudoku Solver screen, should load without any error`() = runComposeUiTest {
        setContent {
            SudokuSolverScreen(
                onClose = {}
            )
        }

        runOnIdle {
            onNodeWithText("Close").assertIsDisplayed()
        }
    }

    @Test
    fun `Sudoku Solver screen, onClose should be called if we click Close Button`() = runComposeUiTest {
        var isCloseCalled = false

        setContent {
            SudokuSolverScreen(
                onClose = { isCloseCalled = true }
            )
        }

        runOnIdle {
            onNodeWithText("Close").assertIsDisplayed().performClick()
            waitForIdle()
            isCloseCalled shouldBe true
        }
    }

    @Test
    fun `Sudoku Solver screen, initial board should initialized with zeros only`() = runComposeUiTest {
        setContent {
            SudokuSolverScreen(
                onClose = {}
            )
        }

        runOnIdle {
            repeat(81) {
                val row = it.row
                val col = it.col

                onNodeWithTag("BoardCell($row,$col)")
                    .assertExists()
                    // Assert(No TEXT Property Set)
                    .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.Text))
            }
        }
    }

    @Test
    fun `Sudoku Solver screen, initially, all numbers should show in the number pad`() = runComposeUiTest {
        setContent {
            SudokuSolverScreen(
                onClose = {}
            )
        }

        mainDispatcher.scheduler.advanceTimeBy(199)

        runOnIdle {
            repeat(9) {
                val number = it + 1
                onNodeWithTag("Number($number)")
                    .assertExists()
                    .assertTextEquals("$number")
            }
        }

        mainDispatcher.scheduler.advanceTimeBy(2)

        runOnIdle {
            repeat(9) {
                val number = it + 1
                onNodeWithTag("Number($number)")
                    .assertExists()
            }
        }
    }

    @Test
    fun `Sudoku Solver screen, Number should remove from the NumberPad, when the board contains enough number of Number`() = runComposeUiTest {
        setContent {
            SudokuSolverScreen {  }
        }

        val number = 5

        runOnIdle {
            putOnTheBoardCell(0, 1, number)
            putOnTheBoardCell(1, 3, number)
            putOnTheBoardCell(2, 6, number)
            putOnTheBoardCell(3, 0, number)
            putOnTheBoardCell(4, 5, number)
            putOnTheBoardCell(5, 8, number)
            putOnTheBoardCell(6, 4, number)
            putOnTheBoardCell(7, 7, number)
        }

        mainDispatcher.scheduler.advanceTimeBy(201)

        runOnIdle {
            onNodeWithTag("Number($number)")
                .assertExists()
            putOnTheBoardCell(8, 2, number)
        }

        mainDispatcher.scheduler.advanceTimeBy(201)

        runOnIdle {
            onNodeWithTag("Number($number)")
                .assertDoesNotExist()
        }
    }

    @Test
    fun `Sudoku Solver screen, board should be solvable when we have at least one valid number in the board`() = runComposeUiTest {
        setContent {
            SudokuSolverScreen {  }
        }

        runOnIdle {
            onNodeWithText("Solve").assertIsNotDisplayed()

            putOnTheBoardCell(2, 6, 5)
        }

        mainDispatcher.scheduler.advanceTimeBy(201)

        runOnIdle {
            onNodeWithText("Solve").assertIsDisplayed()
        }
    }

    private fun ComposeUiTest.putOnTheBoardCell(row: Int, col: Int, number: Int) {
        onNodeWithTag("BoardCell($row,$col)")
            .assertExists()
            .performClick()
        onNodeWithTag("Number($number)")
            .assertExists()
            .performClick()
    }
}