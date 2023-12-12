package com.example.testapp.domain.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.SupabaseClientSingleton
import com.example.testapp.data.models.User
import io.github.jan.supabase.exceptions.UnauthorizedRestException
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class QuizViewModel: ViewModel() {
    var user = MutableLiveData<User?>(null)

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
}