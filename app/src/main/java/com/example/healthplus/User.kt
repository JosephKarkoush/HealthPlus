package com.example.healthplus

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User(
    val name: String,
    val gender: String,
    val age: String,
    val height: String,
    val lastWeight: String,
    val weightEntries: List<WeightEntry>
)