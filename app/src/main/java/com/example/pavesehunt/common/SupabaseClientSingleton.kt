package com.example.testapp.common

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.security.auth.AuthPermission

object SupabaseClientSingleton {
    private val API_KEY = "API_KEY"
    private var client: SupabaseClient? = null

    fun getClient(): SupabaseClient{
        if(client == null){
            client = createSupabaseClient("URL", API_KEY){
                install(Postgrest)
                install(Auth)
                install(Storage)
            }
        }

        return client as SupabaseClient
    }
}
