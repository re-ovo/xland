package me.rerere.xland.data.repo

import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import me.rerere.xland.data.api.NimingbanAPI
import me.rerere.xland.data.model.Post
import me.rerere.xland.data.model.findForumNameByFID
import javax.inject.Inject

class ContentRepo @Inject constructor(
    private val nimingbanAPI: NimingbanAPI
) {
    suspend fun getTimeline(page: Int): List<Post> {
        return supervisorScope {
            val forumList = async { nimingbanAPI.getForumList() }
            val timeline = async { nimingbanAPI.getTimeline(page) }
            val forumListData = forumList.await()
            val timeLineData = timeline.await()
            timeLineData.map {
                it.copy(
                    fName = forumListData.findForumNameByFID(it.fid)
                )
            }
        }
    }

    suspend fun getForumList() = nimingbanAPI.getForumList()
}