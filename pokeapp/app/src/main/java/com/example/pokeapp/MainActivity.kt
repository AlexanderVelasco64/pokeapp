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

                // Load data from DB after sync is done
                LaunchedEffect(Unit) {
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

fun getPokemonColor(type: String): Color {
    return when (type.lowercase()) {
        "fire" -> Color(0xFFFF421C)
        "water" -> Color(0xFF6390F0)
        "grass" -> Color(0xFF7AC74C)
        "electric" -> Color(0xFFF7D02C)
        "psychic" -> Color(0xFFF95587)
        "ice" -> Color(0xFF96D9D6)
        "dragon" -> Color(0xFF6F35FC)
        "ghost" -> Color(0xFF735797)
        "dark" -> Color(0xFF705746)
        "steel" -> Color(0xFFB7B7CE)
        "fairy" -> Color(0xFFD685AD)
        "poison" -> Color(0XFF8E38CF)
        "flying" -> Color(0xFF81C0FF)
        "bug"-> Color(0xFF8EA616)
        "fighting"-> Color(0xFFFF8C00)
        "rock"-> Color(0xFFA5A578)
        "ground"-> Color(0xFF965224)

        else -> Color.Gray // Normal
    }
}

@Composable
fun PokemonRow(pokemon: PokemonResponse) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            // Display the Sprite using Coil
            AsyncImage(
                model = pokemon.sprites.frontDefault,
                contentDescription = pokemon.name,
                modifier = Modifier.size(80.dp)
            )

            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(text = "#${pokemon.id} ${pokemon.name.uppercase()}", style = MaterialTheme.typography.headlineSmall)

                // Display types with color coding
                Row {
                    pokemon.types.forEach { typeEntry ->
                        val typeName = typeEntry.type.name
                        Surface(
                            color = getPokemonColor(typeName),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.padding(end = 4.dp, top = 4.dp)
                        ) {
                            Text(text = typeName, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

