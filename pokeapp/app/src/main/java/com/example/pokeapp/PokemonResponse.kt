package com.example.pokeapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pokemon_table")
data class PokemonResponse(
    @PrimaryKey
    val id: Int, // Pokedex Number
    val name: String, // We'll use the name as the unique ID
    val weight: Int,
    val sprites: Sprites,
    val types: List<TypeEntry>
)

data class Sprites(
    @SerializedName("front_default") val frontDefault: String
)

data class TypeEntry(
    val type: TypeInfo
)

data class TypeInfo(
    val name: String
)