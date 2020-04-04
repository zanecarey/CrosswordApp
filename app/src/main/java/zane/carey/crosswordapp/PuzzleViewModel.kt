package zane.carey.crosswordapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PuzzleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PuzzleRepository

    val allPuzzles: LiveData<List<Puzzle>>

    init {
        val puzzleDao = PuzzleRoomDatabase.getDatabase(application, viewModelScope).puzzleDao()
        repository = PuzzleRepository(puzzleDao)
        allPuzzles = repository.allPuzzles
    }

    fun insert(puzzle: Puzzle) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(puzzle)
    }
}