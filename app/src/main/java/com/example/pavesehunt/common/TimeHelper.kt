package com.example.pavesehunt.common

import java.util.Calendar

object TimeHelper {

    fun getDayOfWeek(day: Int, month: Int, year: Int): Int {
        // Create a Calendar instance
        val calendar = Calendar.getInstance()

        // Set the year, month, and day
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1) // Note: Calendar months are 0-based
        calendar.set(Calendar.DAY_OF_MONTH, day)

        // Get the day of the week (Sunday is 1, Monday is 2, and so on)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        // Convert the day of the week to a human-readable string
        val dayOfWeekString = when (dayOfWeek) {
            Calendar.SUNDAY -> "Domenica"
            Calendar.MONDAY -> "Lunedi"
            Calendar.TUESDAY -> "Martedi"
            Calendar.WEDNESDAY -> "Mercoledi"
            Calendar.THURSDAY -> "Giovedi"
            Calendar.FRIDAY -> "Venerdi"
            Calendar.SATURDAY -> "Sabato"
            else -> "Unknown"
        }

        return dayOfWeek
    }

    fun getMonthNameByNumber(number: Int) : String{
        lateinit var month: String

        when(number){
            1 -> {
                month = "Gennaio"
            }

            2 -> {
                month = "Febbraio"
            }

            3 -> {
                month = "Marzo"
            }

            4 -> {
                month = "Aprile"
            }

            5 -> {
                month = "Maggio"
            }

            6 -> {
                month = "Giugno"
            }

            7 -> {
                month = "Luglio"
            }

            8 -> {
                month = "Agosto"
            }

            9 -> {
                month = "Settembre"
            }

            10 -> {
                month = "Ottobre"
            }

            11 -> {
                month = "Novembre"
            }

            12 -> {
                month = "Dicembre"
            }
        }

        return month
    }
}