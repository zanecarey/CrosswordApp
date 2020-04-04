package zane.carey.crosswordapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Puzzle::class], version = 1)
abstract class PuzzleRoomDatabase : RoomDatabase() {

    abstract fun puzzleDao(): PuzzleDAO

    companion object {
        @Volatile
        private var INSTANCE: PuzzleRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): PuzzleRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PuzzleRoomDatabase::class.java,
                    "word_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(PuzzleDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class PuzzleDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            // If you want to keep the data through app restarts,
            // comment out the following line.
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.puzzleDao())
                }
            }
        }

        fun populateDatabase(puzzleDao: PuzzleDAO) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            puzzleDao.deleteAll()

            var puzzle = Puzzle("Hello")
            puzzleDao.insert(puzzle)
            puzzle = Puzzle("World!")
            puzzleDao.insert(puzzle)
        }
    }
}