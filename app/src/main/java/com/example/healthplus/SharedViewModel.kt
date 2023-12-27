package com.example.healthplus

import FoodRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.healthplus.data.Food


class SharedViewModel : ViewModel() {
    private val _str = MutableLiveData<String>()
    val str: LiveData<String> get() = _str

    private val qRepository: FoodRepository = FoodRepository()

    val food: LiveData<List<Food>> = str.switchMap { queryString ->
        liveData(viewModelScope.coroutineContext) {
            emit(qRepository.getStatements(queryString))
        }
    }

    fun setQueryString(query: String) {
        _str.value = query
    }
}
