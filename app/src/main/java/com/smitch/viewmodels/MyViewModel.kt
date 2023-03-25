package com.smitch.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smitch.model.MyRepository
import com.smitch.model.ScanResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor( val repository: MyRepository) : ViewModel() {
    val scanResults = mutableStateListOf<ScanResult>()

    fun scan() {
        viewModelScope.launch {
            val results = repository.scan()
            scanResults.clear()
            scanResults.addAll(results)

        }
    }

    fun publish() {
        viewModelScope.launch {
            repository.publish()
        }
    }

    fun findBLE() {
        viewModelScope.launch {
            repository.findBLE()
        }
    }
}