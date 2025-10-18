package com.example.motivator

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MotivatorApp", Context.MODE_PRIVATE)

    fun saveFavoriteQuote(quote: String) {
        val favorites = getFavoriteQuotes().toMutableSet()
        favorites.add(quote)
        sharedPreferences.edit().putStringSet("favorites", favorites).apply()
    }

    fun getFavoriteQuotes(): Set<String> {
        return sharedPreferences.getStringSet("favorites", emptySet()) ?: emptySet()
    }

    fun removeFavoriteQuote(quote: String) {
        val favorites = getFavoriteQuotes().toMutableSet()
        favorites.remove(quote)
        sharedPreferences.edit().putStringSet("favorites", favorites).apply()
    }

    fun getFavoriteCount(): Int {
        return getFavoriteQuotes().size
    }
}
