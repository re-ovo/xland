package me.rerere.xland.data.repo

import me.rerere.xland.data.api.WebParser
import javax.inject.Inject

class ContentRepo @Inject constructor(
    private val webParser: WebParser
) {
    suspend fun getForum(
        path: String,
        page: Int
    ) = webParser.getForum(path, page)
}