package com.example.motivator

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var quoteTextView: TextView
    private lateinit var savedCountTextView: TextView
    private lateinit var favoriteButton: Button
    private lateinit var shareButton: Button
    private lateinit var favoritesButton: Button
    private lateinit var nextQuoteButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    private val quotes = listOf(
        "Не бойся ошибаться, бойся бездействия.",
        "Успех – это сумма маленьких усилий, повторяющихся каждый день.",
        "Великие дела начинаются с малого.",
        "Каждый день – это новый шанс изменить свою жизнь.",
        "Делай сегодня то, что другие не хотят, и завтра будешь жить так, как другие не могут.",
        "Живи так, как будто завтра не наступит.",
        "Если ты можешь мечтать об этом, ты можешь это сделать.",
        "Верь в себя, и у тебя всё получится.",
        "Лучший способ предсказать будущее – создать его.",
        "Невозможное – это просто слово.",
        "Твой успех зависит только от тебя.",
        "Сложные времена рождают сильных людей.",
        "Никогда не сдавайся, великие дела требуют времени.",
        "Если не можешь лететь – беги, если не можешь бежать – иди, если не можешь идти – ползи, но всегда двигайся вперед.",
        "Будь лучше, чем вчера, но хуже, чем завтра.",
        "Чем больше работаешь, тем больше везет.",
        "Падение – это не конец, а возможность подняться сильнее.",
        "Однажды ты проснешься и поймешь, что уже стал тем, кем мечтал быть.",
        "Мечтай, верь, действуй – и все получится.",
        "Чтобы достичь цели, нужно сначала ее поставить.",
        "Вчерашний день уже не изменить, но сегодняшний день в твоих руках.",
        "Все возможно, если ты в это веришь.",
        "Люди, которые достаточно безумны, чтобы думать, что могут изменить мир, – действительно его меняют.",
        "Ты можешь больше, чем тебе кажется.",
        "Сила не в том, чтобы никогда не падать, а в том, чтобы подниматься каждый раз, когда падаешь.",
        "Все великие начинания когда-то были просто мечтой.",
        "Никто не может заставить тебя чувствовать себя неполноценным без твоего согласия.",
        "Не бойся идти вперед, бойся стоять на месте.",
        "Если ты делаешь то, что всегда делал, ты получишь то, что всегда получал.",
        "Действие – ключ к любому успеху.",
        "Нет ничего невозможного для того, кто верит.",
        "Будь тем изменением, которое ты хочешь видеть в мире.",
        "Привычка к победе начинается с маленьких побед каждый день.",
        "Всегда стремись стать лучшей версией себя.",
        "Не жди идеального момента – возьми момент и сделай его идеальным.",
        "Ты не проиграл, пока не сдался.",
        "Если хочешь чего-то достичь, начни прямо сейчас.",
        "Секрет успеха – в постоянстве и терпении.",
        "Сложности – это шанс стать сильнее.",
        "Путь в тысячу миль начинается с первого шага.",
        "Чем больше усилий, тем слаще победа.",
        "Твой потенциал не имеет границ.",
        "У тебя есть всё, что нужно, чтобы достичь успеха.",
        "Если ты чего-то хочешь, не бойся за это бороться.",
        "Работай в тишине, пусть успех говорит за тебя.",
        "Не важно, насколько медленно ты движешься – главное, не останавливаться.",
        "Не сравнивай себя с другими – сравнивай себя с собой вчерашним.",
        "Отказ – это не конец, а новый старт.",
        "Победа начинается с веры в себя.",
        "Если хочешь изменить мир – начни с себя.",
        "Будь настойчивым, и ты добьешься своего.",
        "Большая мечта начинается с первого шага.",
        "Ты уже на пути к своей мечте – не останавливайся!",
        "Лучший день для начала – сегодня."
    )

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
        quoteTextView.text = quotes.random()
    }
}
