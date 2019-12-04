package zane.carey.crosswordapp

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface PuzzleInterface {

//    @Headers("Content-Type: application/json")
//    @GET("Data.aspx?date=9/11/2008")
//    fun getPuzzle(@Query("format") format : String): Deferred<Results>

    @Headers("Content-Type: application/json")
    @GET("doshea/nyt_crosswords/master/1999/01/01.json")
    fun getData(): Deferred<Results>
}

data class Results(
    @SerializedName("author")
    val author: String
//    @SerializedName("answers")
//    val answers: List<Answers>
)

//data class Answers(
//    @SerializedName("across")
//    val across: List<Across>,
//    @SerializedName("down")
//    val down: List<Down>
//)
//
//data class Across(
//    @SerializedName("across")
//    val across: List<String>
//)
//
//data class Down(
//    @SerializedName("Down")
//    val down: List<String>
//)

//data class Results(
//    @SerializedName("drinks")
//    val drinks: List<Drinks>?
//)
//
//data class Drinks(
//    @SerializedName("strDrink")
//    val strDrink: String)