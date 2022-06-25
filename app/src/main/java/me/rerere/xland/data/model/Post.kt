package me.rerere.xland.data.model

data class Post(
    val Hide: Int,
    val RemainReplies: Int,
    val Replies: List<Reply>,
    val ReplyCount: Int,
    val admin: Int,
    val content: String,
    val ext: String,
    val fid: Int,
    var fName: String,
    val id: Int,
    val img: String,
    val name: String,
    val now: String,
    val sage: Int,
    val title: String,
    val user_hash: String
)

data class Reply(
    val Hide: Int,
    val ReplyCount: Int,
    val admin: Int,
    val content: String,
    val ext: String,
    val fid: Int,
    val id: Int,
    val img: String,
    val name: String,
    val now: String,
    val sage: Int,
    val title: String,
    val user_hash: String
)