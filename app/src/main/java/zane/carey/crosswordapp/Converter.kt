package zane.carey.crosswordapp

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {

    val gson = Gson()

    inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object: TypeToken<T>() {}.type)

    @TypeConverter
    fun storeCellListToString(cellList: CellList): String = gson.toJson(cellList)

    @TypeConverter
    fun storeStringToCellList(list: String): CellList = gson.fromJson(list)
}