package com.techullurgy.sudoku

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import com.techullurgy.sudoku.presentation.navigation.Home
import com.techullurgy.sudoku.presentation.navigation.Navigator
import com.techullurgy.sudoku.presentation.navigation.SudokuGameLevelSelection
import com.techullurgy.sudoku.presentation.screens.SudokuGameScreen
import com.techullurgy.sudoku.presentation.viewmodels.InitialGameBoard
import com.techullurgy.sudoku.presentation.viewmodels.SudokuGameViewModel
import org.junit.After
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.qualifier
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import org.koin.plugin.module.dsl.viewModel
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class, KoinExperimentalAPI::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ApplicationTest {
    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `default Home screen loaded via KoinApplication`() = runComposeUiTest {
        setContent {
            App()
        }

        runOnIdle {
            onNodeWithText("Sudoku Game").assertIsDisplayed()
        }
    }

    @Test
    fun `overrided Home screen should override the default Home Screen`() = runComposeUiTest {
        setContent {
            App(
                config =
                koinConfiguration {
                    val newNavigationModule =
                        module {
                            navigation<Home> {
                                Text("BASIC NAVIGATION HOME APP")
                            }
                        }
                    modules(newNavigationModule)
                },
            )
        }

        runOnIdle {
            onNodeWithText("BASIC NAVIGATION HOME APP").assertExists()
        }
    }

    @Test
    fun `overrided Home screen should navigate to the default Navigation Paths`() = runComposeUiTest {
        setContent {
            App(
                config =
                koinConfiguration {
                    val homeNavigationModule =
                        module {
                            navigation<Home> {
                                val navigator = get<Navigator>()
                                Button(
                                    onClick = { navigator.goTo(SudokuGameLevelSelection) },
                                ) {
                                    Text("GO TO GAME LEVEL SELECTION")
                                }
                            }
                        }
                    modules(homeNavigationModule)
                },
            )
        }

        runOnIdle {
            onNodeWithText("GO TO GAME LEVEL SELECTION").assertIsDisplayed().performClick()
            waitForIdle()
            onNodeWithText("Easy").assertIsDisplayed()
            onNodeWithText("Medium").assertIsDisplayed()
            onNodeWithText("Hard").assertIsDisplayed()
        }
    }
}

@OptIn(ExperimentalTestApi::class, KoinExperimentalAPI::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class SudokuGameScreenKoinTest : KoinTest {
    @get:Rule
    val koinTestRule =
        KoinTestRule.create {
            modules(
                module {
                    single(
                        qualifier = qualifier<InitialGameBoard>(),
                    ) {
                        List(9) { r ->
                            List(9) { c ->
                                if (r == 2 && c == 7) {
                                    8
                                } else {
                                    0
                                }
                            }
                        }.flatten().joinToString("")
                    }
                },
                module {
                    viewModel<SudokuGameViewModel>()
                },
            )
        }

    @Test
    fun testIsolatedSudokuGameScreen() = runComposeUiTest {
        setContent {
            SudokuGameScreen()
        }

        runOnIdle {
            (0..8).forEach { r ->
                (0..8).forEach { c ->
                    onNodeWithTag("BoardCell($r,$c)")
                        .apply {
                            if (r == 2 && c == 7) {
                                hasText("8")
                            } else {
                                // Assert(No TEXT property Set)
                                assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.Text))
                            }
                        }
                }
            }
        }
    }
}
