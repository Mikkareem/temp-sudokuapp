package com.techullurgy.sudoku

import app.cash.turbine.test
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import kotlin.coroutines.ContinuationInterceptor
import kotlin.time.Duration.Companion.milliseconds

class TurbineFlowTest :
    FunSpec({
        test("test1") {
            runTest {
                flowOf(1, 2, 3)
                    .onEach { delay(200.milliseconds) }
                    .test {
                        awaitItem() shouldBe 1
                        awaitItem() shouldBe 2
                        awaitItem() shouldBe 3
                        awaitComplete()
                    }
            }
        }

        test("test2").config(
            coroutineTestScope = true,
        ) {
            println(coroutineContext[ContinuationInterceptor]) // StandardTestDispatcher

            flowOf(1, 2, 3)
                .onEach { delay(200.milliseconds) }
                .test {
                    awaitItem() shouldBe 1
                    awaitItem() shouldBe 2
                    awaitItem() shouldBe 3
                    awaitComplete()
                }
        }
    })
