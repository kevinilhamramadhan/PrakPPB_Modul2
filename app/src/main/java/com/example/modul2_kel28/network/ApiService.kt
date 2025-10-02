package com.example.modul2_kel28.network

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.modul2_kel28.model.AnimeListResponse
import com.example.modul2_kel28.model.CharacterListResponse
import com.example.modul2_kel28.model.GenreListResponse

interface ApiService {
    // Top Anime
    @GET("top/anime")
    suspend fun getTopAnime(): AnimeListResponse

    // Top Characters
    @GET("top/characters")
    suspend fun getTopCharacters(): CharacterListResponse

    // Get Anime Genres
    @GET("genres/anime")
    suspend fun getAnimeGenres(): GenreListResponse

    // Get Anime by Genre
    @GET("anime")
    suspend fun getAnimeByGenre(
        @Query("genres") genreId: Int,
        @Query("order_by") orderBy: String = "popularity",
        @Query("limit") limit: Int = 25
    ): AnimeListResponse
}