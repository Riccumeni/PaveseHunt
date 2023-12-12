package com.example.testapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.models.User
import io.github.jan.supabase.createSupabaseClient
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
    var counter: MutableLiveData<Int> = MutableLiveData(0)
    val timer = Timer()

    fun startTimer(){
        val timerTask = object : TimerTask() {
            override fun run() {
                counter.postValue(counter.value!! + 1)
                println("Counter: $counter")
            }
        }

        timer.scheduleAtFixedRate(timerTask, 0, 1000)
    }

     fun getClassification(){

        val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBieGdhamJ5c2dyY2VxeXlpZm1zIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDE0MjIyMjUsImV4cCI6MjAxNjk5ODIyNX0.nWsG7AzUNBEl6yZaIYdpO9NY2OT8ODYUY-aR9VOSB78"

         viewModelScope.launch {

             val client = createSupabaseClient("https://pbxgajbysgrceqyyifms.supabase.co/", API_KEY){
                 install(Postgrest)
                 install(GoTrue)
             }

             client.gotrue.loginWith(Email) {
                 email = "rzero6496@gmail.com"
                 password = "PaveseQuiz1!"
             }

             val users = client.postgrest["users"].select().decodeList<User>()
             print(users)
         }
    }

}