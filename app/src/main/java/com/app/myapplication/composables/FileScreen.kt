package com.app.myapplication.composables

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.app.myapplication.data.FileRepository
import com.app.myapplication.data.UserProfile
import com.app.myapplication.util.JsonProvider
import kotlinx.serialization.encodeToString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileScreen() {
    val context = LocalContext.current

    // Repository encapsulates internal-file read/write
    val repo = remember { FileRepository(context) }

    // Json configuration used for serialization
    val json = JsonProvider.json

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Form inputs (stored as Compose state)
    var name by remember { mutableStateOf("") }
    var ageText by remember { mutableStateOf("") } // keep as text until validated
    var favoriteColor by remember { mutableStateOf("") }

    // Shows raw JSON loaded from file
    var fileContent by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            // NOTE: this creates a second top bar if used inside AppNav's Scaffold
            TopAppBar(
                title = { Text("File Storage") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Name input
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Age input (filters to digits and max 3 chars)
            OutlinedTextField(
                value = ageText,
                onValueChange = { ageText = it.filter(Char::isDigit).take(3) },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth()
            )

            // Favorite color input
            OutlinedTextField(
                value = favoriteColor,
                onValueChange = { favoriteColor = it },
                label = { Text("Favorite color") },
                modifier = Modifier.fillMaxWidth()
            )

            // Buttons row: Save + Read
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                Button(onClick = {
                    // Validate age
                    val age = ageText.toIntOrNull()
                    if (age == null) {
                        showToast("Age must be a number")
                        return@Button
                    }

                    // Create data model, trim whitespace
                    val profile = UserProfile(name.trim(), age, favoriteColor.trim())

                    // Serialize to JSON
                    val text = json.encodeToString(profile)

                    // Write JSON to internal file
                    repo.write(text)

                    showToast("File saved successfully")
                }) {
                    Text("Save file")
                }

                OutlinedButton(onClick = {
                    // Read JSON from internal file
                    val text = repo.read()

                    if (text.isBlank()) {
                        fileContent = ""
                        showToast("No file saved")
                    } else {
                        fileContent = text
                        showToast("Loaded from internal file")
                    }
                }) {
                    Text("Read file")
                }
            }

            Text("File content:", style = MaterialTheme.typography.titleMedium)

            // SelectionContainer enables selecting/copying the JSON text
            SelectionContainer {
                OutlinedTextField(
                    value = fileContent,
                    onValueChange = {},
                    readOnly = true, // viewer mode
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), // takes remaining vertical space
                    textStyle = LocalTextStyle.current.copy(
                        fontFamily = FontFamily.Monospace
                    ),
                    placeholder = { Text("Nothing loaded yet") }
                )
            }
        }
    }
}