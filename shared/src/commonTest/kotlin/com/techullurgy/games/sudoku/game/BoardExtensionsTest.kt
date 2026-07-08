package com.techullurgy.games.sudoku.game

import com.techullurgy.games.sudoku.common.test.utils.TestBoardBuilder
import com.techullurgy.games.sudoku.utils.toEnabledNumbers
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class BoardExtensionsTest: FunSpec() {
    init {
        test("") {
            val zeroOnlyBoard = TestBoardBuilder().build()
                .toEnabledNumbers()

            zeroOnlyBoard shouldBe setOf(1,2,3,4,5,6,7,8,9)
        }
    }
}