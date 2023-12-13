package com.example.pavesehunt.domain.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pavesehunt.data.models.Status
import com.example.testapp.common.SupabaseClientSingleton
import com.example.testapp.data.models.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.UnauthorizedRestException
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.Timer
import java.util.TimerTask


class MainViewModel: ViewModel() {
    val status = MutableLiveData(Status.LOADING)


    fun checkToken(token: String){
        viewModelScope.launch {
            val client = SupabaseClientSingleton.getClient()

            try{
                val user = client.gotrue.retrieveUser(token)
                status.value = Status.SUCCESS
            }catch (err: BadRequestRestException){
                status.value = Status.ERROR
            }catch (err: UnauthorizedRestException){
                status.value = Status.ERROR
            }catch (err: HttpRequestException){
                status.value = Status.ERROR
            }




        }
    }
}