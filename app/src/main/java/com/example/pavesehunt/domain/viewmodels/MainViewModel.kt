package com.example.pavesehunt.domain.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pavesehunt.data.models.Status
import com.example.testapp.common.SupabaseClientSingleton
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.UnauthorizedRestException
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.launch



class MainViewModel: ViewModel() {
    val status = MutableLiveData(Status.LOADING)


    fun checkToken(token: String){
        viewModelScope.launch {
            val client = SupabaseClientSingleton.getClient()

            try{
                val user = client.auth.retrieveUser(token)
                print(user.toString())
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