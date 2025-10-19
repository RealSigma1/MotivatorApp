package com.example.motivator

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var quoteTextView: TextView
    private lateinit var savedCountTextView: TextView
    private lateinit var favoriteButton: Button
    private lateinit var shareButton: Button
    private lateinit var favoritesButton: Button
    private lateinit var nextQuoteButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var quoteRepository: QuoteRepository

    private val ioScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        quoteTextView = findViewById(R.id.quoteTextView)
        savedCountTextView = findViewById(R.id.savedCountTextView)
        favoriteButton = findViewById(R.id.favoriteButton)
        shareButton = findViewById(R.id.shareButton)
        favoritesButton = findViewById(R.id.favoritesButton)
        nextQuoteButton = findViewById(R.id.nextQuoteButton)
        sharedPreferences = getSharedPreferences("MotivatorApp", Context.MODE_PRIVATE)
        quoteRepository = QuoteRepository(this)

        updateSavedCount()
        showRandomQuote()

        favoriteButton.setOnClickListener {
            saveFavoriteQuote(quoteTextView.text.toString())
        }

        shareButton.setOnClickListener {
            shareQuote(quoteTextView.text.toString())
        }

        favoritesButton.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        nextQuoteButton.setOnClickListener {
            showRandomQuote()
        }
    }

    override fun onResume() {
        super.onResume()
        updateSavedCount()
    }

    override fun onDestroy() {
        super.onDestroy()
        ioScope.cancel()
    }

    private fun saveFavoriteQuote(quote: String) {
        val favorites = sharedPreferences.getStringSet("favorites", mutableSetOf())?.toMutableSet()
        if (!favorites!!.contains(quote)) {
            favorites.add(quote)
            sharedPreferences.edit().putStringSet("favorites", favorites).apply()
            Toast.makeText(this, "Добавлено в избранное!", Toast.LENGTH_SHORT).show()
            updateSavedCount()
        } else {
            Toast.makeText(this, "Цитата уже в избранном!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareQuote(quote: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, quote)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, "Поделиться цитатой"))
    }

    private fun updateSavedCount() {
        val favorites = sharedPreferences.getStringSet("favorites", emptySet())
        savedCountTextView.text = "Сохранено: ${favorites?.size ?: 0} цитат"
    }

    private fun showRandomQuote() {
        quoteTextView.text = "Загрузка..."

        ioScope.launch {
            val quote = quoteRepository.getRandomOnlineQuote() ?: quoteRepository.getLocalQuotes().random()
            withContext(Dispatchers.Main) {
                quoteTextView.text = quote
            }
        }
    }
}
