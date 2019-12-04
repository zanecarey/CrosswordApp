package zane.carey.crosswordapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.coroutines.*

private lateinit var tv: TextView

val api = RestApi()
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById(R.id.textView)
        getPuzzle()
    }

    private fun getPuzzle() = runBlocking<Unit> {
        val job = CoroutineScope(Dispatchers.Main).launch {
            val request = api.getPuzzle().await()

            withContext(Dispatchers.Main) {
                tv.text = request.author
            }
        }
    }
}
