package com.example.modul2_kel28

sealed class Screen(val route: String, val title: String) {
    object Anime : Screen("anime", "Anime")
    object Character : Screen("character", "Karakter")
    object About : Screen("about", "About")
    object AnimeDetail : Screen("anime_detail/{animeId}", "Anime Detail") {
        fun createRoute(animeId: Int) = "anime_detail/$animeId"
    }
    object CharacterDetail : Screen("character_detail/{characterId}", "Character Detail") {
        fun createRoute(characterId: Int) = "character_detail/$characterId"
    }
}