package com.example.pokeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pokeapp.ui.theme.PokeappTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.room.Room


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // This creates the physical database file on the phone
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "pokemon-database"
        ).build()

        setContent {
            PokeappTheme {
                // 1. State variable to hold the UI text
                var pokemonName by remember { mutableStateOf("Loading...") }

                // 2. Fetch data when the screen loads
                LaunchedEffect(Unit) {
                    try {
                        val retrofit = Retrofit.Builder()
                            .baseUrl("https://pokeapi.co/api/v2/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()

                        val service = retrofit.create(`PokeApiService`::class.java)

                        // LOOP: Fetch 1 to 151
                        for (i in 1..151) {
                            pokemonName = "Syncing Pokémon #$i..." // Update the UI so you see progress

                            val result = service.getPokemonById(i)
                            db.pokemonDao().insertPokemon(result)
                        }
                        pokemonName = "Sync Complete! 151 Pokémon Saved."

                        //val result = service.getPokemonByName("pikachu")
                        // This saves Pikachu to your local phone storage!
                        //db.pokemonDao().insertPokemon(result)
                        //pokemonName = "Found & Saved: ${result.name} (Weight: ${result.weight})"
                    } catch (e: Exception) {
                        // This will show us if the internet/URL failed
                        pokemonName = "Sync Error: ${e.localizedMessage}"
                    }
                }

                // 3. Simple UI to show the result
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text(
                        text = pokemonName,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokeappTheme {
        Greeting("Android")
    }
}