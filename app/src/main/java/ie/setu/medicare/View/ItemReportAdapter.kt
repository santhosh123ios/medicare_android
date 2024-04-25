package ie.setu.medicare.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ie.setu.medicare.Model.Appointment
import ie.setu.medicare.Model.Report
import ie.setu.medicare.Model.Reports
import ie.setu.medicare.R
import ie.setu.medicare.View.Adapter.ItemAppoinmentAdapter

class ItemReportAdapter(private val items: MutableList<Reports>, private val onSelectAction: (Int) -> Unit) :
    RecyclerView.Adapter<ItemReportAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(rpId: String,pos:Int,ptId:String,rpDate:String,ptName:String) {//,catName: String,phone: String,place: String
            itemView.findViewById<TextView>(R.id.itemText).text = "Name : "+ptName
            itemView.findViewById<TextView>(R.id.rpIdText).text = "No : "+rpId
            itemView.findViewById<TextView>(R.id.ptIdText).text = "Id : "+ptId
            itemView.findViewById<TextView>(R.id.dateText).text = "Date : "+rpDate

//            if (status != "pending")
//            {
//                itemView.findViewById<Button>(R.id.selection_action).isEnabled = false
//                itemView.findViewById<Button>(R.id.actionView).isVisible = false
//            }
//            else
//            {
//                itemView.findViewById<Button>(R.id.selection_action).isEnabled = true
//                itemView.findViewById<Button>(R.id.actionView).isVisible = true
//            }

            itemView.findViewById<Button>(R.id.selection_action).setOnClickListener {
                // Call the onButtonClick function and pass the current item
                onSelectAction(pos)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_report_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item.rpId,position,item.ptId,item.rpDate,item.ptName)//,item.catName,item.phone,item.place
        // Add any other click listeners or actions for the item here

    }

    override fun getItemCount(): Int {
        return items.size
    }



}
