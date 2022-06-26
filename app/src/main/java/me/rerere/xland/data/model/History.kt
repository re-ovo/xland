package me.rerere.xland.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class BrowseHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val historyType: HistoryType,
    val time: Long
)

enum class HistoryType {
    Forum,
    Thread
}