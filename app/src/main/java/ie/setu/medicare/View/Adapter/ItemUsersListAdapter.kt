package ie.setu.medicare.View.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ie.setu.medicare.Model.Users
import ie.setu.medicare.R

class ItemUsersListAdapter(private var items: MutableList<Users>, private val onSelectAction: (Int) -> Unit) :
    RecyclerView.Adapter<ItemUsersListAdapter.ViewHolder>() {
    private var originalDataList = items as MutableList<Users>
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(name: String,catName: String,phone: String,place: String,pos:Int,email:String) {
            itemView.findViewById<TextView>(R.id.itemText).text = name
            itemView.findViewById<TextView>(R.id.emailText).text = email
            itemView.findViewById<TextView>(R.id.catName).text = "Category : "+catName
            itemView.findViewById<TextView>(R.id.itemTextPhone).text = "Phone : "+phone
            itemView.findViewById<TextView>(R.id.itemTextPlace).text = "Place : "+place

//            itemView.findViewById<Button>(R.id.selection_action).setOnClickListener {
//                // Call the onButtonClick function and pass the current item
//                onSelectAction(pos)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_users_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item.name,item.catName,item.phone,item.place,position,item.email)
        // Add any other click listeners or actions for the item here

    }

    override fun getItemCount(): Int {
        return items.size
    }


}
