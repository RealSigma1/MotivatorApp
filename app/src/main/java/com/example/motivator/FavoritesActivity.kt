package com.example.motivator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class FavoritesActivity : AppCompatActivity() {
    private lateinit var sharedPrefsHelper: SharedPrefsHelper
    private lateinit var favoritesLayout: LinearLayout
    private lateinit var savedCountTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        sharedPrefsHelper = SharedPrefsHelper(this)
        favoritesLayout = findViewById(R.id.favoritesLayout)
        savedCountTextView = findViewById(R.id.savedCountTextView)

        displayFavorites()
    }

    private fun displayFavorites() {
        val favorites = sharedPrefsHelper.getFavoriteQuotes()
        favoritesLayout.removeAllViews()

        favorites.forEach { quote ->
            val quoteTextView = TextView(this).apply {
                text = quote
                textSize = 18f
                setTextColor(resources.getColor(android.R.color.white))
                setPadding(20, 10, 20, 10)
            }

            val deleteButton = Button(this).apply {
                text = "Удалить"
                setOnClickListener {
                    sharedPrefsHelper.removeFavoriteQuote(quote)
                    displayFavorites()
                }
            }

            val quoteContainer = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(10, 10, 10, 10)
                addView(quoteTextView)
                addView(deleteButton)
            }

            favoritesLayout.addView(quoteContainer)
        }

        savedCountTextView.text = "Сохранено: ${favorites.size} цитат"
    }
}
