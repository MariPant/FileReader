# FileReader
## Files + SQLite App (Jetpack Compose)

A simple Android app (Jetpack Compose) for two exercises:

- **Exercise 1 – Reading & Writing Files:** Save a JSON file into the app’s internal storage and read it back to display on screen.
- **Exercise 2 – Databases (SQLite):** Load JSON (from file or manual input), parse it into a Kotlin data class, and store it in an **SQLite** database using **SQLiteOpenHelper** + a simple DAO. You can also load rows from the DB and clear the table.

---

## Features

### Exercise 1 (Files)
- Enter a `UserProfile` (name, age, favorite color)
- Serialize to JSON using `kotlinx.serialization`
- Save to internal storage file: `profile.json`
- Read `profile.json` back and display its content

### Exercise 2 (SQLite)
- Load JSON from the internal file into a text editor
- Edit JSON manually
- Parse JSON into `UserProfile`
- Insert profile into SQLite database
- Load all rows from SQLite and display them in a list
- Clear the database table

---

## Data Format (JSON)

The app stores a simple JSON object:

```json
{
  "name": "Alice",
  "age": 23,
  "favoriteColor": "Blue"
}
```
## Project Structure

### UI (Compose)

**AppNav**  
Top bar with tab navigation between the **Files** and **SQLite** screens.

**FileScreen**  
Form-based UI for creating a user profile, saving it as a JSON file, and reading the file content back.

**DatabaseScreen**  
JSON text editor with actions to load JSON from file, insert parsed data into SQLite, load rows from the database, and clear the database.

---

### Data Layer

**FileRepository**  
Handles reading and writing JSON data to internal storage:  
`context.filesDir/profile.json`

**ProfileDbHelper**  
`SQLiteOpenHelper` implementation that creates and upgrades the SQLite database (`app.db`).

**ProfileDao**  
Data Access Object (DAO) for inserting profiles, querying all rows, and clearing the table.

**UserProfile**  
Serializable Kotlin data class representing the user profile.

---

### Utils

**JsonProvider**  
Provides a shared `Json` configuration with:
- Pretty printing enabled
- Unknown keys ignored during deserialization

---
