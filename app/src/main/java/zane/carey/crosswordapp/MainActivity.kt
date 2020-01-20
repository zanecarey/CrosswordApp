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
private lateinit var chooseCard: CardView

val api = RestApi()
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        randCardView = findViewById(R.id.randomCardView)
        chooseCard = findViewById(R.id.chooseCardView)

        randCardView.setOnClickListener{

            val intent = Intent(this, PuzzleDisplayActivity::class.java)

            startActivity(intent)
        }

        chooseCard.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Choose a Date")

            val numberPicker = NumberPicker(this)
            numberPicker.minValue = 1976
            numberPicker.maxValue = 2018
            builder.setView(numberPicker)

            builder.setPositiveButton("Ok") { dialog, which ->

            }
        }

    }

   //private fun startPuzzle() = runBlocking<Unit> {
//        val job = CoroutineScope(Dispatchers.Main).launch {
//            val request = api.getPuzzle().await()
//
//            withContext(Dispatchers.Main) {
//                tv.text = request.author
//            }
//        }
}
