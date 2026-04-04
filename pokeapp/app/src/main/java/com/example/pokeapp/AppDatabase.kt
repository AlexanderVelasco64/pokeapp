package com.example.pokeapp

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(Converters::class) // Add this!
@Database(entities = [PokemonResponse::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    // This connects our DAO to the database
    abstract fun pokemonDao(): PokemonDao
}
