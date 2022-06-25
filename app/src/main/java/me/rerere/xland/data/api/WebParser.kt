package me.rerere.xland.data.api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.rerere.xland.data.model.Page
import me.rerere.xland.data.model.Post
import me.rerere.xland.data.model.ThreadPreview
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

private const val TAG = "WebParser"

class WebParser(
    private val okHttpClient: OkHttpClient
) {
    private fun parsePost(element: Element): Post {
        val info = element.select("div[class=h-threads-item-main]").ifEmpty {
            element.select("div[class=h-threads-item-reply-main]")
        }
        return Post(
            title = info.select("span[class=h-threads-info-title]").text(),
            nick = info.select("span[class=h-threads-info-email]").text(),
            date = info.select("span[class=h-threads-info-createdat]").text(),
            uid = info.select("span[class=h-threads-info-uid]").text().substringAfter(":"),
            tid = info.select("a[class=h-threads-info-id]").text().substringAfter(".").toLong(),
            content = info.select("div[class=h-threads-content]").first()?.wholeText()?.trim()
                ?: "",
            image = info.select("img[class=h-threads-img]").first()?.attr("src"),
            po = info.select("span[class=uk-text-primary uk-text-small]").isNotEmpty(),
        )
    }

    suspend fun getForum(
        path: String,
        page: Int
    ): Page<ThreadPreview> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("$path?page=$page")
            .get()
            .build()

        val response = okHttpClient.newCall(request).execute()
        val raw = response.body.string()
        val parser = Jsoup.parse(raw)

        val posts = parser.select("div[class=h-threads-item uk-clearfix]").map {
            parsePost(it).also { post ->
                post.reply = it.select("div[class=h-threads-item-reply]").map { element ->
                    parsePost(element)
                }
            }
        }

        val hasPrevious = parser
            .select("ul[class=uk-pagination uk-pagination-left h-pagination]")
            .select("li:contains(上一页)")
            .select("a")
            .isNotEmpty()
        val hasNext = parser
            .select("ul[class=uk-pagination uk-pagination-left h-pagination]")
            .select("li:contains(下一页)")
            .select("a")
            .isNotEmpty()

        Log.i(TAG, "getForum: $page,$hasPrevious,$hasNext,${posts.size}")
        
        Page(
            data = ThreadPreview(posts),
            currentPage = page,
            hasPreviousPage = hasPrevious,
            hasNextPage = hasNext
        )
    }
}