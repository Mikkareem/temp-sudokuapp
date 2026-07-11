package com.techullurgy.games.sudoku.di

import com.techullurgy.games.sudoku.core.provideDefaultDispatcher
import com.techullurgy.games.sudoku.core.provideMainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Qualifier
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create

@Qualifier
annotation class DefaultDispatcher

@Qualifier
annotation class MainDispatcher

val coreModule = module {
    single<CoroutineDispatcher>(
        qualifier = qualifier<DefaultDispatcher>()
    ) { create(::provideDefaultDispatcher) }

    single<CoroutineDispatcher>(
        qualifier = qualifier<MainDispatcher>()
    ) { create(::provideMainDispatcher) }
}