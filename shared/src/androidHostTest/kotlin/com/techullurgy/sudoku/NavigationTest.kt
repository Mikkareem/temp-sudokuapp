package com.techullurgy.sudoku

import androidx.compose.animation.EnterExitState
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class NavigationTest {
    @Test
    fun navAnimationTransitionTargetStateTest() = runComposeUiTest {
        val backstack = mutableStateListOf<Any>("one")

        lateinit var targetState1: EnterExitState
        lateinit var targetState2: EnterExitState

        setContent {
            NavDisplay(
                backStack = backstack,
                entryProvider =
                entryProvider {
                    entry<String> {
                        targetState1 = LocalNavAnimatedContentScope.current.transition.targetState
                        println(LocalNavAnimatedContentScope.current.transition.isRunning)
                        Text(it)
                    }
                    entry<Int> {
                        targetState2 = LocalNavAnimatedContentScope.current.transition.targetState
                        Text("$it")
                    }
                },
            )
        }

        waitForIdle()
        targetState1 shouldBe EnterExitState.Visible
        shouldThrow<UninitializedPropertyAccessException> { targetState2 }

        backstack.add(2)
        waitForIdle()
        shouldNotThrow<UninitializedPropertyAccessException> { targetState2 }
        targetState1 shouldBe EnterExitState.PostExit
        targetState2 shouldBe EnterExitState.Visible

        backstack.removeLastOrNull()
        waitForIdle()
        targetState1 shouldBe EnterExitState.Visible
        targetState2 shouldBe EnterExitState.PostExit
    }
}
