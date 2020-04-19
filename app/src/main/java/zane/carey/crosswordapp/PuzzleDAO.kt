package zane.carey.crosswordapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PuzzleDAO {
    @Query("SELECT * from puzzle_table ORDER BY puzzle ASC")
    fun getPuzzle(): List<Puzzle>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(puzzle: Puzzle)

    @Query("DELETE FROM puzzle_table")
    fun deleteAll()
}