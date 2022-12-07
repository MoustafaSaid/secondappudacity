package com.example.myapplication.main

import android.app.Application
import androidx.lifecycle.*
import com.example.myapplication.Asteroid
import com.example.myapplication.Constants
import com.example.myapplication.Data.Local.database.AsteroidDB
import com.example.myapplication.Data.Local.database.getDatabase
import com.example.myapplication.Data.api.AsteroidApi
import com.example.myapplication.PictureOfDay
import com.example.myapplication.repos.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepository = Repository(database)
    var getdatafromDatabase = false


    val WeekAsteroids = asteroidRepository.ListOfWeekAsteroids
    val todayAsteroids = asteroidRepository.ListOfTodayAsteroids
    val savedAsteroids = asteroidRepository.ListOfSavedAsteroids
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay


    private val _refreshedData = MutableLiveData(true)

    val refreshedData: LiveData<Boolean>
        get() = _refreshedData
    init {
        viewModelScope.launch {
            _refreshedData.value=asteroidRepository.refreshAsteroids()
            _pictureOfDay.value=  asteroidRepository.getTodayPicture()


        }
    }




}
