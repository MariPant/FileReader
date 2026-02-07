package com.app.myapplication.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav() {
    // Holds which tab is currently selected (0 = Files, 1 = SQLite)
    var tab by remember { mutableIntStateOf(0) }

    // Titles shown in the TabRow
    val tabs = listOf("Exercise 1 (Files)", "Exercise 2 (SQLite)")

    Scaffold(
        topBar = {
            // Using a Column to stack the TopAppBar + TabRow
            Column {
                TopAppBar(
                    title = { Text("Files + SQLite App") },
                    // Material3 TopAppBar color customization
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )

                // Tabs for switching between screens
                TabRow(selectedTabIndex = tab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = tab == index,
                            // Update the selected tab state
                            onClick = { tab = index },
                            text = { Text(title) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        // padding comes from Scaffold (so content doesn't go under top bar)
        Box(Modifier.padding(padding).fillMaxSize()) {
            // Simple "navigation": show one composable or the other
            when (tab) {
                0 -> FileScreen()
                1 -> DatabaseScreen()
            }
        }
    }
}