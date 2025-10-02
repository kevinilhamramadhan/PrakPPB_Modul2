package com.example.modul2_kel28

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.modul2_kel28.model.Character
import com.example.modul2_kel28.model.CharacterListResponse
import com.example.modul2_kel28.network.ApiClient

class CharacterViewModel : ViewModel() {
    private val _characterList = MutableStateFlow<List<Character>>(emptyList())
    val characterList: StateFlow<List<Character>> = _characterList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchTopCharacters() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response: CharacterListResponse = ApiClient.service.getTopCharacters()
                _characterList.value = response.data
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}