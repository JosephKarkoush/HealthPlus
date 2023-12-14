package com.example.healthplus

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object ApiConnection {

    fun getApiAnswer(str: String): String {
        val apiUrl = "https://api.api-ninjas.com/v1/nutrition?query=burger"
        val apiKey = "VIA4++Z6JrjPomS+N/LZHw==3e1GXcAKfgNj5Psd"

        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.setRequestProperty("X-Api-Key", apiKey)
        connection.setRequestProperty(
            "Content-Type",
            "application/json"
        ) // Adjust content type as per API requirements

        try {
            val responseCode = connection.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read and process the API response
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                var line: String?
                val response = StringBuilder()

                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                reader.close()

                print("API response: $response")
                return response.toString()
            } else {
                // Print an error message if the request was not successful
                print("Error: $responseCode - ${connection.responseMessage}")
                return responseCode.toString()
            }

        } catch (e: Exception) {
            print("Request failed: $e")
            return "Request failed: $e"
        } finally {
            connection.disconnect()
        }
    }
}
