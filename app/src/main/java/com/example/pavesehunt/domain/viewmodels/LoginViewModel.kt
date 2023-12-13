package com.example.testapp.domain.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pavesehunt.data.models.Status
import com.example.testapp.common.SupabaseClientSingleton
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.launch



class LoginViewModel: ViewModel() {

    var status = MutableLiveData(Status.LOADING)

    fun login(email: String, password: String, context: Context){

        viewModelScope.launch {

            try{
                val client = SupabaseClientSingleton.getClient()

                client.gotrue.loginWith(Email) {
                    this.email = "rzero6496@gmail.com"
                    this.password = "PaveseQuiz1!"
                }

                val session = client.gotrue.currentSessionOrNull()

                val shared = context.getSharedPreferences("shared", Context.MODE_PRIVATE)

                with(shared.edit()){
                    putString("token", session!!.accessToken)
                    apply()
                }

                status.value = Status.SUCCESS
            }catch(err: BadRequestRestException){
                status.value = Status.ERROR
            }


        }
    }
}