package com.example.modul2_kel28

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.modul2_kel28.ui.theme.Modul2_Kel28Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Modul2_Kel28Theme {
                AnimeApp()
            }
        }
    }
}

@Composable
fun AnimeApp() {
    val navController = rememberNavController()
    val items = listOf(Screen.Anime, Screen.Character, Screen.About)

    // Shared ViewModels
    val animeViewModel: AnimeViewModel = viewModel()
    val characterViewModel: CharacterViewModel = viewModel()

    Scaffold(
        bottomBar = {
            val currentBackStack by navController.currentBackStackEntryAsState()
            val currentRoute = currentBackStack?.destination?.route

            // Hanya tampilkan bottom bar jika bukan di halaman detail
            if (currentRoute != null &&
                !currentRoute.startsWith("anime_detail") &&
                !currentRoute.startsWith("character_detail")) {
                NavigationBar {
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                when (screen) {
                                    Screen.Anime -> Icon(Icons.Default.Movie, contentDescription = "Anime")
                                    Screen.Character -> Icon(Icons.Default.Person, contentDescription = "Karakter")
                                    Screen.About -> Icon(Icons.Default.Info, contentDescription = "About")
                                    else -> {}
                                }
                            },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Anime.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Anime List
            composable(Screen.Anime.route) {
                AnimeListScreen(
                    onAnimeClick = { animeId ->
                        navController.navigate(Screen.AnimeDetail.createRoute(animeId))
                    },
                    viewModel = animeViewModel
                )
            }

            // Character List
            composable(Screen.Character.route) {
                CharacterListScreen(
                    onCharacterClick = { characterId ->
                        navController.navigate(Screen.CharacterDetail.createRoute(characterId))
                    },
                    viewModel = characterViewModel
                )
            }

            // About
            composable(Screen.About.route) {
                AboutScreen()
            }

            // Anime Detail
            composable(
                route = Screen.AnimeDetail.route,
                arguments = listOf(
                    navArgument("animeId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val animeId = backStackEntry.arguments?.getInt("animeId") ?: 0
                AnimeDetailScreen(
                    animeId = animeId,
                    onBackClick = { navController.navigateUp() },
                    viewModel = animeViewModel
                )
            }

            // Character Detail
            composable(
                route = Screen.CharacterDetail.route,
                arguments = listOf(
                    navArgument("characterId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val characterId = backStackEntry.arguments?.getInt("characterId") ?: 0
                CharacterDetailScreen(
                    characterId = characterId,
                    onBackClick = { navController.navigateUp() },
                    viewModel = characterViewModel
                )
            }
        }
    }
}