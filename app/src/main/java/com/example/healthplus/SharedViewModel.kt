package com.example.healthplus

import FoodRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.healthplus.data.Food


class SharedViewModel : ViewModel() {
    var str = ""
    private val qRepository: FoodRepository = FoodRepository()
    val food: LiveData<Food> = liveData {
        emit(qRepository.getStatements(str))
    }

    // Ingen särskild funktion för att hämta LiveData behövs
}