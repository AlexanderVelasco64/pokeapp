#!/usr/bin/env kotlin

import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {
    @GET("pokemon/{id}")
    suspend fun getPokemonById(@Path("id") id: Int): PokemonResponse
}
