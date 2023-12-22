package com.example.testapp.domain.viewmodels

import android.content.Context
import android.util.Log
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
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.FilterOperator
import kotlinx.coroutines.launch
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.storage
import java.util.Timer
import java.util.TimerTask

class QuizViewModel: ViewModel() {
    var user = MutableLiveData<User?>(null)
    var leatherboard = MutableLiveData(Response(status = Status.NOT_STARTED))
    var counter: MutableLiveData<Int> = MutableLiveData(0)
    var timer = Timer()

    fun getUser(){

        val client = SupabaseClientSingleton.getClient()

        viewModelScope.launch {
            try{
                val session = client.auth.currentSessionOrNull()

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

        leatherboard.value!!.data = mutableListOf<User>()
        viewModelScope.launch {
            val client = SupabaseClientSingleton.getClient()

            this@QuizViewModel.leatherboard.value!!.status = Status.LOADING

            try{
                val leatherboard = client.postgrest.from("users").select (columns = Columns.list("username", "points")){
                    order("points", Order.DESCENDING)
                }.decodeList<User>()

                leatherboard.forEach {
                    val url = client.storage.from("avatars").publicUrl("${it.username}.jpg")
                    it.imageUrl = url
                }

                this@QuizViewModel.leatherboard.value = Response(Status.SUCCESS, leatherboard)

            }catch (err: BadRequestRestException){
                this@QuizViewModel.leatherboard.value = Response(Status.ERROR)
            }catch (err: HttpRequestException){
                this@QuizViewModel.leatherboard.value = Response(Status.ERROR)
            }
        }
    }

    fun getFriendsLeatherboard(context: Context){
        leatherboard.value!!.data = mutableListOf<User>()
        viewModelScope.launch {
            val client = SupabaseClientSingleton.getClient()

            this@QuizViewModel.leatherboard.value!!.status = Status.LOADING

            try{
                val leatherboard = client.postgrest.from("users").select (columns = Columns.list("username", "points")){
                    order("points", Order.DESCENDING)
                }.decodeList<User>()

                val shared = context.getSharedPreferences("shared", Context.MODE_PRIVATE)

                val friends = shared.getString("friends", "[]")

                val leatherboardFiltered: MutableList<User> = mutableListOf()

                leatherboard.forEach {
                    if(friends!!.contains(it.username)){
                        leatherboardFiltered.add(it)
                    }
                }

                this@QuizViewModel.leatherboard.value = Response(Status.SUCCESS, leatherboardFiltered)

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

                Log.d("timer", counter.value.toString())
            }
        }

        timer.scheduleAtFixedRate(timerTask, 0, 1000)
    }

    fun stopTimer(){
        timer.cancel()
        counter.value = 0
        timer = Timer()
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
                val user: UserInfo? = client.auth.currentUserOrNull()

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