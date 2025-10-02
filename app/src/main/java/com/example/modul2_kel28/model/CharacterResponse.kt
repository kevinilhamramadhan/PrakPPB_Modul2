package com.example.modul2_kel28.model

data class CharacterListResponse(
    val data: List<Character>
)

data class Character(
    val mal_id: Int,
    val name: String,
    val images: CharacterImages,
    val about: String?
)

data class CharacterImages(
    val jpg: CharacterJpg
)

data class CharacterJpg(
    val image_url: String
)