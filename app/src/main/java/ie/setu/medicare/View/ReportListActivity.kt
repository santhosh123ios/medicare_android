package ie.setu.medicare.View

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.medicare.Model.Appointment
import ie.setu.medicare.Model.Reports
import ie.setu.medicare.R
import ie.setu.medicare.View.Adapter.ItemBookingAdapter
import ie.setu.medicare.ViewModels.SignInActivityVM

class ReportListActivity : AppCompatActivity() {

    val repoprtList = mutableListOf<Reports>()
    private val viewModel: SignInActivityVM by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemReportAdapter
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report_list)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        sharedPreferences = getSharedPreferences("auth_pref", Context.MODE_PRIVATE)
        // Initialize your RecyclerView and Adapter
        recyclerView = findViewById(R.id.reportRecyclerView)
        adapter = ItemReportAdapter(repoprtList, ::onSelectAction)
        //recyclerView.layoutManager = LinearLayoutManager(this)
        // Set a horizontal LinearLayoutManager on the RecyclerView
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        findViewById<ImageView>(R.id.back_button).setOnClickListener {
            finish()
        }

        getReortList()
    }

    private fun getReortList() {

        viewModel.getMyReportAllList() { items ->
            if (items != null) {
                // Successfully retrieved items of type 1
                repoprtList.removeAll(repoprtList)
                repoprtList.addAll(items)
                adapter.notifyDataSetChanged()
            } else {
                // Failed to retrieve items
                println("Failed to retrieve items of type 1.")
            }
        }
    }

    private fun onSelectAction(position: Int) {
        val intent = Intent(this, ReportDetailsActivity::class.java)
        //val user = Users("John Doe", "john.doe@example.com", "1234567890", 1, "password", "catId", "place")
        intent.putExtra("ptName", repoprtList[position].ptName)
        intent.putExtra("ptId", repoprtList[position].ptId)
        intent.putExtra("drName", repoprtList[position].drName)
        intent.putExtra("drId", repoprtList[position].drId)
        intent.putExtra("rpId", repoprtList[position].rpId)
        intent.putExtra("rpId", repoprtList[position].rpId)
        intent.putExtra("rpDate", repoprtList[position].rpDate)
        intent.putExtra("report", repoprtList[position].report)
        startActivity(intent)
    }
}