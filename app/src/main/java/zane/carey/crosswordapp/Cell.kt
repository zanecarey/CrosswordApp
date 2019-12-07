package zane.carey.crosswordapp

public class Cell{
    var letter: Char? = null
    var number: Int? = null

    constructor(letter: Char, number: Int){
        this.letter = letter
        this.number = number
    }
}