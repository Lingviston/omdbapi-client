package by.ve.omdbapiandroid.db

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(tableName = "RecentSearches", primaryKeys = ["query"])
data class RecentSearchEntity(
    @ColumnInfo(name = "query") val query: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long
)