package zane.carey.crosswordapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

private lateinit var randCardView: CardView
private lateinit var resumeCardView: CardView

val api = RestApi()

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        randCardView = findViewById(R.id.randomCardView)
        resumeCardView = findViewById(R.id.savedGamesCardView)

        randCardView.setOnClickListener {

            val intent = Intent(this, PuzzleDisplayActivity::class.java)

            intent.putExtra("puzzleType", "random")
            startActivity(intent)
        }

        resumeCardView.setOnClickListener {

            val intent = Intent(this, PuzzleDisplayActivity::class.java)

            intent.putExtra("puzzleType", "saved")
            startActivity(intent)
        }
        val db = PuzzleRoomDatabase.getDatabase(applicationContext)

        //db.puzzleDao().deleteAll()
    }
}

