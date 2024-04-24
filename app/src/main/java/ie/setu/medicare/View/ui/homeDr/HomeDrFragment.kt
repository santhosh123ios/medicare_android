package ie.setu.medicare.View.ui.homeDr

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.medicare.Model.Appointment
import ie.setu.medicare.R
import ie.setu.medicare.View.Adapter.ItemAppoinmentAdapter
import ie.setu.medicare.View.Adapter.ItemBookingAdapter
import ie.setu.medicare.View.AppointmentCreateActivity
import ie.setu.medicare.View.CategoryActivity
import ie.setu.medicare.View.CreateReportActivity
import ie.setu.medicare.View.SlotActivity
import ie.setu.medicare.View.ui.home.HomeViewModel
import ie.setu.medicare.ViewModels.SignInActivityVM
import ie.setu.medicare.databinding.FragmentGalleryBinding
import ie.setu.medicare.databinding.FragmentHomeBinding
import ie.setu.medicare.databinding.FragmentHomeDrBinding

/**
 * A simple [Fragment] subclass.
 * Use the [HomeDrFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeDrFragment : Fragment() {

    private var _binding: FragmentHomeDrBinding? = null

    val appointmentList = mutableListOf<Appointment>()
    private val viewModel: SignInActivityVM by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAppoinmentAdapter
    private lateinit var sharedPreferences: SharedPreferences

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeDrViewModel::class.java)

        _binding = FragmentHomeDrBinding.inflate(inflater, container, false)
        val root: View = binding.root

        ////val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            ///textView.text = it
        }
//        binding.catButton.setOnClickListener {
//            val intent = Intent(activity, CategoryActivity::class.java)
//            startActivity(intent)
//        }

        // Get the SharedPreferences instance
        sharedPreferences = requireActivity().getSharedPreferences("auth_pref", Context.MODE_PRIVATE)
        // Initialize your RecyclerView and Adapter
        recyclerView = binding.appoinmentRecyclerView
        ///recyclerView = findViewById(R.id.slotRecyclerView)
        adapter = ItemAppoinmentAdapter(appointmentList, ::onSelectAction)
        //recyclerView.layoutManager = LinearLayoutManager(this)
        // Set a horizontal LinearLayoutManager on the RecyclerView
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        getBookingList()

        return root
    }

    override fun onResume() {
        super.onResume()
        getBookingList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getBookingList() {

        val drId = sharedPreferences.getString("id", "id").toString()
        viewModel.getMyAppoinmentList(drId) { items ->
            if (items != null) {
                // Successfully retrieved items of type 1
                appointmentList.removeAll(appointmentList)
                appointmentList.addAll(items)
                adapter.notifyDataSetChanged()

            } else {
                // Failed to retrieve items
                println("Failed to retrieve items of type 1.")
            }
        }
    }

    private fun onSelectAction(position: Int) {
        val intent = Intent(requireContext(), CreateReportActivity::class.java)
        //val user = Users("John Doe", "john.doe@example.com", "1234567890", 1, "password", "catId", "place")
        intent.putExtra("ptName", appointmentList[position].ptName)
        intent.putExtra("ptId", appointmentList[position].ptId)
        intent.putExtra("drName", appointmentList[position].drName)
        intent.putExtra("drId", appointmentList[position].drId)
        intent.putExtra("apId", appointmentList[position].apId)
        intent.putExtra("apSlot", appointmentList[position].apSlot)
        intent.putExtra("apDate", appointmentList[position].apDate)
        startActivity(intent)
//        viewModel.cancelBooking(appointmentList[position].apId, "completed") { isSuccess ->
//            if (isSuccess) {
//                // Update successful
//                println("Successfully canceled.")
//                getBookingList()
//            } else {
//                // Update failed
//                println("Failed to update booking.")
//                Toast.makeText(context, "Failed to  update booking.", Toast.LENGTH_SHORT).show()
//            }
//        }
    }
}