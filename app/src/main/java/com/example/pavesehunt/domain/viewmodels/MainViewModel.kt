package com.example.pavesehunt.domain.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pavesehunt.domain.usecases.STATUS
import com.example.testapp.common.SupabaseClientSingleton
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.UnauthorizedRestException
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.launch
import java.lang.RuntimeException


class MainViewModel: ViewModel() {
    val status = MutableLiveData(STATUS.LOADING)


    suspend fun checkToken(token: String){

        val client = SupabaseClientSingleton.getClient()

        try{
            val user = client.auth.retrieveUser(token)
            status.value = STATUS.SUCCESS
        }catch (err: BadRequestRestException){
            status.value = STATUS.ERROR
        }catch (err: UnauthorizedRestException){
            status.value = STATUS.ERROR
        }catch (err: HttpRequestException){
            status.value = STATUS.ERROR
        }catch (err: Exception){
            status.value = STATUS.ERROR
        }

    }
}