package com.example.testapp.domain.viewmodels

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pavesehunt.common.Questions
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
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.Timer
import java.util.TimerTask

@Serializable
data class QuestionTwo(
    val id: Int? = null,
    val question: String,
    val answer: String,
    val correct_answer: Int,
    val poem: String
)

class QuizViewModel: ViewModel() {
    var leatherboard = MutableLiveData(Response(status = Status.NOT_STARTED))
    var counter: MutableLiveData<Int> = MutableLiveData(0)
    var timer = Timer()

    val questionsResponse = MutableLiveData(Response(status = Status.NOT_STARTED))


    fun getQuestions(){
        val client = SupabaseClientSingleton.getClient()

        viewModelScope.launch {
            try {
                val questions = client.postgrest.from("questions").select().decodeList<QuestionTwo>()
                questionsResponse.value = Response(status = Status.SUCCESS, data = questions)
            }catch (err: BadRequestRestException){
                questionsResponse.value = Response(status = Status.ERROR)
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
                val friendObjects: MutableList<User> = Json.decodeFromString(friends!!)

                val leatherboardFiltered: MutableList<User> = mutableListOf()

                leatherboard.forEach {
                    friendObjects.forEachIndexed { index, friend ->
                        if(friend.username.lowercase() == it.username.lowercase()){
                            leatherboardFiltered.add(it)
                        }
                    }
                }

                leatherboardFiltered.forEach {
                    val url = client.storage.from("avatars").publicUrl("${it.username}.jpg")
                    it.imageUrl = url
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
}