package com.example.modul2_kel28

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter

@Composable
fun AnimeListScreen(viewModel: AnimeViewModel = viewModel()) {
    val animeList by viewModel.animeList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchTopAnime()
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        items(animeList) { anime ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(anime.images.jpg.image_url),
                        contentDescription = anime.title,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(anime.title)
                        Text("Type: ${anime.type ?: "-"}")
                        Text("Episodes: ${anime.episodes ?: 0}")
                        Text("Score: ${anime.score ?: "N/A"}")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimeListScreenPreview(){
    AboutScreen()
}