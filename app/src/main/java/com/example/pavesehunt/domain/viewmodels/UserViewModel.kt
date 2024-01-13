package com.example.testapp.domain.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pavesehunt.data.models.Response
import com.example.pavesehunt.domain.usecases.ErrorCodes
import com.example.pavesehunt.domain.usecases.ErrorException
import com.example.pavesehunt.domain.usecases.STATUS
import com.example.testapp.common.SupabaseClientSingleton
import com.example.testapp.data.models.User
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.UnauthorizedRestException
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.FilterOperator
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.UUID

class UserViewModel: ViewModel() {

    var status = MutableLiveData(STATUS.LOADING)
    var statusSignOut = MutableLiveData(STATUS.LOADING)
    var statusResetPoints = MutableLiveData(STATUS.LOADING)
    var statusSignUpEmail = MutableLiveData(Response(status = STATUS.NOT_STARTED))
    val userResponse = MutableLiveData(Response(status = STATUS.NOT_STARTED))
    var usersResponse = MutableLiveData(Response(status = STATUS.LOADING))

    fun resetPoints(){
        val client = SupabaseClientSingleton.getClient()

        val user = userResponse.value!!.data as User

        viewModelScope.launch {
            try {
                client.postgrest.from("users").update (
                    {
                        User::points setTo 0
                        User::answer_given setTo 0
                    }
                ){
                    filter(column = "uuid", operator = FilterOperator.EQ, value = "${user.uuid}")
                }.decodeSingle<User>()

                user.points = 0
                user.answer_given = 0

                statusResetPoints.value = STATUS.SUCCESS

            }catch (err: Exception){
                statusResetPoints.value = STATUS.ERROR
            }
        }
    }

