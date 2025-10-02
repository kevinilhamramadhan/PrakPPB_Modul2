package com.example.modul2_kel28

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    onCharacterClick: (Int) -> Unit,
    viewModel: CharacterViewModel
) {
    val characterList by viewModel.characterList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isAscending by viewModel.isAscending.collectAsState()

    LaunchedEffect(Unit) {
        if (characterList.isEmpty()) {
            viewModel.fetchTopCharacters()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Search Bar dan Sorting Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search TextField
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Cari karakter...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            // Sort Button
            FilledTonalIconButton(
                onClick = { viewModel.toggleSort() },
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SortByAlpha,
                    contentDescription = if (isAscending) "Sort Z-A" else "Sort A-Z",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Indicator Sorting
        if (searchQuery.isEmpty()) {
            Text(
                text = if (isAscending) "Urutan: A → Z" else "Urutan: Z → A",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        // Character List Content
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading && characterList.isEmpty()) {
                // Loading state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Memuat data karakter...")
                }
            } else {
                val filteredAndSortedList = viewModel.getFilteredAndSortedCharacterList()

                if (filteredAndSortedList.isEmpty() && searchQuery.isNotEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Tidak ada hasil",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Coba kata kunci lain",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
                        items(filteredAndSortedList) { character ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .clickable { onCharacterClick(character.mal_id) },
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Foto Karakter (bulat)
                                    Image(
                                        painter = rememberAsyncImagePainter(character.images.jpg.image_url),
                                        contentDescription = character.name,
                                        modifier = Modifier
                                            .size(90.dp)
                                            .padding(12.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )

                                    // Nama Karakter
                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .weight(1f)
                                            .padding(vertical = 16.dp, horizontal = 8.dp),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = character.name,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}