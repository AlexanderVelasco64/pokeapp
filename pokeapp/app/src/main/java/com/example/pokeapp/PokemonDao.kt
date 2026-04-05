package com.example.pokeapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonDao {
    // This looks for a specific pokemon by name in your local table
    @Query("SELECT * FROM pokemon_table WHERE name = :name LIMIT 1")
    suspend fun getPokemon(name: String): PokemonResponse?

    // This saves a pokemon. If it already exists, it "REPLACES" it (updates it)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: PokemonResponse)

    @Query("SELECT * FROM pokemon_table ORDER BY id ASC")
    suspend fun getAllPokemon(): List<PokemonResponse>

}
