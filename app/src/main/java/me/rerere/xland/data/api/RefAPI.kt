package me.rerere.xland.data.api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.rerere.xland.data.model.Ref
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

private const val TAG = "RefAPI"

class RefAPI(
    private val okHttpClient: OkHttpClient
) {
    suspend fun getRef(id: Long): Ref? = withContext(Dispatchers.IO) {
        Log.i(TAG, "getRef: getting ref $id")
        kotlin.runCatching {
            val request = Request.Builder()
                .url("https://www.nmbxd1.com/Home/Forum/ref?id=$id")
                .get()
                .build()

            val response = okHttpClient.newCall(request).execute()
            val body = response.body.string()
            val jsoup = Jsoup.parse(body)

            val title = jsoup.select("span[class=h-threads-info-title]").text()
            val name = jsoup.select("span[class=h-threads-info-email]").text()
            val now = jsoup.select("span[class=h-threads-info-createdat]").text()
            val userHash = jsoup.select("span[class=h-threads-info-uid]").text()
            val content = jsoup.select("div[class=h-threads-content]").html()

            Ref(
                id = id,
                title = title,
                name = name,
                now = now,
                userHash = userHash,
                content = content
            )
        }.getOrNull()
    }
}