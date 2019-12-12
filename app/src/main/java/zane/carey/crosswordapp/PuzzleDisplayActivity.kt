package zane.carey.crosswordapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.core.view.size
import kotlinx.android.synthetic.main.cell_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

var year = ""
var month = ""
var day = ""

private lateinit var cellGridView: GridView
private lateinit var displayLayout: ConstraintLayout

//game grid variables
var rows = 0
var cols = 0

//current highlighted position value
var highlightedPos = 0

//arrays for our grid
private var gridnums: List<Int> = listOf(0)
private var grid: List<String> = listOf("")
private var cellList: MutableList<Cell> = mutableListOf<Cell>()


class PuzzleDisplayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle_display)

        cellGridView = findViewById(R.id.crosswordGridView)

        //displayLayout = findViewById(R.id.display_layout)

        cellGridView.setOnItemClickListener {parent, view, position, id ->
            cellGridView[position].cellLayout.setBackgroundResource(R.drawable.green_border)
            highlightedPos = position
        }
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

    private fun checkAnswer(){
        //check every cell that has a letter inputted and show whether it is correct or not
        for(i in 0 until cellGridView.size-1){
            if(cellGridView[i].cellLetter.text != "*" && cellGridView[i].cellLetter.text != grid[i]){
                cellGridView[i].setBackgroundResource(R.drawable.red_border)
            }
        }
    }

    //detect when letter is pushed, change current cell
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_ENTER -> {
                checkAnswer()
                true
            }
            KeyEvent.KEYCODE_A -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "A"
                true
            }
            KeyEvent.KEYCODE_B -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "B"
                true
            }
            KeyEvent.KEYCODE_C -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "C"
                true
            }
            KeyEvent.KEYCODE_D -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "D"
                true
            }
            KeyEvent.KEYCODE_E -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "E"
                true
            }
            KeyEvent.KEYCODE_F -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "F"
                true
            }
            KeyEvent.KEYCODE_G -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "G"
                true
            }
            KeyEvent.KEYCODE_H -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "H"
                true
            }
            KeyEvent.KEYCODE_I -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "I"
                true
            }
            KeyEvent.KEYCODE_J -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "J"
                true
            }
            KeyEvent.KEYCODE_K -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "K"
                true
            }
            KeyEvent.KEYCODE_L -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "L"
                true
            }
            KeyEvent.KEYCODE_M -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "M"
                true
            }
            KeyEvent.KEYCODE_N -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "N"
                true
            }
            KeyEvent.KEYCODE_O -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "O"
                true
            }
            KeyEvent.KEYCODE_P -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "P"
                true
            }
            KeyEvent.KEYCODE_Q -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "Q"
                true
            }
            KeyEvent.KEYCODE_R -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "R"
                true
            }
            KeyEvent.KEYCODE_S -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "S"
                true
            }
            KeyEvent.KEYCODE_T -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "T"
                true
            }
            KeyEvent.KEYCODE_U -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "U"
                true
            }
            KeyEvent.KEYCODE_V -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "V"
                true
            }
            KeyEvent.KEYCODE_W -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "W"
                true
            }
            KeyEvent.KEYCODE_X -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "X"
                true
            }
            KeyEvent.KEYCODE_Y -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "Y"
                true
            }
            KeyEvent.KEYCODE_Z -> {
                //current cell letter updated
                cellGridView[highlightedPos].cellLetter.visibility = View.VISIBLE
                cellGridView[highlightedPos].cellLetter.text = "Z"
                true
            }
            else -> super.onKeyUp(keyCode, event)
        }
    }
}
