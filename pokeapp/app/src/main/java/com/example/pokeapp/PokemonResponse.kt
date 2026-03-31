package com.example.pokeapp

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "pokemon_table")
data class PokemonResponse(
    @PrimaryKey
    val name: String, // We'll use the name as the unique ID
    val weight: Int
)
/*
data class PokemonResponse(
    val name: String,
    val weight: Int
)
 */