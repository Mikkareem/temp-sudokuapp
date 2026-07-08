package com.techullurgy.games.sudoku.game

import app.cash.turbine.test
import com.techullurgy.games.sudoku.presentation.viewmodels.SudokuGameViewModel
import io.kotest.core.spec.style.FunSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.time.measureTime

class SudokuGameViewModelTest : FunSpec() {
    init {

        context("timer test") {
            val viewModel = SudokuGameViewModel(GameLevel.EASY)

            beforeTest {
                Dispatchers.setMain(StandardTestDispatcher())
            }

            afterTest {
                Dispatchers.resetMain()
            }

            test("test 1") {
                runTest {
                    viewModel.state
                        .map { it.timer }
                        .distinctUntilChanged()
                        .test {
                            measureTime {
                                println(awaitItem())
                                println(awaitItem())
                                println(awaitItem())
                                println(awaitItem())
                                println(awaitItem())
                                println(awaitItem())
                            }.also { println(it) }
                        }
                }
            }
        }
    }
}
