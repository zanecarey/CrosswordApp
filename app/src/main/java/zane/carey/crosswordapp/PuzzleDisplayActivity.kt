package zane.carey.crosswordapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

var year = ""
var month = ""
var day = ""

private lateinit var authorTV : TextView
private lateinit var editorTV : TextView
private lateinit var dateTV : TextView
private lateinit var cluesTV : TextView

//game grid variables
var rows = 0
var cols = 0
class PuzzleDisplayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle_display)

        authorTV = findViewById(R.id.authorTextView)
        editorTV = findViewById(R.id.editorTextView)
        dateTV = findViewById(R.id.dateTextView)
        cluesTV = findViewById(R.id.cluesTextView)

        getDate()

        getPuzzleData(year, month, day)
    }

    private fun getDate(){
        if (getIntent().hasExtra("year")) {
            year = getIntent().getStringExtra("year")
        }
        if (getIntent().hasExtra("month")) {
            month = getIntent().getStringExtra("month")
        }
        if (getIntent().hasExtra("day")) {
            day = getIntent().getStringExtra("day")
        }
    }

    private fun getPuzzleData(year:String,month:String,day:String){
        val job = CoroutineScope(Dispatchers.Main).launch {
            val request = api.getPuzzle(year,month,day).await()

            rows = request.size.rows
            cols = request.size.cols

            //update ui info
            withContext(Dispatchers.Main) {
                authorTV.text = request.author
                editorTV.text = request.editor
                dateTV.text = request.date
                cluesTV.text = request.clues.across[0]
            }
        }
    }
}
