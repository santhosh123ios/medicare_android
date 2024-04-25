package ie.setu.medicare.View

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ie.setu.medicare.Model.Appointments
import ie.setu.medicare.Model.Report
import ie.setu.medicare.R
import ie.setu.medicare.View.Adapter.ItemAppoinmentAdapter
import ie.setu.medicare.ViewModels.SignInActivityVM
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateReportActivity : AppCompatActivity() {

    private val viewModel: SignInActivityVM by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    val database = Firebase.database
    var ptName = ""
    var ptId = ""
    var drName = ""
    var drId = ""
    var apId = ""
    var apSlot = ""
    var apDate = ""
    var crntDate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_report)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ptName = intent.getStringExtra("ptName").toString()
        ptId = intent.getStringExtra("ptId").toString()
        drName = intent.getStringExtra("drName").toString()
        drId = intent.getStringExtra("drId").toString()
        apId = intent.getStringExtra("apId").toString()
        apSlot = intent.getStringExtra("apSlot").toString()
        apDate = intent.getStringExtra("apDate").toString()

        val currentDate = Date()
        // Format the date
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormatter.format(currentDate)
        // Set the button text to the formatted date
        crntDate = formattedDate

        findViewById<ImageView>(R.id.back_button).setOnClickListener {
            finish()
        }
        findViewById<TextView>(R.id.itemText).text = "Patient : "+ptName
        findViewById<TextView>(R.id.slotText).text = "Slot : "+apSlot
        findViewById<TextView>(R.id.dateText).text = "Date : "+apDate
        var reportText = findViewById<EditText>(R.id.editTextTextMultiLine)

        findViewById<Button>(R.id.submitBtn).setOnClickListener {

            if (reportText.text.isNotEmpty())
            {
                val myRef = database.getReference("reports")
                val rpId = myRef.push().key
                val report = Report("$rpId","$crntDate","$apSlot","$apId","$ptName","$ptId","$drName","$drId",reportText.text.toString())
                viewModel.createReport(report) { isSuccess ->
                    if (isSuccess) {
                        // Insertion successful
                        println("Report created successfully")
                        updateStatus(apId)

                    } else {
                        // Insertion failed
                        println("Failed to create Report")
                        Toast.makeText(this, "Failed to create Report", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else
            {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun updateStatus(apId: String) {
        viewModel.cancelBooking(apId, "completed") { isSuccess ->
            if (isSuccess) {
                // Update successful
                println("Successfully updated.")
                Toast.makeText(this, "Report created successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                // Update failed
                println("Failed to update booking.")
                Toast.makeText(this, "Failed to  update.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}