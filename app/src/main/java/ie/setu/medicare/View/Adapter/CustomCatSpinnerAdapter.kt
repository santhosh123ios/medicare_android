package ie.setu.medicare.View.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ie.setu.medicare.Model.Category
import ie.setu.medicare.Model.CategoryList
import ie.setu.medicare.R

class CustomCatSpinnerAdapter(context: Context, resource: Int, objects: List<CategoryList>) :
    ArrayAdapter<CategoryList>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_cat_item, parent, false)
        val item = getItem(position)
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        ////val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)

        // Set the text views with the custom item's data
        nameTextView.text = item?.catName
        ////descriptionTextView.text = item?.description

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}