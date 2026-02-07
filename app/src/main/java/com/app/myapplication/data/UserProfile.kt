package com.app.myapplication.data

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    // These field names become JSON keys by default (name, age, favoriteColor)
    val name: String,
    val age: Int,
    val favoriteColor: String
)