package zane.carey.crosswordapp

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestApi {
    private val puzzleApi: PuzzleInterface


    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        puzzleApi = retrofit.create(PuzzleInterface::class.java)
    }

    fun getPuzzle(year: String, month: String, day: String): Deferred<Results> {
        //return puzzleApi.getPuzzle(format)
        return puzzleApi.getData(year,month,day)
    }

//    fun getPuzzle(): Deferred<Results> {
//        //return puzzleApi.getPuzzle(format)
//        return puzzleApi.getData()
//    }
}