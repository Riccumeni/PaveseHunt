package com.example.testapp.common

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClientSingleton {
    private val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBieGdhamJ5c2dyY2VxeXlpZm1zIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDE0MjIyMjUsImV4cCI6MjAxNjk5ODIyNX0.nWsG7AzUNBEl6yZaIYdpO9NY2OT8ODYUY-aR9VOSB78"
    private var client: SupabaseClient? = null

    fun getClient(): SupabaseClient{
        if(client == null){
            client = createSupabaseClient("https://pbxgajbysgrceqyyifms.supabase.co/", API_KEY){
                install(Postgrest)
                install(GoTrue)
            }
        }

        return client as SupabaseClient
    }
}