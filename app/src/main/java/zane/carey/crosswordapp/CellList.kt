package zane.carey.crosswordapp

import androidx.room.TypeConverter

class CellList{
    var cellList: List<Cell>

    constructor(cellList: List<Cell>){
        this.cellList = cellList
    }
}