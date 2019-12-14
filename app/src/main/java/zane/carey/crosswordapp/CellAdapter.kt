package zane.carey.crosswordapp

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView

class CellAdapter(val list: List<Cell>) : BaseAdapter() {

    private val myList = list


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // Inflate the custom view
        val inflater =
            parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.cell_item, null)

        // Get the custom view widgets reference
        val num = view.findViewById<TextView>(R.id.cellNumber)
        val letter = view.findViewById<TextView>(R.id.cellLetter)
        val cellLayout = view.findViewById<LinearLayout>(R.id.cellLayout)

        if(myList[position].number != 0){
            num.text = myList[position].number.toString()
        } else {
            num.visibility = View.INVISIBLE
        }

        //check if character is ".", if it is then make cell blank and unclickable
        if(myList[position].letter == "."){
            cellLayout.setBackgroundColor(Color.BLACK)
            cellLayout.isClickable = false
            letter.text = myList[position].letter
        } else {
            letter.text = myList[position].letter.toString()
        }

//        //onclick for inputting a letter
//        layout.setOnClickListener{
//
//            //highlight the chosen cell with red border
//            layout.setBackgroundResource(R.drawable.red_border)
//
//            //change current highlighted position
//
//        }

        return view
    }

    override fun getItem(position: Int): Any {
        return myList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

}