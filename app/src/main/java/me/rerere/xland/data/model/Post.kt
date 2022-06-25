package me.rerere.xland.data.model

data class Post(
    val title: String,
    val nick: String,
    val date: String,
    val uid: String, // 用户ID
    val tid: Long, // 串ID
    val content: String,
    val image: String?,
    val po: Boolean,
    var reply: List<Post> = emptyList()
)