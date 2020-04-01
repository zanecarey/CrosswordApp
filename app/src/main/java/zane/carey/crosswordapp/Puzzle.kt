package zane.carey.crosswordapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "puzzle_table")
public class Puzzle {
    @PrimaryKey(autoGenerate = true)
    private var id: Int = 0
    private var clues = 0
}