package com.mahmouddev.appweather.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.mahmouddev.appweather.room.AppDatabase
import com.mahmouddev.appweather.room.dao.DaoWeather
import com.mahmouddev.appweather.room.entity.WeatherCity
import com.mahmouddev.appweather.room.entity.WeatherUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class UserDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var daoWeather: DaoWeather

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        daoWeather = database.daoWeather()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insert_get_returnNotNull_user() = runBlockingTest {
        val user = WeatherUser(
            city = "Gaza",
            temp = 30.5,
            windSpeed = 1000.5,
            humidity = 1,
            pressure = 1,
            lat = 30.3,
            lng = 34.5,
        )
        daoWeather.insertUserWeather(user)
        val userWeather = daoWeather.currentUserWeather(30.3, 34.5)
        assertNotNull(userWeather)

    }

    @Test
    fun insert_insert_get_returnEqual_user() = runBlockingTest {
        // lat, lng primary keys
        // insert or replace
        val user1 = WeatherUser(
            city = "Gaza",
            temp = 30.5,
            windSpeed = 1000.5,
            humidity = 1,
            pressure = 1,
            lat = 30.3,
            lng = 34.5,
        )

        val user2 = WeatherUser(
            city = "London",
            temp = 30.5,
            windSpeed = 1000.5,
            humidity = 1,
            pressure = 1,
            lat = 30.3,
            lng = 34.5,
        )

        daoWeather.insertUserWeather(user1)
        daoWeather.insertUserWeather(user2)
        val userWeather = daoWeather.currentUserWeather(30.3, 34.5)
        assertEquals("London", userWeather?.city)
    }

    @Test
    fun insert_insert_get_returnEqual_city() = runBlockingTest {
        // city is primary key
        // insert or ignore
        val user1 = WeatherCity(
            city = "Gaza",
            temp = 30.5,
            windSpeed = 1000.5,
            humidity = 1,
            pressure = 1,
            lat = 30.3,
            lng = 34.5,
        )

        val user2 = WeatherCity(
            city = "Gaza",
            temp = 30.5,
            windSpeed = 1000.5,
            humidity = 1,
            pressure = 1,
            lat = 30.3,
            lng = 34.5,
        )

        daoWeather.insertCity(user1)
        val id = daoWeather.insertCity(user2)
        daoWeather.cityAdded("Gaza")
        assertEquals(-1, id)
    }


}