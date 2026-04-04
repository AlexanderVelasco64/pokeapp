package com.example.pokeapp

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromString(value: String): List<TypeEntry> {
        val listType = object : TypeToken<List<TypeEntry>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<TypeEntry>): String = Gson().toJson(list)

    @TypeConverter
    fun fromSprites(sprites: Sprites): String = Gson().toJson(sprites)

    @TypeConverter
    fun toSprites(value: String): Sprites = Gson().fromJson(value, Sprites::class.java)
}