    fun sendResult(time: Int, isCorrect: Boolean, actualProgress: Int){
        val client = SupabaseClientSingleton.getClient()

        val user = userResponse.value!!.data as User

        user.points = user.points + (1000/time)

        try {
            viewModelScope.launch {

                if(isCorrect){
                    client.postgrest.from("users").update (
                        {
                            User::points setTo user.points
                            User::answer_given setTo actualProgress + 1
                        }
                    ){
                        filter(column = "uuid", operator = FilterOperator.EQ, value = "${user.uuid}")
                    }
                }else{
                    client.postgrest.from("users").update (
                        {
                            User::answer_given setTo actualProgress + 1
                        }
                    ){
                        filter(column = "uuid", operator = FilterOperator.EQ, value = "${user.uuid}")
                    }
                }
            }
        }catch (err: HttpRequestException) {

        }catch (err: BadRequestRestException){

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

                    this@UserViewModel.userResponse.value = Response(status = STATUS.SUCCESS, data = user)
                }
            }catch(err: UnauthorizedRestException){
                print("errore")
            }
        }
    }
    fun getUsers(text: String, context: Context){
        viewModelScope.launch {
            val client = SupabaseClientSingleton.getClient()

            try{
                val response = client.postgrest.from("users").select(columns = Columns.list("id", "username", "points")){
                    User::username ilike "%${text}%"
                }.decodeList<User>()

                response.forEach {
                    if(isFriend(it.id!!)){
                        it.isFriend = true
                    }
                }

                usersResponse.value = Response(status = STATUS.SUCCESS, data = response)
            }catch (err: BadRequestRestException){
                usersResponse.value!!.status = STATUS.ERROR
            }catch (err: HttpRequestException){
                usersResponse.value!!.status = STATUS.ERROR
            }
        }
    }

    private fun isFriend(userId: Int): Boolean{
        var  isFriend = false

        val user = userResponse.value!!.data as User
        val userFriends: MutableList<Int> = Json.decodeFromString(user.friends)

        userFriends.forEach {
            if(it == userId){
                isFriend = true
            }
        }

        return isFriend
    }

    fun setFriends(newFriends: String, uuid: String){
        viewModelScope.launch {
            val client = SupabaseClientSingleton.getClient()

            client.postgrest.from("users").update (
                {
                    User::friends setTo newFriends
                }
            ){
                filter(column = "uuid", operator = FilterOperator.EQ, value = uuid)
            }.decodeSingle<User>()

            val user = userResponse.value!!.data as User
            user.friends = newFriends
        }
    }

    fun getFriends(context: Context, text: String?){
        viewModelScope.launch {
            val client = SupabaseClientSingleton.getClient()

            try{

                lateinit var response: List<User>

                if(text == null){
                    response = client.postgrest.from("users").select(columns = Columns.list("id", "username", "points")){}.decodeList<User>()
                }else{
                    response = client.postgrest.from("users").select(columns = Columns.list("id", "username", "points")){
                        User::username ilike "%${text}%"
                    }.decodeList<User>()
                }

                response = response.filter {
                    isFriend(it.id!!)
                }

                /*
                response = response.filter {
                    isFriend(it, context)
                }
                */

                response.forEach {
                    it.isFriend = true
                }

                usersResponse.value = Response(status = STATUS.SUCCESS, data = response)
            }catch (err: BadRequestRestException){
                usersResponse.value!!.status = STATUS.ERROR
            }catch (err: HttpRequestException){
                usersResponse.value!!.status = STATUS.ERROR
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

                statusSignOut.value = STATUS.SUCCESS
            }catch (err: BadRequestRestException){
                statusSignOut.value = STATUS.ERROR
            }
        }
    }

    fun login(email: String, password: String, context: Context){
        viewModelScope.launch {

            try{
                if(!isValidEmail(email)){
                    throw Exception("Invalid email")
                }

                if(password.isEmpty()){
                    throw Exception("Password is too short")
                }

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

                status.value = STATUS.SUCCESS
            }catch(err: BadRequestRestException){
                status.value = STATUS.ERROR
            }catch (err: HttpRequestException){
                status.value = STATUS.ERROR
            } catch (err: Exception){
                status.value = STATUS.ERROR
            }

        }
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")
        return emailRegex.matches(email)
    }

    fun signUpEmail(email: String, password: String, repeatedPassword: String, username: String, image: ByteArray, context: Context){

        try{
            if(!isValidEmail(email)){
                throw ErrorException(ErrorCodes.EMAIL_WRONG)
            }

            if(password.length <= 5){
                throw ErrorException(ErrorCodes.PASSWORD_TOO_SHORT)
            }

            if(password != repeatedPassword){
                throw ErrorException(ErrorCodes.PASSWORDS_NOT_EQUAL)
            }
        }catch (err: ErrorException){

            statusSignUpEmail.value = Response(status = STATUS.ERROR, code = err.errorCode, message = err.message.toString())

            return
        }

        viewModelScope.launch {
            val client = SupabaseClientSingleton.getClient()

            var usernameAlreadyExist = false

            try {
                val usernameIsAlreadyToken = client.postgrest.from("users").select { filter("username", FilterOperator.EQ, username) }.decodeSingle<User>()
                print(usernameIsAlreadyToken.username)

            }catch (err: Exception){
                usernameAlreadyExist = true
            }

            try{
                if(!usernameAlreadyExist){
                    throw Exception("username is already token")
                }


                val result = client.auth.signUpWith(Email){
                    this.email = email
                    this.password = password
                }

                val user = User(username = username.lowercase(), points = 0, uuid = result!!.id)

                client.postgrest.from("users").insert(user)

                val bucket = client.storage.from("avatars")
                bucket.upload("${username.lowercase()}.jpg", image, upsert = false)

                statusSignUpEmail.value = Response(status = STATUS.SUCCESS)

            } catch(err:BadRequestRestException){
                statusSignUpEmail.value = Response(status = STATUS.ERROR)
            }catch (err: HttpRequestException){
                statusSignUpEmail.value = Response(status = STATUS.ERROR, code = ErrorCodes.INTERNET_CONNECTION, message = "Check your internet connection")
            }catch (err: Exception){
                statusSignUpEmail.value = Response(status = STATUS.ERROR, code = ErrorCodes.USERNAME_ALREADY_TOKEN, message = err.message.toString())
            }
        }
    }


}