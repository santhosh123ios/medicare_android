package ie.setu.medicare.View

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ie.setu.medicare.Model.Appointment
import ie.setu.medicare.Model.Category
import ie.setu.medicare.Model.SlotsList
import ie.setu.medicare.R
import ie.setu.medicare.View.Adapter.ItemCatHorizontalAdapter
import ie.setu.medicare.View.Adapter.ItemSlotAdapter
import ie.setu.medicare.View.Adapter.ItemSlotSelectionAdapter
import ie.setu.medicare.ViewModels.SignInActivityVM
import ie.setu.medicare.databinding.ActivityAppointmentCreateBinding
import ie.setu.medicare.databinding.ActivitySignInBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppointmentCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentCreateBinding
    var selectedDate = ""
    var ptName = ""
    var ptId = ""
    var drName = ""
    var drId = ""
    var selectedSlotId = ""
    var selectedSlotName = ""
    private val viewModel: SignInActivityVM by viewModels()
    val database = Firebase.database
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemSlotSelectionAdapter
    var slotList = mutableListOf<SlotsList>()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAppointmentCreateBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_appointment_create)
        sharedPreferences = getSharedPreferences("auth_pref", Context.MODE_PRIVATE)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val back_button = findViewById<ImageView>(R.id.back_button)
        back_button.setOnClickListener {
            finish()
        }

        // Get a reference to the button
        val datePicButton = findViewById<Button>(R.id.date_pic)
        val submitBtn = findViewById<Button>(R.id.submitBtn)
        // Get the current date
        val currentDate = Date()

        // Format the date
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormatter.format(currentDate)
        // Set the button text to the formatted date
        datePicButton.text = formattedDate
        selectedDate = formattedDate

        // Set up a click listener for the date_pic button
        submitBtn.setOnClickListener {
            val myRef = database.getReference("appointments")
            val apId = myRef.push().key
            val appointment = Appointment("$apId","$selectedDate","$selectedSlotName","$selectedSlotId","$ptName","$ptId","$drName","$drId" ,"pending")
            viewModel.createAppointment(appointment) { isSuccess ->
                if (isSuccess) {
                    // Insertion successful
                    println("Appointment created successfully")
                    Toast.makeText(this, "Appointment created successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    // Insertion failed
                    println("Failed to create Appointment")
                    Toast.makeText(this, "Failed to create appointment", Toast.LENGTH_SHORT).show()
                }
            }
        }
        datePicButton.setOnClickListener {
            // Create a DatePickerDialog with the current date as the default
            // Get the current date
            val currentDateCal = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    // Create a calendar object with the selected date
                    val selectedDateCalendar = Calendar.getInstance()
                    selectedDateCalendar.set(year, month, dayOfMonth)

                    // Format the selected date
                    val formattedDate = dateFormatter.format(selectedDateCalendar.time)

                    // Set the button text to the formatted selected date
                    datePicButton.text = formattedDate
                    // Update the selectedDate variable with the selected date
                    selectedDate = formattedDate
                },
                currentDateCal.get(Calendar.YEAR),
                currentDateCal.get(Calendar.MONTH),
                currentDateCal.get(Calendar.DAY_OF_MONTH)
            )

            // Show the DatePickerDialog
            datePickerDialog.show()
        }

        // Initialize your RecyclerView and Adapter
        ///recyclerView = binding.slotRecyclerView
        recyclerView = findViewById(R.id.slotRecyclerView)
        adapter = ItemSlotSelectionAdapter(slotList, ::onSelectAction)
        //recyclerView.layoutManager = LinearLayoutManager(this)
        // Set a horizontal LinearLayoutManager on the RecyclerView
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        ptName = sharedPreferences.getString("name", "User Name").toString()
        ptId = sharedPreferences.getString("id", "user@example.com").toString()

        drName = intent.getStringExtra("drName").toString()
        drId = intent.getStringExtra("drId").toString()
        //ptId = intent.getStringExtra("drId").toString()
        println("SANTYHOSH USER IS : "+ptName)

        viewModel.getSlots { slots ->
            if (slots != null) {
                // Successfully retrieved categories
                slotList.removeAll(slotList)
                slotList.addAll(slots)
                slotList[0].editSelection = true
                adapter.notifyDataSetChanged()
            } else {
                // Failed to retrieve categories
                println("Failed to retrieve Slot")
            }
        }
    }

    private fun onSelectAction(position: Int) {
        for (i in slotList.indices) {
            slotList[i].editSelection = false
            slotList[i].deleteSelection = false
        }
        slotList[position].editSelection = true
        selectedSlotId = slotList[position].slotId
        selectedSlotName = slotList[position].slotName
        adapter.notifyDataSetChanged()
    }
}