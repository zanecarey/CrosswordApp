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


    @GET("/api/json/v1/1/random.php")
    fun getData(): Deferred<Results>
}

//data class Results(
//    @SerializedName("author")
//    val author: String
//)

data class Results(
    @SerializedName("drinks")
    val drinks: List<Drinks>?
)

data class Drinks(
    @SerializedName("strDrink")
    val strDrink: String)