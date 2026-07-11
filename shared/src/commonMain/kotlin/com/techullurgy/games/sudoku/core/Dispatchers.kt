package com.techullurgy.games.sudoku.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

internal fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main