@file:Suppress("unused")

package com.techullurgy.games.sudoku

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.techullurgy.games.sudoku.presentation.components.TruchetTiles
import com.techullurgy.games.sudoku.presentation.navigation.CustomOverlaySceneStrategy
import com.techullurgy.games.sudoku.presentation.navigation.Home
import com.techullurgy.games.sudoku.presentation.navigation.Navigator
import com.techullurgy.games.sudoku.presentation.navigation.navigationModule
import com.techullurgy.games.sudoku.presentation.ui.contentColor
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.includes
import org.koin.dsl.koinConfiguration
import org.koin.plugin.module.dsl.koinConfiguration

@KoinApplication
class SudokuApp

@Module
@Configuration
@ComponentScan("com.techullurgy.games.sudoku")
class AppModule {
    @Singleton
    fun provideStartDestination(): NavKey = Home
}

@OptIn(KoinExperimentalAPI::class)
@Composable
@Suppress("ktlint:compose:modifier-missing-check")
fun App(config: KoinConfiguration? = null) {
    KoinApplication(
        configuration =
        koinConfiguration {
            modules(navigationModule)
            includes(koinConfiguration<SudokuApp>(), config)
        },
    ) {
        val navigator = koinInject<Navigator>()
        val entryProvider = koinEntryProvider<NavKey>()

        val customOverlaySceneStrategy = remember { CustomOverlaySceneStrategy<NavKey>() }

        Box {
            TruchetTiles()

            CompositionLocalProvider(
                LocalContentColor provides contentColor,
            ) {
                NavDisplay(
                    backStack = navigator.backStack,
                    onBack = { navigator.goBack() },
                    sceneStrategies = listOf(customOverlaySceneStrategy),
                    entryDecorators =
                    listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                    entryProvider = entryProvider,
                )
            }
        }
    }
}
