package com.example.modul2_kel28

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.modul2_kel28.model.Anime
import com.example.modul2_kel28.model.AnimeListResponse
import com.example.modul2_kel28.model.Genre
import com.example.modul2_kel28.model.GenreListResponse
import com.example.modul2_kel28.network.ApiClient

class AnimeViewModel : ViewModel() {
    private val _animeList = MutableStateFlow<List<Anime>>(emptyList())
    val animeList: StateFlow<List<Anime>> = _animeList

    private val _genreList = MutableStateFlow<List<Genre>>(emptyList())
    val genreList: StateFlow<List<Genre>> = _genreList

    private val _selectedGenre = MutableStateFlow<Genre?>(null)
    val selectedGenre: StateFlow<Genre?> = _selectedGenre

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isAscending = MutableStateFlow(true)
    val isAscending: StateFlow<Boolean> = _isAscending

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchGenres()
    }

    fun fetchTopAnime() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response: AnimeListResponse = ApiClient.service.getTopAnime()
                _animeList.value = response.data
                _selectedGenre.value = null
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchGenres() {
        viewModelScope.launch {
            try {
                val response: GenreListResponse = ApiClient.service.getAnimeGenres()
                // Filter hanya beberapa genre populer untuk kemudahan
                val popularGenres = response.data.filter {
                    it.name in listOf("Action", "Comedy", "Romance", "Drama", "Fantasy",
                        "Adventure", "Slice of Life", "Supernatural")
                }
                _genreList.value = popularGenres.sortedBy { it.name }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchAnimeByGenre(genre: Genre) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _selectedGenre.value = genre
                val response: AnimeListResponse = ApiClient.service.getAnimeByGenre(
                    genreId = genre.mal_id
                )
                _animeList.value = response.data
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearGenreFilter() {
        fetchTopAnime()
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