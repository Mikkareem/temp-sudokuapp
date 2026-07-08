package com.techullurgy.sudoku.game

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

object SudokuGenerator {
    private val sudokuBoard: MutableBoard = List(9) { List(9) { 0 }.toMutableList() }

    private fun initialize(board: Board = sudokuBoard.map { it.map { 0 } }) {
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                sudokuBoard[i][j] = board[i][j]
            }
        }
    }

    suspend fun generate(): Board {
        initialize()
        solveSudokuPrivately()
        return sudokuBoard
    }

    suspend fun generateWith(board: Board): Board {
        initialize(board)
        solveSudokuPrivately()
        return sudokuBoard
    }

    private suspend fun solveSudokuPrivately(): Boolean {
        for (row in 0 until 9) {
            currentCoroutineContext().ensureActive()
            for (column in 0 until 9) {
                currentCoroutineContext().ensureActive()
                if (sudokuBoard[row][column] == 0) {
                    val guesses = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
                    guesses.shuffle()
                    for (guessIndex in 0 until 9) {
                        currentCoroutineContext().ensureActive()
                        if (validMove(guesses[guessIndex], row, column)) {
                            sudokuBoard[row][column] = guesses[guessIndex]
                            if (solveSudokuPrivately()) {
                                return true
                            }
                            sudokuBoard[row][column] = 0
                        }
                    }
                    return false
                }
            }
        }
        return true
    }

    private fun validMove(guess: Int, row: Int, column: Int): Boolean = isGuessValidInRow(guess, row) &&
        isGuessValidInColumn(guess, column) &&
        isGuessValidInBox(guess, row, column)

    private fun isGuessValidInBox(guess: Int, row: Int, column: Int): Boolean {
        val localBoxRowStart = row - row % 3
        val localBoxColumnStart = column - column % 3
        for (i in localBoxRowStart until localBoxRowStart + 3) {
            for (j in localBoxColumnStart until localBoxColumnStart + 3) {
                if (sudokuBoard[i][j] == guess) return false
            }
        }
        return true
    }

    private fun isGuessValidInColumn(guess: Int, column: Int): Boolean {
        for (i in 0 until 9) {
            if (sudokuBoard[i][column] == guess) return false
        }
        return true
    }

    private fun isGuessValidInRow(guess: Int, row: Int): Boolean {
        for (i in 0 until 9) {
            if (sudokuBoard[row][i] == guess) return false
        }
        return true
    }
}
