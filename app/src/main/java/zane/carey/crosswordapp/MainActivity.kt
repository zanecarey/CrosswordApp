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

val api = RestApi()

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        randCardView = findViewById(R.id.randomCardView)

        randCardView.setOnClickListener {

            val intent = Intent(this, PuzzleDisplayActivity::class.java)

            intent.putExtra("puzzleType", "random")
            startActivity(intent)
        }
    }
}

