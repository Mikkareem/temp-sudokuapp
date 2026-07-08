package com.techullurgy.sudoku.game

object SudokuValidator {
    fun isValidAfterPut(board: Board, row: Int, col: Int, value: Int): Boolean {
        val newBoard = board.map { it.toMutableList() }
        newBoard[row][col] = value
        return isRowCorrect(newBoard, row) && isColumnCorrect(newBoard, col) && isBoxCorrect(newBoard, row, col)
    }

    fun isValid(board: Board): Boolean {
        for (i in 0 until 9) {
            if (!isRowCorrect(board, i) || !isColumnCorrect(board, i)) return false
        }

        for (i in 0 until 9) {
            for (j in 0 until 9) {
                if (!isBoxCorrect(board, i, j)) return false
            }
        }

        return true
    }

    private fun isRowCorrect(board: Board, row: Int): Boolean {
        val res = mutableSetOf<Int>()
        for (i in board.indices) {
            if (res.contains(board[row][i])) return false
            if (board[row][i] != 0) {
                res.add(board[row][i])
            }
        }
        return true
    }

    private fun isColumnCorrect(board: Board, column: Int): Boolean {
        val res = mutableSetOf<Int>()
        for (i in board[column].indices) {
            if (res.contains(board[i][column])) return false
            if (board[i][column] != 0) {
                res.add(board[i][column])
            }
        }
        return true
    }

    private fun isBoxCorrect(board: Board, row: Int, column: Int): Boolean {
        val res = mutableSetOf<Int>()

        val rowStart = row - row % 3
        val colStart = column - column % 3
        val rowEnd = rowStart + 3
        val colEnd = colStart + 3

        for (i in rowStart until rowEnd) {
            for (j in colStart until colEnd) {
                if (res.contains(board[i][j])) return false
                if (board[i][j] != 0) {
                    res.add(board[i][j])
                }
            }
        }

        return true
    }
}
