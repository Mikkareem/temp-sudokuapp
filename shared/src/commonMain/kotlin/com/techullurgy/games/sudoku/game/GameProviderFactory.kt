package com.techullurgy.games.sudoku.game

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameProviderFactory(private val level: GameLevel, private val coroutineScope: CoroutineScope) {
    fun generateDefault(): SudokuGame = SudokuGame(level).also {
        coroutineScope.launch(Dispatchers.Default) { it.generate() }
    }

    fun initWithInitialBoard(initialBoardString: String): SudokuGame {
        val initial = initialBoardString.chunked(9).map { it.chunked(1).map { it.toInt() } }
        return SudokuGame(level).also {
            it.initWith(initial)
        }
    }
}
