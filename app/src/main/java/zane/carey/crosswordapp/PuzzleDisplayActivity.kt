package zane.carey.crosswordapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.core.view.size
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.android.synthetic.main.cell_item.view.*
import kotlinx.android.synthetic.main.custom_info_display.view.*
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.random.Random

var year = ""
var month = ""
var day = ""

private lateinit var cellRecyclerView: RecyclerView
private lateinit var displayLayout: ConstraintLayout
private lateinit var clueTextView: TextView
private lateinit var timerTextView: TextView
private lateinit var chronometer: Chronometer
private var myMenu: Menu? = null

//game grid variables
var rows = 0
var cols = 0

//game info
private var date: String = ""
private var author: String = ""
private var editor: String = ""
private var cluesAcross: MutableList<String> = mutableListOf<String>()
private var cluesDown: MutableList<String> = mutableListOf<String>()




private var highlightedCellsList: MutableList<Int> = mutableListOf<Int>()

//current input mode
private var inputMode: String = "horizontal"

//first and last indexes of each row
private var firstCells: List<Int> =
    listOf(0, 15, 30, 45, 60, 75, 90, 105, 120, 135, 150, 165, 180, 195, 210)
private var lastCells: List<Int> =
    listOf(14, 29, 44, 59, 74, 89, 104, 119, 134, 149, 164, 179, 194, 209, 224)

//arrays for our grid
private var gridnums: List<Int> = listOf(0)
private var grid: List<String> = listOf("")
private var cellList = mutableListOf<Cell>()
private lateinit var savedCellList: CellList


//letter card views
private lateinit var cvA: CardView
private lateinit var cvB: CardView
private lateinit var cvC: CardView
private lateinit var cvD: CardView
private lateinit var cvE: CardView
private lateinit var cvF: CardView
private lateinit var cvG: CardView
private lateinit var cvH: CardView
private lateinit var cvI: CardView
private lateinit var cvJ: CardView
private lateinit var cvK: CardView
private lateinit var cvL: CardView
private lateinit var cvM: CardView
private lateinit var cvN: CardView
private lateinit var cvO: CardView
private lateinit var cvP: CardView
private lateinit var cvQ: CardView
private lateinit var cvR: CardView
private lateinit var cvS: CardView
private lateinit var cvT: CardView
private lateinit var cvU: CardView
private lateinit var cvV: CardView
private lateinit var cvW: CardView
private lateinit var cvX: CardView
private lateinit var cvY: CardView
private lateinit var cvZ: CardView


private lateinit var puzzleViewModel: PuzzleViewModel

private lateinit var puzzleCopy: Puzzle
class PuzzleDisplayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle_display)
        setSupportActionBar(findViewById(R.id.myToolBar))

        cellRecyclerView = findViewById(R.id.crosswordRecyclerView)

        clueTextView = findViewById(R.id.clue_TextView)
        //displayLayout = findViewById(R.id.display_layout)

        //timerTextView = findViewById(R.id.timerTextView)
        chronometer = findViewById(R.id.chronometer1)

        puzzleViewModel = ViewModelProvider(this).get(PuzzleViewModel::class.java)
