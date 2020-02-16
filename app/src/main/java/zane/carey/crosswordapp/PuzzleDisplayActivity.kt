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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cell_item.view.*
import kotlinx.android.synthetic.main.custom_info_display.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

var year = ""
var month = ""
var day = ""

private lateinit var cellRecyclerView: RecyclerView
private lateinit var displayLayout: ConstraintLayout
private lateinit var clueTextView: TextView
private var myMenu: Menu? = null

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
class HighlightedPosition {
    companion object {
        var position = 0
    }
}

var highlightedCellsList: MutableList<Int> = mutableListOf<Int>()

//current input mode
var inputMode: String = "horizontal"

//first and last indexes of each row
private var firstCells: List<Int> =
    listOf(0, 15, 30, 45, 60, 75, 90, 105, 120, 135, 150, 165, 180, 195, 210)
private var lastCells: List<Int> =
    listOf(14, 29, 44, 59, 74, 89, 104, 119, 134, 149, 164, 179, 194, 209, 224)

//arrays for our grid
private var gridnums: List<Int> = listOf(0)
private var grid: List<String> = listOf("")
private var cellList: MutableList<Cell> = mutableListOf<Cell>()



class PuzzleDisplayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle_display)
        setSupportActionBar(findViewById(R.id.myToolBar))

        cellRecyclerView = findViewById(R.id.crosswordRecyclerView)

        clueTextView = findViewById(R.id.clue_TextView)
        //displayLayout = findViewById(R.id.display_layout)


        if (intent.getStringExtra("puzzleType") == "random") {
            getRandomDate()
        } else {
            year = intent.getIntExtra("pickerValue", 2015).toString()
            month = "05"
            day = "05"
        }

        getPuzzleData("2008", "11", "15")


    }

    private fun getRandomDate() {
        val yearVal = (1976..2017).shuffled().first()
        val monthVal = (1..12).shuffled().first()
        val dayVal = (1..30).shuffled().first()

        if (monthVal < 10) {
            month = monthVal.toString().padStart(2, '0')
        } else {
            month = monthVal.toString()
        }

        if (dayVal < 10) {
            day = dayVal.toString().padStart(2, '0')
        } else {
            day = dayVal.toString()
        }

        year = yearVal.toString()
    }

    private fun getPuzzleData(year: String, month: String, day: String) {
        val job = CoroutineScope(Dispatchers.Main).launch {
            //            try{
//                val request = api.getPuzzle(year, month, day).await()
//
//            } catch ( ex: HttpException) {
//
//            }
            val request = api.getPuzzle(year, month, day).await()


            rows = request.size.rows
            cols = request.size.cols

            date = request.date
            author = request.author
            editor = request.editor

            cluesAcross = request.clues.across.toMutableList()
            cluesDown = request.clues.down.toMutableList()


            //cellRecyclerView.numColumns = cols

            gridnums = request.gridnums
            grid = request.grid

            for (i in 0 until grid.size) {
                cellList.add(Cell(grid[i], gridnums[i]))
            }
            val adapter = CellAdapter(cellList, this@PuzzleDisplayActivity)


            cellRecyclerView.adapter = adapter

            cellRecyclerView.layoutManager =
                GridLayoutManager(this@PuzzleDisplayActivity, 15)


            //update ui info
            withContext(Dispatchers.Main) {

                clueTextView.text = request.clues.across[0]
            }
        }
    }

    private fun checkAnswer() {
        //check every cell that has a letter inputted and show whether it is correct or not
        for (i in 0 until cellRecyclerView.size - 1) {
            if (cellRecyclerView[i].cellLetter.text != "*" && cellRecyclerView[i].cellLetter.text != grid[i]) {
                cellRecyclerView[i].setBackgroundResource(R.drawable.red_border)
            }
        }
    }

    fun displayClue(position: Int) {
        if (inputMode == "horizontal") {
            var clueCount = 0
            var blackCellStreak = true
            for (i in 0..position) {

                if (cellRecyclerView[i].cellLetter.text == ".") {
                    if (blackCellStreak) {
                        clueCount++
                        blackCellStreak = false
                    }
                } else if (lastCells.contains(i)) {
                    clueCount++
                    blackCellStreak = true
                } else {
                    blackCellStreak = true
                }

            }
            if(lastCells.contains(position)){
                clueCount--
            }
            clueTextView.text = cluesAcross[clueCount]
        } else {
            var newPosition = position

                while(cellRecyclerView[newPosition].cellLetter.text != "." && newPosition > 14){

                    if(cellRecyclerView[newPosition - 15].cellLetter.text != "."){
                        newPosition -= 15
                    } else {
                        break
                    }

                }

            val vertClueCount = verticalClueDisplay(newPosition)
            clueTextView.text = cluesDown[vertClueCount]
        }
    }

    private fun verticalClueDisplay(position: Int) : Int{
        var vertClueCount = 0
        for (i in 1..position) {
            if (i <= 14) {
                if (cellRecyclerView[i].cellLetter.text != ".") {
                    vertClueCount++
                }
            } else if (cellRecyclerView[i].cellLetter.text != "." && cellRecyclerView[i - 15].cellLetter.text == ".") {
                vertClueCount++
            }
        }
        return vertClueCount
    }

    //display the puzzle information in a dialog
    private fun displayInfo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Puzzle Information")
        val alertView = layoutInflater.inflate(R.layout.custom_info_display, null)
        alertView.author_text_view.text = "Author - " + author
        alertView.editor_text_view.text = "Editor - " + editor
        alertView.date_text_view.text = "Date - " + date
        alertView.rows_text_view.text = "Rows - " + rows
        alertView.cols_text_view.text = "Columns - " + cols

        builder.setView(alertView)

        builder.setPositiveButton("OK") { dialog, which ->

        }
        builder.show()


    }

    //flip the direction of the clue from horizontal to vertical and vice versa
    private fun changeDirection() {
        if (inputMode == "horizontal") {
            inputMode = "vertical"
            myMenu?.findItem(R.id.action_change_direction)?.setIcon(R.drawable.ic_swap_horiz_white_48dp)

        } else {
            inputMode = "horizontal"
            myMenu?.findItem(R.id.action_change_direction)?.setIcon(R.drawable.ic_swap_vert_black_48dp)
        }
        removeGreenHighlight(HighlightedPosition.position)
        removeBlueHighlights()
        highlightCells(HighlightedPosition.position)
        displayClue(HighlightedPosition.position)
    }

    //Remove the highlight from the previously chosen cell
    fun removeGreenHighlight(position: Int) {

        cellRecyclerView[position].setBackgroundResource(R.drawable.border)

    }

    fun highlightCells(position: Int) {

        cellRecyclerView[position].setBackgroundResource(R.drawable.green_border)

        if(inputMode == "horizontal"){
            //Highlight cells to right of chosen cell with blue border
            if (!lastCells.contains(position)) {

                for (i in position + 1..position + 14) {
                    if (lastCells.contains(i)) {
                        cellRecyclerView[i].setBackgroundResource(R.drawable.blue_border)
                        highlightedCellsList.add(i)
                        break
                    } else {
                        if (cellRecyclerView[i].cellLetter.text != ".") {
                            cellRecyclerView[i].setBackgroundResource(R.drawable.blue_border)
                            highlightedCellsList.add(i)
                        } else {
                            break
                        }
                    }
                }
            }

            //Highlight cells to left of chosen cell with blue border
            if (!firstCells.contains(position)) {

                for (i in position - 1 downTo position - 14) {
                    if (firstCells.contains(i)) {
                        cellRecyclerView[i].setBackgroundResource(R.drawable.blue_border)
                        highlightedCellsList.add(i)
                        break
                    } else {
                        if (cellRecyclerView[i].cellLetter.text != ".") {
                            cellRecyclerView[i].setBackgroundResource(R.drawable.blue_border)
                            highlightedCellsList.add(i)
                        } else {
                            break
                        }
                    }
                }
            }
        } else {
            var positionUpward = position
            var positionDownward = position
            while(positionUpward - 15 >= 0){
                positionUpward -= 15
                if(cellRecyclerView[positionUpward].cellLetter.text != "."){
                    cellRecyclerView[positionUpward].setBackgroundResource(R.drawable.blue_border)
                    highlightedCellsList.add(positionUpward)
                } else {
                    break
                }
            }
            while(positionDownward + 15 < cellRecyclerView.size){
                positionDownward += 15
                if (cellRecyclerView[positionDownward].cellLetter.text != ".") {
                    cellRecyclerView[positionDownward].setBackgroundResource(R.drawable.blue_border)
                    highlightedCellsList.add(positionDownward)
                } else {
                    break
                }
            }
        }
    }

    fun removeBlueHighlights() {

        for (i in 0 until highlightedCellsList.size) {
            cellRecyclerView[highlightedCellsList[i]].cellLayout.setBackgroundResource(R.drawable.border)
        }
        highlightedCellsList.clear()
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
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "A"
                true
            }
            KeyEvent.KEYCODE_B -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "B"
                true
            }
            KeyEvent.KEYCODE_C -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "C"
                true
            }
            KeyEvent.KEYCODE_D -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "D"
                true
            }
            KeyEvent.KEYCODE_E -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "E"
                true
            }
            KeyEvent.KEYCODE_F -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "F"
                true
            }
            KeyEvent.KEYCODE_G -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "G"
                true
            }
            KeyEvent.KEYCODE_H -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "H"
                true
            }
            KeyEvent.KEYCODE_I -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "I"
                true
            }
            KeyEvent.KEYCODE_J -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "J"
                true
            }
            KeyEvent.KEYCODE_K -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "K"
                true
            }
            KeyEvent.KEYCODE_L -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "L"
                true
            }
            KeyEvent.KEYCODE_M -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "M"
                true
            }
            KeyEvent.KEYCODE_N -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "N"
                true
            }
            KeyEvent.KEYCODE_O -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "O"
                true
            }
            KeyEvent.KEYCODE_P -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "P"
                true
            }
            KeyEvent.KEYCODE_Q -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "Q"
                true
            }
            KeyEvent.KEYCODE_R -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "R"
                true
            }
            KeyEvent.KEYCODE_S -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "S"
                true
            }
            KeyEvent.KEYCODE_T -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "T"
                true
            }
            KeyEvent.KEYCODE_U -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "U"
                true
            }
            KeyEvent.KEYCODE_V -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "V"
                true
            }
            KeyEvent.KEYCODE_W -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "W"
                true
            }
            KeyEvent.KEYCODE_X -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "X"
                true
            }
            KeyEvent.KEYCODE_Y -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "Y"
                true
            }
            KeyEvent.KEYCODE_Z -> {
                //current cell letter updated
                cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
                cellRecyclerView[HighlightedPosition.position].cellLetter.text = "Z"
                true
            }
            else -> super.onKeyUp(keyCode, event)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        myMenu = menu
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
            R.id.action_change_direction -> { supportActionBar
                changeDirection()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
