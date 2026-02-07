package com.app.myapplication.util

import kotlinx.serialization.json.Json

object JsonProvider {
    val json: Json = Json {
        // Makes output JSON pretty (indented) when encoding
        prettyPrint = true

        // Ignores extra JSON fields not present in UserProfile
        // (helps if JSON format changes or contains more data)
        ignoreUnknownKeys = true
    }
}