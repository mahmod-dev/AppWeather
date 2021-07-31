package com.mahmouddev.appweather.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.mahmouddev.appweather.R
import com.mahmouddev.appweather.room.entity.WeatherCity
import com.mahmouddev.appweather.room.entity.WeatherCity.Companion.COLUMNS
import com.mahmouddev.appweather.util.Constants.CSV_FILE_NAME
import java.io.File

object CSVHelper {

    fun exportDatabaseToCSVFile(context: Context, data: List<WeatherCity>?) {
        Log.e("TAG", "exportDatabaseToCSVFile: ", )
        val csvFile = generateFile(context, CSV_FILE_NAME)
        if (csvFile != null) {
            exportFavoriteCitiesToCSVFile(context, csvFile, data)
            Toast.makeText(
                context,
                context.getString(R.string.csv_file_generated_text),
                Toast.LENGTH_LONG
            ).show()
            val intent = goToFileIntent(context, csvFile)
            context.startActivity(intent)
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.csv_file_not_generated_text),
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private fun exportFavoriteCitiesToCSVFile(
        context: Context,
        csvFile: File,
        cities: List<WeatherCity>?
    ) {
        csvWriter().open(csvFile, append = false) {
            // Header
            writeRow(COLUMNS)
            if (!cities.isNullOrEmpty()) {
                cities.forEach {
                    writeRow(
                        listOf(
                            it.city,
                            it.temp,
                            it.windSpeed,
                            it.humidity,
                            it.pressure,
                            it.lat,
                            it.lng
                        )
                    )
                }
            } else
                Toast.makeText(context, context.getString(R.string.no_data_csv), Toast.LENGTH_SHORT)
                    .show()
        }

    }
}
