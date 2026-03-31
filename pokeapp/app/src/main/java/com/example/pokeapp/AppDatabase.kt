package com.example.pokeapp

import androidx.room.Database
import androidx.room.RoomDatabase

// We tell Room which entities (tables) belong in this database
@Database(entities = [PokemonResponse::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    // This connects our DAO to the database
    abstract fun pokemonDao(): PokemonDao
}
