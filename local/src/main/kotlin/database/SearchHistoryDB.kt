package database

import Dao.SearchHistoryDao
import androidx.room.Database
import androidx.room.RoomDatabase
import roomentity.SearchHistoryEntity

@Database(
    entities = [SearchHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}