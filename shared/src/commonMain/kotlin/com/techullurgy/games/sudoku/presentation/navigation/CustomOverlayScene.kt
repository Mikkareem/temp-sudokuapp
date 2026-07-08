package com.techullurgy.games.sudoku.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

class CustomOverlaySceneStrategy<T : NavKey> : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.last()
        return lastEntry.metadata[CustomOverlayNavMetadataKey]?.let {
            val previousEntries = entries.dropLast(1)
            CustomOverlayScene(
                key = lastEntry.contentKey,
                entry = lastEntry,
                previousEntries = previousEntries,
            )
        }
    }

    companion object {
        fun customOverlay(): Map<String, Any> = metadata {
            put(CustomOverlayNavMetadataKey, Unit)
        }
    }
}

private data class CustomOverlayScene<T : NavKey>(
    val entry: NavEntry<T>,
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
) : OverlayScene<T> {
    override val entries: List<NavEntry<T>> = listOf(entry)
    override val overlaidEntries: List<NavEntry<T>> = previousEntries
    override val content: @Composable (() -> Unit) = entry::Content
}

data object CustomOverlayNavMetadataKey : NavMetadataKey<Unit>
