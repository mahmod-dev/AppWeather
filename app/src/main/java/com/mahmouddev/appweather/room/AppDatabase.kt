package com.mahmouddev.appweather.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mahmouddev.appweather.room.dao.DaoWeather
import com.mahmouddev.appweather.room.entity.WeatherCity
import com.mahmouddev.appweather.room.entity.WeatherUser

@Database(entities = [WeatherCity::class,WeatherUser::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun daoWeather(): DaoWeather


}