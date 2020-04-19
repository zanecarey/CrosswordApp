package zane.carey.crosswordapp

class Cell{
    var letter: String? = null
    var number: Int? = null
    var visibility: Int? = null

    constructor(letter: String, number: Int, visibility: Int){
        this.letter = letter
        this.number = number
        this.visibility = visibility
    }
}