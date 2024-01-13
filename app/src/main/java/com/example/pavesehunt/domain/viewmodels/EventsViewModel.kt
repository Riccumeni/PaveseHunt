package com.example.pavesehunt.domain.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pavesehunt.data.models.Event
import com.example.pavesehunt.data.models.Response
import com.example.pavesehunt.domain.usecases.STATUS
import com.example.testapp.common.SupabaseClientSingleton
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.FilterOperator
import kotlinx.coroutines.launch

class EventsViewModel: ViewModel() {
    val eventsResponse = MutableLiveData(Response(status = STATUS.NOT_STARTED))
    val selectedDay = MutableLiveData<Int>()
    var previousDay: Int? = null

    fun getEventsByMonthAndYear(month: Int, year: Int){
        val client = SupabaseClientSingleton.getClient()

        viewModelScope.launch {
            try {
                val response = client.postgrest.from("events").select {
                    filter(column = "month", FilterOperator.EQ, month)
                    filter(column = "year", FilterOperator.EQ, year)
                }.decodeList<Event>()

                eventsResponse.value = Response(status = STATUS.SUCCESS, data = response)

            }catch (err: HttpRequestException){
                eventsResponse.value = Response(status = STATUS.ERROR)
            }
        }
    }
}