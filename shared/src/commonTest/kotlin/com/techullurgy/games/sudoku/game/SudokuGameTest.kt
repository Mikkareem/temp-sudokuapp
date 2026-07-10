package com.techullurgy.games.sudoku.game

import app.cash.turbine.test
import com.techullurgy.games.sudoku.utils.col
import com.techullurgy.games.sudoku.utils.row
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class SudokuGameTest : FunSpec() {
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerTest

    init {
        coroutineTestScope = true

        val game = SudokuGame(GameLevel.EASY)

        test("changing the board will trigger the boardState Flow update") {
            game.boardState.test {
                val actualBoardBeforeUpdate = awaitItem()
                game.setBoardAt(24, 6)
                val actualBoardAfterUpdate = awaitItem()

                val expected =
                    actualBoardBeforeUpdate.toMutableList().apply {
                        this[24.row] =
                            this[24.row].toMutableList().apply {
                                this[24.col] = 6
                            }
                    }

                actualBoardAfterUpdate shouldBe expected
            }
        }
    }
}
