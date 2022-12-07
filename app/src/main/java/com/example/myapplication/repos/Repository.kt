package com.example.myapplication.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.myapplication.Asteroid
import com.example.myapplication.Constants
import com.example.myapplication.Constants.API_KEY
import com.example.myapplication.Constants.API_QUERY_DATE_FORMAT
import com.example.myapplication.Constants.DEFAULT_END_DATE_DAYS
import com.example.myapplication.Data.Local.database.AsteroidDB
import com.example.myapplication.Data.Local.database.asDomainModel
import com.example.myapplication.Data.Local.database.asDatabaseModel
import com.example.myapplication.Data.api.AsteroidApi
import com.example.myapplication.PictureOfDay
import com.example.myapplication.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class Repository(val database: AsteroidDB) {


suspend fun getTodayPicture(): PictureOfDay {
    return try {
        AsteroidApi.retrofitService.getPictureOfDay( Constants.API_KEY)
    } catch (e: Exception) {
        PictureOfDay("", "", "")
    }
}

    val  ListOfWeekAsteroids:LiveData<List<Asteroid>>
    get(){
        val calendar = Calendar.getInstance()
             calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorow=calendar.time.time
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val oneWeekFromTomorrow = calendar.time.time

        return  Transformations.map(
            database.SavedDao.getAsteroidsFromDates(

                tomorow,oneWeekFromTomorrow
            )
        ){
            it.asDomainModel()
        }

    }

    val  ListOfTodayAsteroids:LiveData<List<Asteroid>>
    get(){
        val calendar = Calendar.getInstance()
        val todayDay= calendar.time.time
        return  Transformations.map(
            database.SavedDao.getAsteroidsFromDates(
                todayDay,todayDay
            )
        ){
            it.asDomainModel()
        }

    }
    val  ListOfSavedAsteroids:LiveData<List<Asteroid>>
    get(){
        val calendar = Calendar.getInstance()
        val todayDay= calendar.time.time
        calendar.add(Calendar.DAY_OF_YEAR,7)
        val endOfTheWeek=calendar.time.time
        return  Transformations.map(
            database.SavedDao.getAsteroidsFromDates(
                todayDay,endOfTheWeek
            )
        ){
            it.asDomainModel()
        }

    }




  suspend  fun refreshAsteroids(): Boolean {


        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat(API_QUERY_DATE_FORMAT)
        val startDate = format.format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, DEFAULT_END_DATE_DAYS)
        val endDate = format.format(calendar.time)

        return try {
            val response = AsteroidApi.retrofitService.getAsteroidsList(startDate, endDate, API_KEY)
            if (response.code() != 200) {
                false
            } else {
                response.body()?.let {
                    val entityList = parseAsteroidsJsonResult(JSONObject(it)).asDatabaseModel()
                    database.SavedDao.insertAll(entityList)
                }
                true
            }
        } catch (e: Exception) {
            false
        }
    }



    suspend fun deletePreviousDayAsteroids() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        withContext(Dispatchers.IO) {
            database.SavedDao.deleteOldAsteroids(calendar.time.time)
        }
    }

}