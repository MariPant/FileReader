package com.app.myapplication.data

import android.content.Context
import java.io.File

class FileRepository(private val context: Context) {

    // Internal file name in app's private storage
    private val fileName = "profile.json"

    fun write(text: String) {
        // context.filesDir points to internal app storage (no extra permissions needed)
        val file = File(context.filesDir, fileName)

        // Writes text as UTF-8; overwrites previous content
        file.writeText(text, Charsets.UTF_8)
    }

    fun read(): String {
        val file = File(context.filesDir, fileName)

        // If file doesn't exist yet, return empty string
        if (!file.exists()) return ""

        // Read entire file to string
        return file.readText(Charsets.UTF_8)
    }
}