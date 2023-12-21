package com.example.healthplus.data

import com.squareup.moshi.Json

data class Food(
        @Json(name = "name")
        val name: String,
        val calories : String,
        val serving_size_g : String,
        val fat_total_g : String,
        val fat_saturated_g : String,
        val protein_g : String,
        val sodium_mg : String,
        val potassium_mg : String,
        val cholesterol_mg : String,
        val carbohydrates_total_g : String,
        val fiber_g : String,
        val sugar_g : String
)