package me.rerere.xland.data.api

import me.rerere.xland.data.model.ForumList
import me.rerere.xland.data.model.Post
import retrofit2.http.GET
import retrofit2.http.Query

interface NimingbanAPI {
    @GET("/Api/getForumList")
    suspend fun getForumList(): ForumList

    @GET("/Api/timeline")
    suspend fun getTimeline(
        @Query("page") page: Int
    ): List<Post>

    @GET("/Api/showf")
    suspend fun getForumContent(
        @Query("id") id: Int
    ): List<Post>

    @GET("/Api/thread")
    suspend fun getThread(
        @Query("id") id: Long,
        @Query("page") page: Int
    ): Post
}