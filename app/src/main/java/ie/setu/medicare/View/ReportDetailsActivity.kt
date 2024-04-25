package ie.setu.medicare.View

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ie.setu.medicare.R
import ie.setu.medicare.ViewModels.SignInActivityVM
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportDetailsActivity : AppCompatActivity() {

    private val viewModel: SignInActivityVM by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    val database = Firebase.database
    var ptName = ""
    var ptId = ""
    var drName = ""
    var drId = ""
    var apId = ""
    var rpId = ""
    var rpDate = ""
    var crntDate = ""
    var report = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report_details)
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
        rpId = intent.getStringExtra("rpId").toString()
        rpDate = intent.getStringExtra("rpDate").toString()
        report = intent.getStringExtra("report").toString()

        val currentDate = Date()
        // Format the date
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormatter.format(currentDate)
        // Set the button text to the formatted date
        crntDate = formattedDate
        findViewById<ImageView>(R.id.back_button).setOnClickListener {
            finish()
        }
        findViewById<TextView>(R.id.itemText).text = "Name : "+ptName
        findViewById<TextView>(R.id.idText).text = "Id : "+ptId
        findViewById<TextView>(R.id.rpIdText).text = "Report Id : "+rpId
        findViewById<TextView>(R.id.dateText).text = "Date : "+rpDate
        findViewById<TextView>(R.id.editTextTextMultiLine).text = report
    }
}