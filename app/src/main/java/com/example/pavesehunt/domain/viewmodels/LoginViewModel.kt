package com.example.testapp.domain.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.SupabaseClientSingleton
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.launch

enum class Status{
    SUCCESS,
    LOADING,
    ERROR
}

class LoginViewModel: ViewModel() {

    var status = MutableLiveData(Status.LOADING)

    fun login(email: String, password: String){
        val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBieGdhamJ5c2dyY2VxeXlpZm1zIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDE0MjIyMjUsImV4cCI6MjAxNjk5ODIyNX0.nWsG7AzUNBEl6yZaIYdpO9NY2OT8ODYUY-aR9VOSB78"

        viewModelScope.launch {

            try{
                val client = SupabaseClientSingleton.getClient()

                client.gotrue.loginWith(Email) {
                    this.email = "rzero6496@gmail.com"
                    this.password = "PaveseQuiz1!"
                }

                val session = client.gotrue.currentSessionOrNull()

                status.value = Status.SUCCESS
            }catch(err: BadRequestRestException){
                status.value = Status.ERROR
            }


        }
    }
}