package com.example.pokeapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RegionSelectionScreen(onRegionSelected: (Region) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(pokemonRegions) { region ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onRegionSelected(region) },
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Text(
                    text = region.name,
                    modifier = Modifier.padding(24.dp),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

@Composable
fun PokemonListScreen(
    region: Region,
    db: AppDatabase, // Pass the database to fetch data
    onBack: () -> Unit
) {
    val pokemonList = remember { mutableStateListOf<PokemonResponse>() }
    val allPokemon = remember { mutableStateListOf<PokemonResponse>() }
    var searchQuery by remember { mutableStateOf("") }

    // 2. Create a filtered list that updates automatically
    val filteredPokemon by remember(searchQuery, allPokemon) {
        derivedStateOf {
            if (searchQuery.isBlank()) {
                allPokemon
            } else {
                allPokemon.filter {
                    it.name.contains(searchQuery, ignoreCase = true) ||
                            it.id.toString() == searchQuery
                }
            }
        }
    }

    // Load only the Pokémon for this specific region
    LaunchedEffect(region) {
        val regionalPokemon = db.pokemonDao().getPokemonByRange(region.startId, region.endId)
        pokemonList.clear()
        pokemonList.addAll(regionalPokemon)
    }

    Scaffold( modifier = Modifier.fillMaxSize())
    {
        innerPadding -> // This innerPadding handles the status bar for you!
        Column(modifier = Modifier.padding(innerPadding))
        {
            // Back Button to return to Region Selection
            Button(
                onClick = onBack,
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Back to Regions")
            }

            // 3. Add the Search Bar (TextField)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search by name or ID...") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )

            Text(
                text = "${region.name} Pokédex",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    items = filteredPokemon,
                    key = { it.id } // Performance: unique key for each item
                ) { pokemon ->
                    PokemonRow(pokemon) // Your existing row component
                }
            }

        }
    }
}
