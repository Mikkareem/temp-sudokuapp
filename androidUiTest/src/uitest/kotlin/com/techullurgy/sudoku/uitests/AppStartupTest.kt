package com.techullurgy.sudoku.uitests

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.textAsString
import androidx.test.uiautomator.uiAutomator
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppStartupTest {
    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val intent =
            Intent().apply {
                setClassName(
                    "com.techullurgy.sudoku",
                    "com.techullurgy.sudoku.MainActivity",
                )
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

        context.startActivity(intent)
    }

    @After
    fun tearDown() {
        // kill recents (optional hard reset)
        uiAutomator {
            pressHome()
            device.executeShellCommand("pm clear com.techullurgy.sudoku")
        }
    }

    @Test
    fun startTest() {
        uiAutomator {
            onElement { textAsString() == "Sudoku Game" }
                .click()
        }
    }

    @Test
    fun secondTest() {
        uiAutomator {
            onElement { textAsString() == "Sudoku Game" }
                .click()
            onElement { textAsString() == "Easy" }.click()
            device.waitForIdle()

            Thread.sleep(5000)
        }
    }
}
