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

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isAscending = MutableStateFlow(true)
    val isAscending: StateFlow<Boolean> = _isAscending

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

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleSort() {
        _isAscending.value = !_isAscending.value
    }

    fun getFilteredAndSortedCharacterList(): List<Character> {
        var result = _characterList.value

        // Filter berdasarkan search query
        if (_searchQuery.value.isNotEmpty()) {
            result = result.filter {
                it.name.contains(_searchQuery.value, ignoreCase = true)
            }
        }

        // Sort berdasarkan nama
        result = if (_isAscending.value) {
            result.sortedBy { it.name }
        } else {
            result.sortedByDescending { it.name }
        }

        return result
    }
}