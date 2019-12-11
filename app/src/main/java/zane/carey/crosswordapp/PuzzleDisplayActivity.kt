package zane.carey.crosswordapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.GridView
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

var year = ""
var month = ""
var day = ""

private lateinit var cellGridView: GridView

//game grid variables
var rows = 0
var cols = 0

//arrays for our grid
private var gridnums: List<Int> = listOf(0)
private var grid: List<String> = listOf("")
private var cellList: MutableList<Cell> = mutableListOf<Cell>()

class PuzzleDisplayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle_display)

        cellGridView = findViewById(R.id.crosswordGridView)

        getDate()

        getPuzzleData(year, month, day)
    }

    private fun getDate() {
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

    private fun getPuzzleData(year: String, month: String, day: String) {
        val job = CoroutineScope(Dispatchers.Main).launch {
            val request = api.getPuzzle(year, month, day).await()

            rows = request.size.rows
            cols = request.size.cols

            cellGridView.numColumns = cols

            gridnums = request.gridnums
            grid = request.grid

            for (i in 0 until grid.size - 1) {
                cellList.add(Cell(grid[i], gridnums[i]))
            }
            val adapter = CellAdapter(cellList)

            cellGridView.adapter = adapter

            //update ui info
            withContext(Dispatchers.Main) {
                //                authorTV.text = request.author
//                editorTV.text = request.editor
//                dateTV.text = request.date
//                cluesTV.text = request.clues.across[0]
            }
        }
    }

    //detect when letter is pushed, change current cell
//    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
//        return when (keyCode) {
//            KeyEvent.KEYCODE_A -> {
//                //current cell letter updated
//                if(event.isShiftPressed){
//                    return true
//                } else {
//                    return false
//                }
//                true
//            }
//        } else -> super.onKeyUp(keyCode, event)
//    }
}
