package com.example.pavesehunt.domain.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pavesehunt.data.models.Collection
import com.example.pavesehunt.data.models.Response
import com.example.pavesehunt.domain.usecases.STATUS
import com.example.testapp.common.SupabaseClientSingleton
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch

class CollectionViewModel: ViewModel() {
    val collectionsResponse = MutableLiveData(Response(status = STATUS.NOT_STARTED))
    val collectionsFiltered = MutableLiveData<List<Collection>>()
    var collectionSelected: Collection? = null

    fun getCollections(){
        val client = SupabaseClientSingleton.getClient()

        collectionsResponse.value = Response(status = STATUS.LOADING)

        viewModelScope.launch {
            try {
                val collections = client.postgrest.from("collections").select().decodeList<Collection>()

                collections.forEach {
                    val url = client.storage.from("collections").publicUrl("${it.id}.jpeg")
                    it.image = url
                }

                collectionsFiltered.value = collections

                collectionsResponse.value = Response(status = STATUS.SUCCESS, data = collections)
            }catch (err: HttpRequestException){
                collectionsResponse.value = Response(status = STATUS.ERROR)
            }catch (err: BadRequestRestException){
                collectionsResponse.value = Response(status = STATUS.ERROR)
            }
        }
    }

    fun getCollectionsFiltered(text: String){
        var collections = collectionsResponse.value!!.data as List<Collection>

        val pattern = ("^$text.*").toRegex()

        collectionsFiltered.value = collections.filter {
            pattern.matches(it.title)
        }
    }

    fun setCollection(tappedCollection: Collection){
        collectionSelected = tappedCollection
    }
}