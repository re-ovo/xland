package me.rerere.xland.data.model

import com.google.gson.annotations.SerializedName

typealias ForumList = List<ForumCategory>

fun ForumList.findForumNameByFID(fid: Int): String = this.flatMap { it.forums }
    .find { it.id.toInt() == fid }?.name ?: ""

data class ForumCategory(
    @SerializedName("forums")
    val forums: List<Forum>,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("sort")
    val sort: String,
    @SerializedName("status")
    val status: String
)

data class Forum(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("fgroup")
    val fgroup: String,
    @SerializedName("forum_fuse_id")
    val forumFuseId: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("interval")
    val interval: String,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("permission_level")
    val permissionLevel: String,
    @SerializedName("showName")
    val showName: String,
    @SerializedName("sort")
    val sort: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("thread_count")
    val threadCount: String,
    @SerializedName("updateAt")
    val updateAt: String
)