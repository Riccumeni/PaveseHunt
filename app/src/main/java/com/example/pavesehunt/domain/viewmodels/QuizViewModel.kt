package com.example.testapp.domain.viewmodels

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pavesehunt.data.models.Response
import com.example.pavesehunt.data.models.Status
import com.example.testapp.common.SupabaseClientSingleton
import com.example.testapp.data.models.User
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.UnauthorizedRestException
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.FilterOperator
import kotlinx.coroutines.launch
import io.github.jan.supabase.postgrest.query.Order
import java.util.Timer
import java.util.TimerTask

class QuizViewModel: ViewModel() {
    var user = MutableLiveData<User?>(null)
    var leatherboard = MutableLiveData<Response>()
    var counter: MutableLiveData<Int> = MutableLiveData(0)
    val timer = Timer()

    fun getUser(){

        val client = SupabaseClientSingleton.getClient()

        viewModelScope.launch {
            try{
                val session = client.gotrue.currentSessionOrNull()

                if (session != null){
                    val user = client.postgrest["users"].select {
                        eq("uuid", session.user!!.id)
                    }.decodeSingle<User>()
                    this@QuizViewModel.user.value = user
                }
            }catch(err: UnauthorizedRestException){
                print("errore")
            }
        }
    }

    fun getLeatherboard(){
        viewModelScope.launch {
            val client = SupabaseClientSingleton.getClient()

            try{
                val leatherboard = client.postgrest.from("users").select (columns = Columns.list("username", "points")){
                    order("points", Order.DESCENDING)
                }.decodeList<User>()

                this@QuizViewModel.leatherboard.value = Response(Status.SUCCESS, leatherboard)

            }catch (err: BadRequestRestException){
                this@QuizViewModel.leatherboard.value = Response(Status.ERROR)
            }catch (err: HttpRequestException){
                this@QuizViewModel.leatherboard.value = Response(Status.ERROR)
            }
        }
    }

    fun startTimer(){
        val timerTask = object : TimerTask() {
            override fun run() {
                counter.postValue(counter.value!! + 1)
                println("Counter: $counter")
            }
        }

        timer.scheduleAtFixedRate(timerTask, 0, 1000)
    }

    fun stopTimer(context: Context){
        timer.cancel()
        addPoints(counter.value!!, context)
        counter.value = 0
    }

    fun addPoints(time: Int, context: Context){
        val client = SupabaseClientSingleton.getClient()

        val shared = context.getSharedPreferences("shared", Context.MODE_PRIVATE)

        val userPoints = shared.getInt("points", 0) + (1000/time)

        with(shared.edit()){
            putInt("points", userPoints )
        }

        try {
            viewModelScope.launch {
                val user: UserInfo? = client.gotrue.currentUserOrNull()

                client.postgrest.from("users").update (
                    {
                        User::points setTo userPoints
                    }
                ){
                    filter(column = "uuid", operator = FilterOperator.EQ, value = "${user?.id}")
                }
            }
        }catch (err: HttpRequestException) {

        }catch (err: BadRequestRestException){

        }
    }
}