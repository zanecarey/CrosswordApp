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

class PuzzleDisplayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle_display)

        authorTV = findViewById(R.id.authorTextView)
        editorTV = findViewById(R.id.editorTextView)

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

            withContext(Dispatchers.Main) {
                authorTV.text = request.author
                editorTV.text = request.editor
            }
        }
    }
}
