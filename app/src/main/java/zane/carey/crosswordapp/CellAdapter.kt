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
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cell_item.view.*

class CellAdapter(val list: List<Cell>, val context: Context) :
    RecyclerView.Adapter<CellAdapter.ViewHolder>() {

    private val myList = list

    private val myContext = context


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.cell_item, p0, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var clickable = true
        if (myList[position].number != 0) {
            holder.num.text = myList[position].number.toString()
        } else {
            holder.num.visibility = View.INVISIBLE
        }

        //check if character is ".", if it is then make cell blank and unclickable
        if (myList[position].letter == ".") {
            holder.layout.setBackgroundColor(Color.BLACK)
            clickable = false
            holder.letter.text = myList[position].letter
        } else {
            holder.letter.text = myList[position].letter.toString()
            if (myList[position].visibility == View.INVISIBLE) {
                holder.letter.visibility = View.INVISIBLE
            } else {
                holder.letter.visibility = View.VISIBLE
            }

        }

        holder.layout.setOnClickListener {

            if (clickable) {
                holder.layout.setBackgroundResource(R.drawable.green_border)

                //update highlighted position
                if (myContext is PuzzleDisplayActivity) {
                    myContext.removeGreenHighlight(HighlightedPosition.position)
                    myContext.removeBlueHighlights()
                    myContext.highlightCells(position)
                    myContext.displayClue(position)
                }
                HighlightedPosition.position = position
            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val num = view.cellNumber
        val letter = view.cellLetter
        val layout = view.cellLayout


    }
}