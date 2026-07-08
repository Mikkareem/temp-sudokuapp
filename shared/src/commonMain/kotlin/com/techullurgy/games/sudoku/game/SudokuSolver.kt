package com.techullurgy.games.sudoku.game

import com.techullurgy.games.sudoku.utils.col
import com.techullurgy.games.sudoku.utils.in2D
import com.techullurgy.games.sudoku.utils.row
import com.techullurgy.games.sudoku.utils.indexing
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SudokuSolver {
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
    val emptyCells: Set<Int> field = (0 until 81).toMutableSet()

    suspend fun solve() {
        val solvedBoard = SudokuGenerator.generateWith(board.in2D)

        for (i in solvedBoard.indices) {
            for (j in solvedBoard[i].indices) {
                board[i indexing j] = solvedBoard[i][j]
            }
        }
    }

    fun setBoardAt(selectedCell: Int, selectedNumber: Int) {
        board[selectedCell.row indexing selectedCell.col] = selectedNumber
        if(selectedNumber == 0) {
            emptyCells.add(selectedCell)
        } else {
            emptyCells.remove(selectedCell)
        }
    }

    fun isEmptyCell(cell: Int): Boolean = emptyCells.contains(cell)

    fun reset() {
        for(r in 0 until 9) {
            for(c in 0 until 9) {
                board[r indexing c] = 0
                emptyCells.add(r indexing c)
            }
        }
    }
}
