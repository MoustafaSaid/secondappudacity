package com.example.myapplication.Data.Local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.Asteroid
import com.example.myapplication.Constants.API_QUERY_DATE_FORMAT
import java.text.SimpleDateFormat

@Entity
data class AsteroidTable(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: Long,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)
fun List<AsteroidTable>.asDomainModel(): List<Asteroid> {
    return map {
        val date = SimpleDateFormat(API_QUERY_DATE_FORMAT).format(it.closeApproachDate)

        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = date,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}
fun List<Asteroid>.asDatabaseModel(): List<AsteroidTable> {
    return map {
        val endDate = SimpleDateFormat(API_QUERY_DATE_FORMAT).parse(it.closeApproachDate.toString())
        AsteroidTable(
            id = it.id,
            codename = it.codename,
            closeApproachDate = endDate?.time ?: 0L,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}