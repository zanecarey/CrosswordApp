package zane.carey.crosswordapp

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface PuzzleInterface {
    @Headers("Content-Type: application/json")
    @GET("doshea/nyt_crosswords/master/{year}/{month}/{day}.json")
    //@GET("doshea/nyt_crosswords/master/1993/03/16.json")
    fun getData(
        @Path("year") year: String, @Path("month") month: String, @Path("day") day: String
    ): Deferred<Results>
}

data class Results(
    @SerializedName("author")
    val author: String,
    @SerializedName("editor")
    val editor: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("size")
    val size: Size,
    @SerializedName("answers")
    val answers: Answers,
    @SerializedName("clues")
    val clues: Clues,
    @SerializedName("grid")
    val grid: List<String>,
    @SerializedName("gridnums")
    val gridnums: List<Int>,
    @SerializedName("circles")
    val circles: List<Int>
)

data class Size(
    @SerializedName("cols")
    val cols: Int,
    @SerializedName("rows")
    val rows: Int
)

data class Answers(
    @SerializedName("across")
    val across: List<String>,
    @SerializedName("down")
    val down: List<String>
)

data class Clues(
    @SerializedName("across")
    val across: List<String>,
    @SerializedName("down")
    val down: List<String>
)

//data class Results(
//    @SerializedName("drinks")
//    val drinks: List<Drinks>?
//)
//
//data class Drinks(
//    @SerializedName("strDrink")
//    val strDrink: String)