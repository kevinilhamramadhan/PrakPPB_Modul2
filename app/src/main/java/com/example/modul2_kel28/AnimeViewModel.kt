package com.example.modul2_kel28

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.modul2_kel28.model.Anime
import com.example.modul2_kel28.model.AnimeListResponse
import com.example.modul2_kel28.network.ApiClient

class AnimeViewModel : ViewModel() {
    private val _animeList = MutableStateFlow<List<Anime>>(emptyList())
    val animeList: StateFlow<List<Anime>> = _animeList

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isAscending = MutableStateFlow(true)
    val isAscending: StateFlow<Boolean> = _isAscending

    fun fetchTopAnime() {
        viewModelScope.launch {
            try {
                val response: AnimeListResponse = ApiClient.service.getTopAnime()
                _animeList.value = response.data
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleSort() {
        _isAscending.value = !_isAscending.value
    }

    fun getFilteredAndSortedAnimeList(): List<Anime> {
        var result = _animeList.value

        // Filter berdasarkan search query
        if (_searchQuery.value.isNotEmpty()) {
            result = result.filter {
                it.title.contains(_searchQuery.value, ignoreCase = true)
            }
        }

        // Sort berdasarkan nama
        result = if (_isAscending.value) {
            result.sortedBy { it.title }
        } else {
            result.sortedByDescending { it.title }
        }

        return result
    }
}