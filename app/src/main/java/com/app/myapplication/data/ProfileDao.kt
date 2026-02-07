package com.app.myapplication.data

import android.content.ContentValues

class ProfileDao(private val dbHelper: ProfileDbHelper) {

    fun insert(profile: UserProfile): Long {
        // Writable DB for INSERT/UPDATE/DELETE
        val db = dbHelper.writableDatabase

        // Map Kotlin data object -> ContentValues (column -> value)
        val values = ContentValues().apply {
            put(ProfileDbHelper.COL_NAME, profile.name)
            put(ProfileDbHelper.COL_AGE, profile.age)
            put(ProfileDbHelper.COL_FAVORITE_COLOR, profile.favoriteColor)
        }

        // Insert into table; returns the new row ID or -1 on error
        return db.insert(
            ProfileDbHelper.TABLE_PROFILES,
            null,
            values
        )
    }

    fun getAll(): List<Pair<Long, UserProfile>> {
        // Readable DB for SELECT queries
        val db = dbHelper.readableDatabase

        // Query all rows ordered by id descending (newest first)
        val cursor = db.query(
            ProfileDbHelper.TABLE_PROFILES,
            arrayOf(
                ProfileDbHelper.COL_ID,
                ProfileDbHelper.COL_NAME,
                ProfileDbHelper.COL_AGE,
                ProfileDbHelper.COL_FAVORITE_COLOR
            ),
            null, // selection (WHERE)
            null, // selectionArgs
            null, // groupBy
            null, // having
            "${ProfileDbHelper.COL_ID} DESC"
        )

        val result = mutableListOf<Pair<Long, UserProfile>>()

        // cursor.use auto-closes cursor after block (prevents leaks)
        cursor.use {
            // Get column indices once (faster + cleaner)
            val idIndex = it.getColumnIndexOrThrow(ProfileDbHelper.COL_ID)
            val nameIndex = it.getColumnIndexOrThrow(ProfileDbHelper.COL_NAME)
            val ageIndex = it.getColumnIndexOrThrow(ProfileDbHelper.COL_AGE)
            val colorIndex = it.getColumnIndexOrThrow(ProfileDbHelper.COL_FAVORITE_COLOR)

            // Iterate rows
            while (it.moveToNext()) {
                val id = it.getLong(idIndex)

                // Convert cursor row -> UserProfile
                val profile = UserProfile(
                    name = it.getString(nameIndex),
                    age = it.getInt(ageIndex),
                    favoriteColor = it.getString(colorIndex)
                )

                // Store pair (id, profile) for UI display
                result.add(id to profile)
            }
        }

        return result
    }

    fun clearAll() {
        val db = dbHelper.writableDatabase

        // Delete all rows in the table
        db.delete(ProfileDbHelper.TABLE_PROFILES, null, null)
    }
}