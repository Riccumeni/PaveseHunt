package com.example.testapp.domain.viewmodels

import android.content.Context
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
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.FilterOperator
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch



class UserViewModel: ViewModel() {

    var status = MutableLiveData(Status.LOADING)
    var statusSignOut = MutableLiveData(Status.LOADING)
    var statusSignUpEmail = MutableLiveData(Status.LOADING)

    val userResponse = MutableLiveData(Response(status = Status.NOT_STARTED))

    var usersResponse = MutableLiveData(Response(status = Status.LOADING))

    fun addPoints(time: Int, context: Context){
        val client = SupabaseClientSingleton.getClient()

        val user = userResponse.value!!.data as User

        user.points = user.points + (1000/time)

        try {
            viewModelScope.launch {

                client.postgrest.from("users").update (
                    {
                        User::points setTo user.points
                    }
                ){
                    filter(column = "uuid", operator = FilterOperator.EQ, value = "${user.uuid}")
                }
            }
        }catch (err: HttpRequestException) {

        }catch (err: BadRequestRestException){

        }
    }

    fun setProgress(actualProgress: Int){
        val client = SupabaseClientSingleton.getClient()

        val user = userResponse.value!!.data as User
        user.answer_given = user.answer_given?.plus(1)

        viewModelScope.launch {

            try{
                client.postgrest.from("users").update({
                    User::answer_given setTo actualProgress + 1
                }) {
                    filter("uuid", FilterOperator.EQ, "${user.uuid}")
                }
            }catch (err: HttpRequestException){

            }
        }
    }

    fun getUser(){

        val client = SupabaseClientSingleton.getClient()

        viewModelScope.launch {
            try{
                val session = client.auth.currentSessionOrNull()

                if (session != null){
                    val user = client.postgrest["users"].select {
                        eq("uuid", session.user!!.id)
                    }.decodeSingle<User>()

                    val url = client.storage.from("avatars").publicUrl("${user.username}.jpg")

                    user.imageUrl = url

                    this@UserViewModel.userResponse.value = Response(status = Status.SUCCESS, data = user)
                }
            }catch(err: UnauthorizedRestException){
                print("errore")
            }
        }
    }
    fun getUsers(text: String){
        viewModelScope.launch {
            val client = SupabaseClientSingleton.getClient()

            try{
                val response = client.postgrest.from("users").select(columns = Columns.list("username", "points")){
                    User::username ilike "%${text}%"
                }.decodeList<User>()

                usersResponse.value = Response(status = Status.SUCCESS, data = response)
            }catch (err: BadRequestRestException){
                usersResponse.value!!.status = Status.ERROR
            }catch (err: HttpRequestException){
                usersResponse.value!!.status = Status.ERROR
            }
        }
    }

    fun signOut(context: Context){
        viewModelScope.launch {
            val client = SupabaseClientSingleton.getClient()
            val shared = context.getSharedPreferences("shared", Context.MODE_PRIVATE)

            try{
                client.auth.clearSession()

                with(shared.edit()){
                    putString("token", "")
                    apply()
                }

                statusSignOut.value = Status.SUCCESS
            }catch (err: BadRequestRestException){
                statusSignOut.value = Status.ERROR
            }
        }
    }

    fun login(email: String, password: String, context: Context){

        viewModelScope.launch {

            try{
                val client = SupabaseClientSingleton.getClient()

                client.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }


                client.auth.refreshCurrentSession()

                val currentSession = client.auth.currentSessionOrNull()

                val shared = context.getSharedPreferences("shared", Context.MODE_PRIVATE)

                with(shared.edit()){
                    putString("token", currentSession!!.accessToken)
                    apply()
                }

                status.value = Status.SUCCESS
            }catch(err: BadRequestRestException){
                status.value = Status.ERROR
            }
        }
    }

    fun signUpEmail(email: String, password: String, username: String, image: ByteArray, context: Context){
        viewModelScope.launch {
            val client = SupabaseClientSingleton.getClient()

            try{

                val result = client.auth.signUpWith(Email){
                    this.email = email
                    this.password = password
                }

                val user = User(username = username, points = 0, uuid = result!!.id)

                client.postgrest.from("users").insert(user)

                /*
                val shared = context.getSharedPreferences("shared", Context.MODE_PRIVATE)

                with(shared.edit()){
                    putString("token", session.accessToken)
                    apply()
                }
                 */

                val bucket = client.storage.from("avatars")
                bucket.upload("${username}.jpg", image, upsert = false)

                statusSignUpEmail.value = Status.SUCCESS

            }catch(err:BadRequestRestException){
                statusSignUpEmail.value = Status.ERROR
            }catch (err: HttpRequestException){
                statusSignUpEmail.value = Status.ERROR
            }
        }
    }


}