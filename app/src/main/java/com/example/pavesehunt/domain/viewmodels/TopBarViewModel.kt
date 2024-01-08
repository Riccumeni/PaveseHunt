package com.example.pavesehunt.domain.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TopBarViewModel : ViewModel() {
    val isInQuizFragment = MutableLiveData(false)
    val screenChanged = MutableLiveData(false)
}