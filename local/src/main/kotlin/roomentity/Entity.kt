package roomentity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @ColumnInfo(name= "querycolumn")
    @PrimaryKey
    val query: String,
    @ColumnInfo(name= "timestamp")
    val timestamp: Long = System.currentTimeMillis()
)
