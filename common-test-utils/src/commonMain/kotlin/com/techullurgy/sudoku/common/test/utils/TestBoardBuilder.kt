package com.techullurgy.sudoku.common.test.utils


class TestBoardBuilder {
    private val _board = List(9) { MutableList(9) { 0 } }

    fun setAt(index: Int, value: Int): TestBoardBuilder {
        _board[index / 9][index % 9] = value
        return this
    }

    fun setAt(row: Int, col: Int, value: Int): TestBoardBuilder {
        _board[row][col] = value
        return this
    }

    fun build(): List<List<Int>> {
        return _board
    }
}