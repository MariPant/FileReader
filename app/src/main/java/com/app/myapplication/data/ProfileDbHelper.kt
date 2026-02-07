package com.app.myapplication.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ProfileDbHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        // Called only when DB is created for the first time
        db.execSQL(
            """
            CREATE TABLE $TABLE_PROFILES (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NAME TEXT NOT NULL,
                $COL_AGE INTEGER NOT NULL,
                $COL_FAVORITE_COLOR TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // For exercises: simplest strategy is dropping & recreating (data loss)
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PROFILES")
        onCreate(db)
    }

    companion object {
        // Database file name (stored internally by Android)
        const val DATABASE_NAME = "app.db"

        // Increment this when schema changes to trigger onUpgrade
        const val DATABASE_VERSION = 1

        // Table and column names
        const val TABLE_PROFILES = "profiles"
        const val COL_ID = "id"
        const val COL_NAME = "name"
        const val COL_AGE = "age"
        const val COL_FAVORITE_COLOR = "favorite_color"
    }
}