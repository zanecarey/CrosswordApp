package zane.carey.crosswordapp

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class PuzzleRepository(private val puzzleDao: PuzzleDAO) {

    val allPuzzles: List<Puzzle> = puzzleDao.getPuzzle()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(puzzle: Puzzle){
        puzzleDao.insert(puzzle)
    }
}