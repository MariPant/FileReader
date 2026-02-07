package com.app.myapplication.composables

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.app.myapplication.data.FileRepository
import com.app.myapplication.data.ProfileDao
import com.app.myapplication.data.ProfileDbHelper
import com.app.myapplication.data.UserProfile
import com.app.myapplication.util.JsonProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatabaseScreen() {
    val context = LocalContext.current

    // Create DAO once per composition (remember prevents re-creating on recomposition)
    val dao = remember { ProfileDao(ProfileDbHelper(context)) }

    // File repo used to load the JSON text from internal storage
    val fileRepo = remember { FileRepository(context) }

    // Shared Json config (prettyPrint, ignoreUnknownKeys)
    val json = JsonProvider.json

    // Small helper to show toast messages (quick UX feedback)
    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Holds the JSON currently displayed/edited in the text field
    var jsonText by remember { mutableStateOf("") }

    // Holds DB rows: (rowId, UserProfile)
    var rows by remember { mutableStateOf<List<Pair<Long, UserProfile>>>(emptyList()) }

    // Helper function to reload DB content into UI state
    fun refresh() {
        // Runs a query and stores results into Compose state -> triggers recomposition
        rows = dao.getAll()
    }

    // Run once when this composable enters composition
    LaunchedEffect(Unit) {
        refresh()
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Save JSON into SQLite",
            style = MaterialTheme.typography.titleMedium
        )

        // --- Load JSON from internal file ---
        OutlinedButton(
            onClick = {
                // Reads profile.json from internal storage
                val text = fileRepo.read()

                if (text.isBlank()) {
                    showToast("Internal file is empty")
                } else {
                    // Put file text into editor
                    jsonText = text
                    showToast("Loaded JSON from internal file")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Load JSON from file")
        }

        // --- JSON editor ---
        OutlinedTextField(
            value = jsonText,
            onValueChange = { jsonText = it },
            modifier = Modifier.fillMaxWidth(),
            // minLines keeps it tall enough for editing JSON comfortably
            minLines = 6,
            textStyle = LocalTextStyle.current.copy(
                fontFamily = FontFamily.Monospace // monospace helps readability for JSON
            ),
            placeholder = {
                // Placeholder is empty right now; you could put example JSON here
                Text("")
            }
        )

        // --- Save JSON → DB ---
        Button(
            onClick = {
                runCatching {
                    // Parse JSON into UserProfile using kotlinx.serialization
                    val profile = json.decodeFromString<UserProfile>(jsonText)

                    // Insert into SQLite via DAO
                    val id = dao.insert(profile)

                    showToast("Inserted row id = $id")

                    // OPTIONAL (but useful): refresh so the new row appears immediately
                    // refresh()
                }.onFailure {
                    // Catches JSON parse errors and DB insert errors
                    showToast("Parse/DB error: ${it.message}")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save JSON → DB")
        }

        // --- Load from DB + Clear DB ---
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = {
                    refresh()
                    showToast("Loaded ${rows.size} rows from DB")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Load from DB")
            }

            OutlinedButton(
                onClick = {
                    dao.clearAll()
                    refresh()
                    showToast("DB cleared")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Clear DB")
            }
        }

        // Show title only if there are rows to display
        if (rows.isNotEmpty()) {
            Text(
                text = "Database rows:",
                style = MaterialTheme.typography.titleMedium
            )
        }

        // List DB content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(rows) { (id, profile) ->
                Card {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text("ID: $id", style = MaterialTheme.typography.labelMedium)
                        Text("Name: ${profile.name}")
                        Text("Age: ${profile.age}")
                        Text("Favorite color: ${profile.favoriteColor}")
                    }
                }
            }
        }
    }
}