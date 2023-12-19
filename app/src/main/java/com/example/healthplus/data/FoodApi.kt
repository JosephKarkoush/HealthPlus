package com.example.healthplus.data


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApi {
    @GET("nutrition")
    suspend fun getStatements(@Query("query") choice :String): Response<Food>

}