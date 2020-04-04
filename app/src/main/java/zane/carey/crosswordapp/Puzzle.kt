package zane.carey.crosswordapp;

import androidx.room.ColumnInfo
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "puzzle_table")
data class Puzzle(@PrimaryKey @ColumnInfo(name = "puzzle") val puzzle: String)