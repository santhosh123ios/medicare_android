package ie.setu.medicare.View.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ie.setu.medicare.Model.CategoryList
import ie.setu.medicare.Model.SlotsList
import ie.setu.medicare.R

class ItemSlotAdapter(private val items: MutableList<SlotsList>, private val onEdit: (Int) -> Unit, private val onDelete: (Int) -> Unit, private val onEditAction: (Int) -> Unit, private val onDeleteAction: (Int) -> Unit) :
    RecyclerView.Adapter<ItemSlotAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(name: String,editStatus: Boolean,deletestatus:Boolean,pos:Int) {
            itemView.findViewById<TextView>(R.id.itemText).text = name
            if (editStatus)
            {
                itemView.findViewById<RelativeLayout>(R.id.edit_view).isVisible = true
            }
            else
            {
                itemView.findViewById<RelativeLayout>(R.id.edit_view).isVisible = false
            }

            if (deletestatus)
            {
                itemView.findViewById<RelativeLayout>(R.id.delete_view).isVisible = true
            }
            else
            {
                itemView.findViewById<RelativeLayout>(R.id.delete_view).isVisible = false
            }

            // Handle button click
            itemView.findViewById<Button>(R.id.delete_action).setOnClickListener {
                // Call the onButtonClick function and pass the current item
                onDeleteAction(pos)
            }
            itemView.findViewById<Button>(R.id.edit_action).setOnClickListener {
                // Call the onButtonClick function and pass the current item
                onEditAction(pos)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item.slotName,item.editSelection,item.deleteSelection,position)
        // Add any other click listeners or actions for the item here

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun removeItem(position: Int) {
        ///items.removeAt(position)
        //items[position].deleteSelection = true
        ///notifyItemRemoved(position)
        onDelete(position)
    }

    fun editItem(position: Int) {
        //items[position].editSelection = true
        onEdit(position)
    }


}
