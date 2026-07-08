package com.techullurgy.sudoku.game

import com.techullurgy.sudoku.utils.col
import com.techullurgy.sudoku.utils.in2D
import com.techullurgy.sudoku.utils.row
import com.techullurgy.sudoku.utils.indexing
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SudokuGame(level: GameLevel) {
    val boardState: StateFlow<Board> field =
        MutableStateFlow(
            List(9) { List(9) { 0 } },
        )

    private val board: MutableList<Int> =
        run {
            val original = MutableList(9 * 9) { 0 }
            object : MutableList<Int> by original {
                override fun set(index: Int, element: Int): Int {
                    boardState.update {
                        it.toMutableList().apply {
                            this[index.row] =
                                this[index.row].toMutableList().apply {
                                    this[index.col] = element
                                }
                        }
                    }
                    return original.set(index, element)
                }
            }
        }

    var emptyCells = generateRandomIndicesForEmptying(level.emptyCount)
        private set

    suspend fun generate() {
        val newBoard = SudokuGenerator.generate().map { it.toMutableList() }
        emptyCells.forEach {
            newBoard[it.row][it.col] = 0
        }

        for (r in 0 until 9) {
            for (c in 0 until 9) {
                board[r indexing c] = newBoard[r][c]
            }
        }
    }

    fun initWith(initial: Board) {
        emptyCells =
            initial
                .flatten()
                .mapIndexed { index, it -> index to it }
                .filter { it.second == 0 }
                .map { it.first }
                .toSet()
        for (r in 0 until 9) {
            for (c in 0 until 9) {
                board[r indexing c] = initial[r][c]
            }
        }
    }

    fun setBoardAt(selectedCell: Int, selectedNumber: Int) {
        board[selectedCell.row indexing selectedCell.col] = selectedNumber
    }

    fun isEmptyCell(cell: Int): Boolean = emptyCells.contains(cell)

    fun isGridSuccessfulDone(): Boolean {
        if (!isFinished()) return false
        return SudokuValidator.isValid(board.in2D)
    }

    private fun isFinished(): Boolean {
        for (i in board.indices) {
            if (board[i] == 0) return false
        }

        return true
    }

    companion object {
        private fun generateRandomIndicesForEmptying(count: Int): Set<Int> {
            val res = mutableSetOf<Int>()
            while (true) {
                if (res.size >= count) return res
                res.add((0 until 9 * 9).random())
            }
        }
    }
}
