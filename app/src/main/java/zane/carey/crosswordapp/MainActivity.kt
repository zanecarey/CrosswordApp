package zane.carey.crosswordapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

private lateinit var randCardView: CardView
private lateinit var resumeCardView: CardView
private lateinit var resetCV: CardView
private lateinit var dateChoice: String
private var puzzleIndex: Int = 0
val api = RestApi()

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        randCardView = findViewById(R.id.randomCardView)
        resumeCardView = findViewById(R.id.savedGamesCardView)
        resetCV = findViewById(R.id.resetDBCardView)

        randCardView.setOnClickListener {

            val intent = Intent(this, PuzzleDisplayActivity::class.java)

            intent.putExtra("puzzleTypeRandom", "random")
            startActivity(intent)
        }

        resumeCardView.setOnClickListener {

            val intent = Intent(this, PuzzleDisplayActivity::class.java)
            val db = PuzzleRoomDatabase.getDatabase(applicationContext)
            val list = db.puzzleDao().getPuzzle()
            val puzzleList = mutableListOf<String>()
            for (i in list.indices) {

                //array[i] = Date(list[i].puzzleYear, list[i].puzzleMonth, list[i].puzzleDay).toString()
                puzzleList.add(list[i].puzzleDay + "/" + list[i].puzzleMonth + "/" + list[i].puzzleYear)

            }
            val array = puzzleList.toTypedArray()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pick a saved game")
            builder.setItems(array
            ) { dialog, which ->
                dateChoice = array[which].toString()
                //puzzleIndex = which
                intent.putExtra("puzzleTypeSaved", which)
                startActivity(intent)
            }

//            builder.setPositiveButton("Ok") { dialog, which ->
//
//                intent.putExtra("puzzleTypeSaved", dateChoice + puzzleIndex)
//                startActivity(intent)
//            }

            val dialog = builder.create()
            dialog.show()

        }

        resetCV.setOnClickListener {
            val db = PuzzleRoomDatabase.getDatabase(applicationContext)

            db.puzzleDao().deleteAll()
        }
    }
}

