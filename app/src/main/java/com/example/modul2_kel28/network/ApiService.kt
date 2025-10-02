package com.example.modul2_kel28.network

import retrofit2.http.GET
import com.example.modul2_kel28.model.AnimeListResponse

interface ApiService {
    // Anime by id
    @GET("top/anime")
    suspend fun getTopAnime(): AnimeListResponse
}