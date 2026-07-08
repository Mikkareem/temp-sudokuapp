package com.techullurgy.games.sudoku.utils

import com.techullurgy.games.sudoku.game.Board

internal val Int.row: Int get() = this / 9
internal val Int.col: Int get() = this % 9

// row indexing col = index
internal infix fun Int.indexing(other: Int): Int = this * 9 + other

internal val List<Int>.in2D: List<List<Int>> get() = chunked(9)

internal fun Board.toEnabledNumbers(): Set<Int> = flatten()
    .groupingBy { it }
    .eachCount()
    .let { oldMap ->
        setOf(1,2,3,4,5,6,7,8,9).run {
            map { it to (oldMap[it] ?: 0) }
        }.toMap()
    }
    .mapNotNull { (k, v) ->
        if (v >= 9) null else k
    }.toSet()

internal fun getRelevantBoardIndicesFor(index: Int): Set<Int> {
    if (index == -1) return emptySet()

    val res = mutableSetOf(index)

    val currentRow = index.row
    val currentCol = index.col
    val currentRowStart = currentRow / 3
    val currentColStart = currentCol / 3

    for (row in 0 until 9) {
        for (col in 0 until 9) {
            val newIndex = row indexing col

            val rowStart = row / 3
            val colStart = col / 3

            (
                    (row == currentRow) || (col == currentCol) ||
                            (rowStart == currentRowStart && colStart == currentColStart)
                    )
                .takeIf { it }
                ?.let { res.add(newIndex) }
        }
    }

    return res.toSet()
}