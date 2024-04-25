package ie.setu.medicare.View.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.medicare.Model.CategoryList
import ie.setu.medicare.Model.Users
import ie.setu.medicare.R
import ie.setu.medicare.View.PatientActivity

class ItemCatHorizontalAdapter(private var items: MutableList<Users>, private val onSelectAction: (Int) -> Unit) :
    RecyclerView.Adapter<ItemCatHorizontalAdapter.ViewHolder>() {
    private var originalDataList = items as MutableList<Users>
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(name: String,catName: String,phone: String,place: String,pos:Int) {
            itemView.findViewById<TextView>(R.id.itemText).text = name
            itemView.findViewById<TextView>(R.id.catName).text = catName
            itemView.findViewById<TextView>(R.id.itemTextPhone).text = "Phone : "+phone
            itemView.findViewById<TextView>(R.id.itemTextPlace).text = "Place : "+place

            itemView.findViewById<Button>(R.id.selection_action).setOnClickListener {
                // Call the onButtonClick function and pass the current item
                onSelectAction(pos)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_cat_horizontal_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item.name,item.catName,item.phone,item.place,position)
        // Add any other click listeners or actions for the item here

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun filter(query: String) {
        if (query.isEmpty()) {
            items = originalDataList
        } else {
            // Filter the list based on the query
            items = originalDataList.filter { data ->
                // Compare the query with the data properties
                // Assuming MyData has properties name and description
                data.name.contains(query, ignoreCase = true) || data.place.contains(query, ignoreCase = true)
            } as MutableList<Users>
        }
        // Notify the adapter of data changes
        notifyDataSetChanged()
    }

}
