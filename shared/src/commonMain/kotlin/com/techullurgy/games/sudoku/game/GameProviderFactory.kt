package com.techullurgy.games.sudoku.game

import com.techullurgy.games.sudoku.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.qualifier

class GameProviderFactory(private val level: GameLevel, private val coroutineScope: CoroutineScope): KoinComponent {

    private val defaultDispatcher by inject<CoroutineDispatcher>(qualifier = qualifier<DefaultDispatcher>())
    fun generateDefault(): SudokuGame = SudokuGame(level).also {
        coroutineScope.launch(defaultDispatcher) { it.generate() }
    }

    fun initWithInitialBoard(initialBoardString: String): SudokuGame {
        val initial = initialBoardString.chunked(9).map { it.chunked(1).map { it.toInt() } }
        return SudokuGame(level).also {
            it.initWith(initial)
        }
    }
}
