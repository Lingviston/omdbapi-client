package by.ve.omdbapiandroid.db

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(tableName = "SearchQueries", primaryKeys = ["query", "year", "type"])
data class SearchQueryEntity(
    @ColumnInfo(name = "query") val query: String,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long
)