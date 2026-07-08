package com.techullurgy.sudoku

import androidx.activity.ComponentActivity
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import kotlinx.serialization.Serializable
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class NavigationEventTest {
    @Serializable data object Screen1 : NavKey

    @Serializable data object Screen2 : NavKey

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun basicBackNavigationTest() {
        rule.setContent {
            val backStack = rememberNavBackStack(Screen1)
            NavDisplay(
                backStack = backStack,
                entryProvider =
                entryProvider {
                    entry<Screen1> {
                        Button(
                            onClick = { backStack.add(Screen2) },
                        ) {
                            Text("Screen2")
                        }
                    }
                    entry<Screen2> {
                        Button(
                            onClick = { backStack.removeLastOrNull() },
                        ) {
                            Text("Back")
                        }
                    }
                },
            )
        }

        rule.runOnIdle {
            rule.onNodeWithText("Back").assertIsNotDisplayed()
            rule.onNodeWithText("Screen2").assertIsDisplayed().performClick()
            rule.waitForIdle()
            rule.onNodeWithText("Back").assertIsDisplayed()
            rule.onNodeWithText("Screen2").assertIsNotDisplayed()

            // System Back Button Press Simulation in Robolectric
            rule.activity.onBackPressedDispatcher.onBackPressed()

            rule.waitForIdle()
            rule.onNodeWithText("Screen2").assertIsDisplayed()
        }
    }

    @Test
    fun navigationBackHandler_NoOp_OnSystemBackPress() {
        rule.setContent {
            val backStack = rememberNavBackStack(Screen1)
            NavDisplay(
                backStack = backStack,
                entryProvider =
                entryProvider {
                    entry<Screen1> {
                        Button(
                            onClick = { backStack.add(Screen2) },
                        ) {
                            Text("Screen2")
                        }
                    }
                    entry<Screen2> {
                        val state =
                            rememberNavigationEventState(
                                currentInfo = object : NavigationEventInfo() {},
                            )

                        NavigationBackHandler(
                            state = state,
                            isBackEnabled = true,
                            onBackCancelled = {},
                            onBackCompleted = { /* No-Op on BackPress */ },
                        )
                        Button(
                            onClick = { backStack.removeLastOrNull() },
                        ) {
                            Text("Back")
                        }
                    }
                },
            )
        }

        rule.runOnIdle {
            rule.onNodeWithText("Screen2").assertIsDisplayed().performClick()
            rule.waitForIdle()
            rule.onNodeWithText("Back").assertIsDisplayed()

            rule.activity.onBackPressedDispatcher.onBackPressed()

            rule.waitForIdle()
            rule.onNodeWithText("Back").assertIsDisplayed()
            rule.onNodeWithText("Screen2").assertIsNotDisplayed()
        }
    }
}