//        puzzleViewModel.allPuzzles.observe(this, Observer { puzzles ->
////            puzzles?.let { dbTV.text = puzzles[Random.nextInt(0, puzzles.size-1)].puzzle}
//        })

        //dbTV = findViewById(R.id.dbTV)

        cvA = findViewById(R.id.cardViewA)
        cvB = findViewById(R.id.cardViewB)
        cvC = findViewById(R.id.cardViewC)
        cvD = findViewById(R.id.cardViewD)
        cvE = findViewById(R.id.cardViewE)
        cvF = findViewById(R.id.cardViewF)
        cvG = findViewById(R.id.cardViewG)
        cvH = findViewById(R.id.cardViewH)
        cvI = findViewById(R.id.cardViewI)
        cvJ = findViewById(R.id.cardViewJ)
        cvK = findViewById(R.id.cardViewK)
        cvL = findViewById(R.id.cardViewL)
        cvM = findViewById(R.id.cardViewM)
        cvN = findViewById(R.id.cardViewN)
        cvO = findViewById(R.id.cardViewO)
        cvP = findViewById(R.id.cardViewP)
        cvQ = findViewById(R.id.cardViewQ)
        cvR = findViewById(R.id.cardViewR)
        cvS = findViewById(R.id.cardViewS)
        cvT = findViewById(R.id.cardViewT)
        cvU = findViewById(R.id.cardViewU)
        cvV = findViewById(R.id.cardViewV)
        cvW = findViewById(R.id.cardViewW)
        cvX = findViewById(R.id.cardViewX)
        cvY = findViewById(R.id.cardViewY)
        cvZ = findViewById(R.id.cardViewZ)

        if (intent.getStringExtra("puzzleTypeRandom") == "random") {
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.start()
            getPuzzleData(getRandomYear(), getRandomMonth(), getRandomDay())
        } else {
            val db = PuzzleRoomDatabase.getDatabase(applicationContext)
            val list = db.puzzleDao().getPuzzle()
            val puzzleIndex = intent.getIntExtra("puzzleTypeSaved", 0)

            val year = list[puzzleIndex.toInt()].puzzleYear
            val month = list[puzzleIndex.toInt()].puzzleMonth
            val day = list[puzzleIndex.toInt()].puzzleDay
            savedCellList = list[puzzleIndex.toInt()].gameBoardState
            val timer = list[puzzleIndex.toInt()].puzzleTimer
            val progress = list[puzzleIndex].progress
            puzzleCopy = Puzzle(year,month,day,timer, savedCellList,progress)

            chronometer.base = SystemClock.elapsedRealtime() - timer
            chronometer.start()

            getPuzzleData(year, month, day)
        }

        //Letter box listeners
        cvA.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "A"
            updateInputPosition()
        }
        cvB.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "B"
            updateInputPosition()
        }
        cvC.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "C"
            updateInputPosition()
        }
        cvD.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "D"
            updateInputPosition()
        }
        cvE.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "E"
            updateInputPosition()
        }
        cvF.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "F"
            updateInputPosition()
        }
        cvG.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "G"
            updateInputPosition()
        }
        cvH.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "H"
            updateInputPosition()
        }
        cvI.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "I"
            updateInputPosition()
        }
        cvJ.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "J"
            updateInputPosition()
        }
        cvK.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "K"
            updateInputPosition()
        }
        cvL.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "L"
            updateInputPosition()
        }
        cvM.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "M"
            updateInputPosition()
        }
        cvN.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "N"
            updateInputPosition()
        }
        cvO.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "O"
            updateInputPosition()
        }
        cvP.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "P"
            updateInputPosition()
        }
        cvQ.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "Q"
            updateInputPosition()

        }
        cvR.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "R"
            updateInputPosition()

        }
        cvS.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "S"
            updateInputPosition()

        }
        cvT.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "T"
            updateInputPosition()

        }
        cvU.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "U"
            updateInputPosition()

        }
        cvV.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "V"
            updateInputPosition()

        }
        cvW.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "W"
            updateInputPosition()

        }
        cvX.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "X"
            updateInputPosition()

        }
        cvY.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "Y"
            updateInputPosition()

        }
        cvZ.setOnClickListener {
            cellRecyclerView[HighlightedPosition.position].cellLetter.visibility = View.VISIBLE
            cellRecyclerView[HighlightedPosition.position].cellLetter.text = "Z"
            updateInputPosition()

        }


    }

    private fun updateInputPosition() {
        if (inputMode == "horizontal" && highlightedCellsList.contains(HighlightedPosition.position + 1)) {
            HighlightedPosition.position++
            highlightCells(HighlightedPosition.position)
        } else if (highlightedCellsList.contains(HighlightedPosition.position + 15)) {
            HighlightedPosition.position += 15
            highlightCells(HighlightedPosition.position)
        }
    }

    private fun getRandomYear(): String {
        year = (1976..2017).shuffled().first().toString()
        return year
    }

    private fun getRandomMonth(): String {
        val monthVal = (1..12).shuffled().first()

        if (monthVal < 10) {
            month = monthVal.toString().padStart(2, '0')
        } else {
            month = monthVal.toString()
        }
        return month
    }

    private fun getRandomDay(): String {
        val dayVal = (1..30).shuffled().first()
        if (dayVal < 10) {
            day = dayVal.toString().padStart(2, '0')
        } else {
            day = dayVal.toString()
        }
        return day
    }

    private fun getPuzzleData(year: String, month: String, day: String) {
        val job = CoroutineScope(Dispatchers.Main).launch {
            try {
                val request = api.getPuzzle(year, month, day).await()

                rows = request.size.rows
                cols = request.size.cols

                if (rows != 15 || cols != 15) {
                    getPuzzleData(getRandomYear(), getRandomMonth(), getRandomDay())
                    return@launch
                }

                date = request.date
                author = request.author
                editor = request.editor

                cluesAcross = request.clues.across.toMutableList()
                cluesDown = request.clues.down.toMutableList()


                //cellRecyclerView.numColumns = cols

                gridnums = request.gridnums
                grid = request.grid

                for (i in 0 until grid.size) {
                    cellList.add(Cell(grid[i], gridnums[i], View.INVISIBLE))
                }
                val adapter: CellAdapter
                if (intent.getStringExtra("puzzleTypeRandom") == "random") {
                    adapter = CellAdapter(cellList, this@PuzzleDisplayActivity)
                } else {
                    adapter = CellAdapter(savedCellList.cellList, this@PuzzleDisplayActivity)
                }
                cellRecyclerView.adapter = adapter

                cellRecyclerView.layoutManager =
                    GridLayoutManager(this@PuzzleDisplayActivity, 15)

                //update ui info
                withContext(Dispatchers.Main) {

                    clueTextView.text = request.clues.across[0]
                }


            } catch (ex: HttpException) {

                //update ui info
                withContext(Dispatchers.Main) {

                    //getPuzzleData("2008", "11", "15")
                    getPuzzleData(getRandomYear(), getRandomMonth(), getRandomDay())
                    //clueTextView.text = ex.code().toString()
                }
            }
        }
    }

    private fun checkAnswer() {
        //check every cell that has a letter inputted and show whether it is correct or not
        for (i in 0 until cellRecyclerView.size - 1) {
            if (cellRecyclerView[i].cellLetter.text != "*" && cellRecyclerView[i].cellLetter.text != grid[i]) {
                cellRecyclerView[i].setBackgroundResource(R.drawable.red_border)
            } else if (cellRecyclerView[i].cellLetter.text != "*" && cellRecyclerView[i].cellLetter.text == grid[i] && cellRecyclerView[i].cellLetter.visibility == View.VISIBLE){
                cellRecyclerView[i].setBackgroundResource((R.drawable.gold_border))
            }
        }
    }

    //return the puzzle progress in % form
    private fun getPuzzleProgress(): Double {
        var progress = 0.0
        var cellAnswers = 0
        for (i in 0 until cellRecyclerView.size - 1) {
            if (cellRecyclerView[i].cellLetter.text == grid[i] && cellRecyclerView[i].cellLetter.visibility == View.VISIBLE) {
                progress++
            }
            if (grid[i] != ".") {
                cellAnswers++
            }
        }
        val dblProgress = (progress / cellAnswers) * 100
        val dbFormat = DecimalFormat("##.#")
        dbFormat.roundingMode = RoundingMode.CEILING
        return dbFormat.format(dblProgress).toDouble()
    }

    fun displayClue(position: Int) {
        if (inputMode == "horizontal") {
            var clueCount = 0
            var blackCellStreak = true
            for (i in 0..position) {
                if (cellRecyclerView[i].cellLetter.text == ".") {
                    if(firstCells.contains(i)) {
                        clueCount--
                    } else {
                        if (blackCellStreak) {
                            clueCount++
                            blackCellStreak = false
                        }
                    }

                } else if (lastCells.contains(i)) {
                    clueCount++
                    blackCellStreak = true
                } else {
                    blackCellStreak = true
                }

            }
            if (lastCells.contains(position)) {
                clueCount--
            }
            clueTextView.text = cluesAcross[clueCount]
        } else {
            var newPosition = position

            while (cellRecyclerView[newPosition].cellLetter.text != "." && newPosition > 14) {

                if (cellRecyclerView[newPosition - 15].cellLetter.text != ".") {
                    newPosition -= 15
                } else {
                    break
                }

            }

            val vertClueCount = verticalClueDisplay(newPosition)
            clueTextView.text = cluesDown[vertClueCount]
        }
    }

    private fun verticalClueDisplay(position: Int): Int {
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
            myMenu?.findItem(R.id.action_change_direction)
                ?.setIcon(R.drawable.ic_swap_horiz_white_48dp)

        } else {
            inputMode = "horizontal"
            myMenu?.findItem(R.id.action_change_direction)
                ?.setIcon(R.drawable.ic_swap_vert_black_48dp)
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

        if (inputMode == "horizontal") {
            //Highlight cells to right of chosen cell with blue border
            if (!lastCells.contains(position)) {

                for (i in position + 1..position + 14) {
                    if (lastCells.contains(i)) {
                        if(cellRecyclerView[i].cellLetter.text != "."){
                            cellRecyclerView[i].setBackgroundResource(R.drawable.blue_border)
                            highlightedCellsList.add(i)
                        }
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
                        if(cellRecyclerView[i].cellLetter.text != "."){
                            cellRecyclerView[i].setBackgroundResource(R.drawable.blue_border)
                            highlightedCellsList.add(i)
                        }
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
            while (positionUpward - 15 >= 0) {
                positionUpward -= 15
                if (cellRecyclerView[positionUpward].cellLetter.text != ".") {
                    cellRecyclerView[positionUpward].setBackgroundResource(R.drawable.blue_border)
                    highlightedCellsList.add(positionUpward)
                } else {
                    break
                }
            }
            while (positionDownward + 15 < cellRecyclerView.size) {
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
            R.id.action_change_direction -> {
                supportActionBar
                changeDirection()
                return true
            }
            R.id.action_save_game -> {
                saveGame()
                return true
            }
            R.id.delete_save_game -> {
                deleteSave()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit Game?")
        builder.setPositiveButton("Ok") { dialog, which ->
            cellList.clear()
            HighlightedPosition.position = 0
            super.onBackPressed()
        }
        val dialog = builder.create()
        dialog.show()
    }

    //save the game to room db
    fun saveGame() {

        val db = PuzzleRoomDatabase.getDatabase(applicationContext)
        val timerValue = (SystemClock.elapsedRealtime() - chronometer.base)
        val progress = getPuzzleProgress()
        val gameState = mutableListOf<Cell>()
        for (i in 0 until cellRecyclerView.size) {
            gameState.add(
                Cell(
                    cellRecyclerView[i].cellLetter.text.toString(),
                    Integer.parseInt(cellRecyclerView[i].cellNumber.text.toString()),
                    cellRecyclerView[i].cellLetter.visibility
                )
            )
        }

        db.puzzleDao().insert(Puzzle(year,month,day,timerValue,CellList(gameState),progress))
    }

    fun deleteSave() {
        val db = PuzzleRoomDatabase.getDatabase(applicationContext)
        db.puzzleDao().deleteSave(puzzleCopy)
    }
}
