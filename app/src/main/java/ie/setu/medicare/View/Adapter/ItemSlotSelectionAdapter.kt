package ie.setu.medicare.View.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ie.setu.medicare.Model.SlotsList
import ie.setu.medicare.R

class ItemSlotSelectionAdapter(private val items: MutableList<SlotsList>, private val onSelectAction: (Int) -> Unit) :
    RecyclerView.Adapter<ItemSlotSelectionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(name: String,pos:Int,selected:Boolean) {//,catName: String,phone: String,place: String
            itemView.findViewById<TextView>(R.id.itemText).text = name
//            itemView.findViewById<TextView>(R.id.catName).text = catName
//            itemView.findViewById<TextView>(R.id.itemTextPhone).text = "Phone : "+phone
//            itemView.findViewById<TextView>(R.id.itemTextPlace).text = "Place : "+place

            if (selected)
            {
                itemView.findViewById<RelativeLayout>(R.id.selectionView).setBackgroundColor(Color.parseColor("#FFC801"))
            }
            else
            {
                itemView.findViewById<RelativeLayout>(R.id.selectionView).setBackgroundColor(Color.parseColor("#FFFFFF"))
            }

            itemView.findViewById<Button>(R.id.selection_action).setOnClickListener {
                // Call the onButtonClick function and pass the current item
                onSelectAction(pos)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_slot_selection_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item.slotName,position,item.editSelection)//,item.catName,item.phone,item.place
        // Add any other click listeners or actions for the item here

    }

    override fun getItemCount(): Int {
        return items.size
    }



}
