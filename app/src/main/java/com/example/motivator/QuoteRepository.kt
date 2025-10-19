package com.example.motivator

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class QuoteRepository(private val context: Context) {

    private val quotesUrl =
        "https://batmanapollo.ru/100-%D1%86%D0%B8%D1%82%D0%B0%D1%82-%D0%B2%D0%B5%D0%BB%D0%B8%D0%BA%D0%B8%D1%85-%D0%BB%D1%8E%D0%B4%D0%B5%D0%B9-%D1%86%D0%B8%D1%82%D0%B0%D1%82%D1%8B-%D0%B2%D0%B5%D0%BB%D0%B8%D0%BA%D0%B8%D1%85-%D0%BB/"

    private var onlineQuotesCache: List<String>? = null

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    suspend fun fetchQuotesFromWeb(): List<String>? = withContext(Dispatchers.IO) {
        try {
            if (!isNetworkAvailable()) {
                Log.d("QuoteRepository", "Нет интернета")
                return@withContext null
            }

            Log.d("QuoteRepository", "Загрузка цитат с сайта...")
            val doc = Jsoup.connect(quotesUrl).get()

            val paragraphs = doc.select("p")
            val quotes = paragraphs.mapNotNull { p ->
                val text = p.text().trim()
                if ((text.contains("—") || text.contains("-")) &&
                    text.length in 40..200 // ограничение длины
                ) text else null
            }

            Log.d("QuoteRepository", "Найдено цитат: ${quotes.size}")

            if (quotes.isNotEmpty()) {
                onlineQuotesCache = quotes
                quotes
            } else null
        } catch (e: Exception) {
            Log.e("QuoteRepository", "Ошибка парсинга: ${e.message}", e)
            null
        }
    }

    suspend fun getRandomOnlineQuote(): String? {
        if (onlineQuotesCache == null) {
            onlineQuotesCache = fetchQuotesFromWeb()
        }
        val list = onlineQuotesCache ?: return null
        return list.random()
    }

    fun getLocalQuotes(): List<String> = listOf(
        "Не бойся ошибаться, бойся бездействия.",
        "Успех – это сумма маленьких усилий, повторяющихся каждый день.",
        "Сначала ты работаешь на имя, потом имя работает на тебя.",
        "Путь в тысячу миль начинается с одного шага.",
        "Всё, что нас не убивает, делает нас сильнее."
    )
}
