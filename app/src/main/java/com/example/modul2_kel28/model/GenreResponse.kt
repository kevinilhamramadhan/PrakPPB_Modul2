package com.example.modul2_kel28.model

data class GenreListResponse(
    val data: List<Genre>
)

data class Genre(
    val mal_id: Int,
    val name: String,
    val url: String,
    val count: Int
)

data class AnimeGenre(
    val mal_id: Int,
    val name: String
)