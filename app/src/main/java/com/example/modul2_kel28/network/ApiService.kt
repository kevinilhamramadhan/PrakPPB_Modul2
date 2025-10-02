package com.example.modul2_kel28.network

import retrofit2.http.GET
import com.example.modul2_kel28.model.AnimeListResponse
import com.example.modul2_kel28.model.CharacterListResponse

interface ApiService {
    // Top Anime
    @GET("top/anime")
    suspend fun getTopAnime(): AnimeListResponse

    // Top Characters
    @GET("top/characters")
    suspend fun getTopCharacters(): CharacterListResponse
}