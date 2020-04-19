package zane.carey.crosswordapp;

import androidx.recyclerview.widget.RecyclerView
import androidx.room.*

@Entity(tableName = "puzzle_table")
@TypeConverters(Converter::class)
//data class Puzzle(@PrimaryKey @ColumnInfo(name = "puzzle") val puzzleYear: String, val puzzleMonth: String, val puzzleDay: String, val puzzleTimer: String, val gameBoardState: CellList)
data class Puzzle(@PrimaryKey @ColumnInfo(name = "puzzle") val puzzleYear: String, val puzzleMonth: String, val puzzleDay: String)