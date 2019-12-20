package zane.carey.crosswordapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.core.view.size
import kotlinx.android.synthetic.main.cell_item.view.*
import kotlinx.android.synthetic.main.custom_info_display.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

var year = ""
var month = ""
var day = ""

private lateinit var cellGridView: GridView
private lateinit var displayLayout: ConstraintLayout
private lateinit var clueTextView: TextView

//game grid variables
var rows = 0
var cols = 0

//game info
var date: String = ""
var author: String = ""
var editor: String = ""
var cluesAcross: MutableList<String> = mutableListOf<String>()
var cluesDown: MutableList<String> = mutableListOf<String>()


//current highlighted position value
var highlightedPos = 0
var highlightedCellsList: MutableList<Int> = mutableListOf<Int>()

//current input mode
var inputMode: String = "horizontal"

//arrays for our grid
private var gridnums: List<Int> = listOf(0)
private var grid: List<String> = listOf("")
private var cellList: MutableList<Cell> = mutableListOf<Cell>()


class PuzzleDisplayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle_display)

        cellGridView = findViewById(R.id.crosswordGridView)

        clueTextView = findViewById(R.id.clue_TextView)
        //displayLayout = findViewById(R.id.display_layout)


            getDate()

            getPuzzleData(year, month, day)

        cellGridView.setOnItemClickListener { parent, view, position, id ->

            highlightCells(position)

        }
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

            date = request.date
            author = request.author
            editor = request.editor

            cluesAcross = request.clues.across.toMutableList()
            cluesDown = request.clues.down.toMutableList()

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

                clueTextView.text = request.clues.across[0]
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

    //display the puzzle information in a dialog
    private fun displayInfo(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Puzzle Information")
        val alertView = layoutInflater.inflate(R.layout.custom_info_display, null)
        alertView.author_text_view.text = "Author - " + author
        alertView.editor_text_view.text = "Editor - " + editor
        alertView.date_text_view.text = "Date - " + date
        alertView.rows_text_view.text = "Rows - " + rows
        alertView.cols_text_view.text = "Columns - " + cols

        builder.setView(alertView)

        builder.setPositiveButton("OK") {dialog, which ->

        }
        builder.show()


    }

    //flip the direction of the clue from horizontal to vertical and vice versa
    private fun changeDirection(){
        if(inputMode == "horizontal"){
            inputMode = "vertical"
        } else {
            inputMode = "horizontal"
        }

        //change highlighted cells properly
        highlightCells(highlightedPos)
    }

    private fun highlightCells(position: Int){

        //change clue to display to player

        //get correct clue number
        var clueNum = 0
        for(i in 0 until position){
            if(cellGridView[i].cellLetter.text == "."){
                clueNum++
            }
        }

        if(inputMode == "horizontal"){
            clueTextView.text = cluesAcross[clueNum]
        } else {
            clueTextView.text = cluesDown[clueNum]
        }



        //check if cell is a blank cell
        if(grid[position] != ".") {

            //remove highlight from former cells
            cellGridView[highlightedPos].cellLayout.setBackgroundResource(R.drawable.border)

            for (i in 0 until highlightedCellsList.size) {
                cellGridView[highlightedCellsList[i]].cellLayout.setBackgroundResource(R.drawable.border)
            }

            highlightedCellsList.clear()

            //add highlight to chosen cell
            cellGridView[position].cellLayout.setBackgroundResource(R.drawable.green_border)

            //add highlights for the whole row if on horizontal mode
            if (inputMode == "horizontal") {

                var rowMultiplierLeft = 0
                var rowMultiplierRight = 1

                when (position) {
                    in cellGridView.numColumns..(cellGridView.numColumns * 2 - 1) -> {
                        rowMultiplierRight = 2
                        rowMultiplierLeft = 1
                    }
                    in cellGridView.numColumns * 2..(cellGridView.numColumns * 3 - 1) -> {
                        rowMultiplierRight = 3
                        rowMultiplierLeft = 2
                    }
                    in cellGridView.numColumns * 3..(cellGridView.numColumns * 4 - 1) -> {
                        rowMultiplierRight = 4
                        rowMultiplierLeft = 3
                    }
                    in cellGridView.numColumns * 4..(cellGridView.numColumns * 5 - 1) -> {
                        rowMultiplierRight = 5
                        rowMultiplierLeft = 4
                    }
                    in cellGridView.numColumns * 5..(cellGridView.numColumns * 6 - 1) -> {
                        rowMultiplierRight = 6
                        rowMultiplierLeft = 5
                    }
                    in cellGridView.numColumns * 6..(cellGridView.numColumns * 7 - 1) -> {
                        rowMultiplierRight = 7
                        rowMultiplierLeft = 6
                    }
                    in cellGridView.numColumns * 7..(cellGridView.numColumns * 8 - 1) -> {
                        rowMultiplierRight = 8
                        rowMultiplierLeft = 7
                    }
                    in cellGridView.numColumns * 8..(cellGridView.numColumns * 9 - 1) -> {
                        rowMultiplierRight = 9
                        rowMultiplierLeft = 8
                    }
                    in cellGridView.numColumns * 9..(cellGridView.numColumns * 10 - 1) -> {
                        rowMultiplierRight = 10
                        rowMultiplierLeft = 9
                    }
                    in cellGridView.numColumns * 10..(cellGridView.numColumns * 11 - 1) -> {
                        rowMultiplierRight = 11
                        rowMultiplierLeft = 10
                    }
                    in cellGridView.numColumns * 11..(cellGridView.numColumns * 12 - 1) -> {
                        rowMultiplierRight = 12
                        rowMultiplierLeft = 11
                    }
                    in cellGridView.numColumns * 12..(cellGridView.numColumns * 13 - 1) -> {
                        rowMultiplierRight = 13
                        rowMultiplierLeft = 12
                    }
                    in cellGridView.numColumns * 13..(cellGridView.numColumns * 14 - 1) -> {
                        rowMultiplierRight = 14
                        rowMultiplierLeft = 13
                    }
                    in cellGridView.numColumns * 14..(cellGridView.numColumns * 15 - 1) -> {
                        rowMultiplierRight = 15
                        rowMultiplierLeft = 14
                    }
                    in cellGridView.numColumns * 15..(cellGridView.numColumns * 16 - 1) -> {
                        rowMultiplierRight = 16
                        rowMultiplierLeft = 15
                    }
                }

                for (i in (position - 1) downTo cellGridView.numColumns * rowMultiplierLeft) {
                    if (cellGridView[i].cellLetter.text != ".") {
                        cellGridView[i].setBackgroundResource(R.drawable.blue_border)
                        highlightedCellsList.add(i)
                    } else {
                        break
                    }
                }

                for (i in position + 1 until cellGridView.numColumns * rowMultiplierRight) {

                    if (cellGridView[i].cellLetter.text != ".") {
                        cellGridView[i].setBackgroundResource(R.drawable.blue_border)
                        highlightedCellsList.add(i)
                    } else {
                        break
                    }
                }
            } else {
                var positionUp = position
                var positionDown = position
                while (positionUp - cellGridView.numColumns >= 0) {
                    positionUp -= cellGridView.numColumns
                    if (cellGridView[positionUp].cellLetter.text != ".") {
                        cellGridView[positionUp].setBackgroundResource(R.drawable.blue_border)
                        highlightedCellsList.add(positionUp)
                    } else {
                        break
                    }
                }
                while (positionDown + cellGridView.numColumns <= cellGridView.size) {
                    positionDown += cellGridView.numColumns
                    if (cellGridView[positionDown].cellLetter.text != ".") {
                        cellGridView[positionDown].setBackgroundResource(R.drawable.blue_border)
                        highlightedCellsList.add(positionDown)
                    } else {
                        break
                    }
                }
            }
            //change highlighted position
            highlightedPos = position
        }
    }

    //detect when letter is pushed, change current cell
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_SHIFT_LEFT -> {
                Toast.makeText(this, date, Toast.LENGTH_SHORT).show()
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.action_error_check -> {
                checkAnswer()
                return true
            }
            R.id.action_display_info -> {
                displayInfo()
                return true
            }
            R.id.action_change_direction -> {
                changeDirection()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
