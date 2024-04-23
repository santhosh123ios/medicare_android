package ie.setu.medicare.View

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ie.setu.medicare.Model.Category
import ie.setu.medicare.Model.CategoryList
import ie.setu.medicare.Model.Slots
import ie.setu.medicare.Model.SlotsList
import ie.setu.medicare.R
import ie.setu.medicare.View.Adapter.ItemAdapter
import ie.setu.medicare.View.Adapter.ItemSlotAdapter
import ie.setu.medicare.ViewModels.SignInActivityVM
import ie.setu.medicare.databinding.ActivitySignInBinding
import ie.setu.medicare.databinding.ActivitySlotBinding

class SlotActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySlotBinding
    private val viewModel: SignInActivityVM by viewModels()
    val database = Firebase.database

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemSlotAdapter
    var slotList = mutableListOf<SlotsList>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySlotBinding.inflate(layoutInflater)

        ///setContentView(binding.root)
        setContentView(R.layout.activity_slot)
//        enableEdgeToEdge()x
//        setContentView(R.layout.activity_category)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val addButton = findViewById<Button>(R.id.add_Btn)
        val catNameET = findViewById<EditText>(R.id.slotName)
        val back_button = findViewById<ImageView>(R.id.back_button)
        back_button.setOnClickListener {
            finish()
        }
        addButton.setOnClickListener {
            if (catNameET.text.isNotEmpty())
            {
                val myRef = database.getReference("slots")
                val userId = myRef.push().key
                val slot = Slots("$userId", catNameET.text.toString())
                viewModel.insertSlots(slot) { isSuccess ->
                    if (isSuccess) {
                        // Insertion successful
                        println("Slot inserted successfully")
                        Toast.makeText(this, "Slot inserted successfully", Toast.LENGTH_SHORT).show()
                        viewModel.getSlots { slots ->
                            if (slots != null) {
                                // Successfully retrieved categories
                                slotList.removeAll(slotList)
                                slotList.addAll(slots)
                                adapter.notifyDataSetChanged()
                            } else {
                                // Failed to retrieve categories
                                println("Failed to retrieve Slot")
                            }
                        }
                    } else {
                        // Insertion failed
                        println("Failed to insert Slot")
                        Toast.makeText(this, "Failed to insert Slot", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else
            {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }

        // Initialize your RecyclerView and Adapter
        recyclerView = findViewById(R.id.slotRecyclerView)
        adapter = ItemSlotAdapter(slotList, ::onEditItem, ::onDeleteItem, ::onEditItemAction, ::onDeleteItemAction)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Add some sample data to the list
        ////items.addAll(listOf("Item 1", "Item 2", "Item 3"))
        ///getCategoriesData()
        getAllCategories()
        // Create and attach the ItemTouchHelper
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (direction == ItemTouchHelper.LEFT) {
                    // Swipe left to delete the item
                    adapter.removeItem(position)
                } else if (direction == ItemTouchHelper.RIGHT) {
                    // Swipe right to edit the item
                    adapter.editItem(position)
                }
            }
        })

        // Attach the ItemTouchHelper to the RecyclerView
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun getAllCategories() {
        viewModel.getSlots { slots ->
            if (slots != null) {
                // Successfully retrieved categories
                slotList.removeAll(slotList)
                slotList.addAll(slots)
                adapter.notifyDataSetChanged()
            } else {
                // Failed to retrieve categories
                println("Failed to retrieve categories")
            }
        }
    }


    // Callback functions for editing and deleting items
    private fun onEditItem(position: Int) {
        // Implement your edit logic here
        // You could show an edit dialog or navigate to an edit screen
        for (i in slotList.indices) {
            slotList[i].editSelection = false
            slotList[i].deleteSelection = false
        }
        slotList[position].editSelection = true
        println("SANTHOSH EDIT categories"+position)
        adapter.notifyDataSetChanged()
    }

    private fun onDeleteItem(position: Int) {
        // Implement your delete logic here
        // For example, you could show a confirmation dialog before deleting the item
        for (i in slotList.indices) {
            slotList[i].editSelection = false
            slotList[i].deleteSelection = false
        }
        slotList[position].deleteSelection = true
        println("SANTHOSH DELETE categories"+position)
        adapter.notifyDataSetChanged()
    }

    private fun onDeleteItemAction(position: Int)
    {
        val catIdToDelete = slotList[position].slotId // Replace with the actual catId you want to delete

        viewModel.deleteSlots(catIdToDelete) { isSuccess ->
            if (isSuccess) {
                // Deletion successful
                println("Slot deleted successfully.")
                getAllCategories()
            } else {
                // Deletion failed
                println("Failed to delete slot.")
                Toast.makeText(this, "Failed to delete slot.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun onEditItemAction(position: Int)
    {
        showEditPopup(position)
    }

    // Show edit popup for the item at the given position
    private fun showEditPopup(position: Int) {
        // Create the AlertDialog
        // Create the AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Item")

        // Inflate the layout and set it as the content view of the dialog
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.edit_popup, null)
        val editText: EditText = view.findViewById(R.id.editText)
        editText.setText(slotList[position].slotName) // Pre-fill with the initial text

        builder.setView(view)

        // Set up the dialog buttons
        builder.setPositiveButton("Save") { dialog, _ ->
            // Get the new text from the EditText
            val newText = editText.text.toString()

            // Call the onTextSave function to handle the saved text
            onItemEdited(position,newText)

            // Dismiss the dialog
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            // Dismiss the dialog
            dialog.dismiss()
        }

        // Show the dialog
        builder.create().show()
    }

    private fun onItemEdited(position: Int, newText: String) {

        viewModel.editSlots(slotList[position].slotId, newText) { isSuccess ->
            if (isSuccess) {
                // Update successful
                println("Slot updated successfully.")
                getAllCategories()
            } else {
                // Update failed
                println("Failed to update slot.")
                Toast.makeText(this, "Failed to  update slot.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}