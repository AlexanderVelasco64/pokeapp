package com.example.pokeapp

//import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.room.Room
import coil.compose.AsyncImage

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
                val pokemonList = remember { mutableStateListOf<PokemonResponse>() }
                var pokemonName by remember { mutableStateOf("Loading...") }

                // Load data from DB after sync is done
                LaunchedEffect(Unit) {

                   try {
                       val retrofit = Retrofit.Builder()
                       .baseUrl("https://pokeapi.co/api/v2/")
                       .addConverterFactory(GsonConverterFactory.create())
                       .build()
                       val service = retrofit.create(`PokeApiService`::class.java)

                       val totalPokemon = 1025 // Current National Dex count

                       for (i in 1..totalPokemon) {
                           // Check if we already have it first!
                           val existing = db.pokemonDao().getPokemonById(i)
                           if (existing == null) {
                               pokemonName = "Downloading #$i..."
                               val result = service.getPokemonById(i)
                               db.pokemonDao().insertPokemon(result)
                           }
                           pokemonName = "Sync Complete! 1025 Pokémon Saved."

                       }
                   } catch (e: Exception) {
                       // This will show us if the internet/URL failed
                       pokemonName = "Sync Error: ${e.localizedMessage}"
                   }

                    // (Keep your existing Sync Loop here)

                    // After loop finishes, pull everything from the DB
                    val allPokemon = db.pokemonDao().getAllPokemon() // You'll need to add this to your DAO!
                    pokemonList.addAll(allPokemon)
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(pokemonList) { pokemon ->
                        PokemonRow(pokemon)
                    }
                }
            }
        }
    }
}