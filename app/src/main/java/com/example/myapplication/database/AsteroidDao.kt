package com.example.myapplication.Data.Local.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.Asteroid


/**
 * Defines methods for using the SleepNight class with Room.
 */
@Dao
interface AsteroidDao {

    @Query("select * from asteroidTable where closeApproachDate > :date order by closeApproachDate asc")
    fun getAsteroids(date: Long): LiveData<List<AsteroidTable>>

    @Query("select * from asteroidTable where closeApproachDate > :from and closeApproachDate < :to order by closeApproachDate asc")
    fun getAsteroidsFromDates(from: Long, to: Long): LiveData<List<AsteroidTable>>

    @Query("delete from asteroidTable where closeApproachDate <= :date")
    suspend fun deleteOldAsteroids(date: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroids: List<AsteroidTable>)

}

