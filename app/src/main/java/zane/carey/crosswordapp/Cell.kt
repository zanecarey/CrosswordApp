package zane.carey.crosswordapp

class Cell{
    var letter: String? = null
    var number: Int? = null

    constructor(letter: String, number: Int){
        this.letter = letter
        this.number = number
    }
}