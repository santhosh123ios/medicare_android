package ie.setu.medicare.View.ui.gallery

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.medicare.Model.Appointment
import ie.setu.medicare.Model.Users
import ie.setu.medicare.R
import ie.setu.medicare.View.Adapter.ItemBookingAdapter
import ie.setu.medicare.View.Adapter.ItemCatHorizontalAdapter
import ie.setu.medicare.ViewModels.SignInActivityVM
import ie.setu.medicare.databinding.FragmentGalleryBinding

class PatientBookingListFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    val bookingList = mutableListOf<Appointment>()
    private val viewModel: SignInActivityVM by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemBookingAdapter
    private lateinit var sharedPreferences: SharedPreferences
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner) {
            //textView.text = it
        }

        // Get the SharedPreferences instance
        sharedPreferences = requireActivity().getSharedPreferences("auth_pref", Context.MODE_PRIVATE)
        // Initialize your RecyclerView and Adapter
        recyclerView = binding.bookingRecyclerView
        ///recyclerView = findViewById(R.id.slotRecyclerView)
        adapter = ItemBookingAdapter(bookingList, ::onSelectAction)
        //recyclerView.layoutManager = LinearLayoutManager(this)
        // Set a horizontal LinearLayoutManager on the RecyclerView
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        getBookingList()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getBookingList() {

        val ptId = sharedPreferences.getString("id", "id").toString()
        viewModel.getMyBookingList(ptId) { items ->
            if (items != null) {
                // Successfully retrieved items of type 1
                bookingList.removeAll(bookingList)
                bookingList.addAll(items)
                adapter.notifyDataSetChanged()
//                for (item in items) {
//                    println("Name: ${item.name}, Email: ${item.email}, Phone: ${item.phone}, Type: ${item.type}, CatId: ${item.catId}, Place: ${item.place}")
//                }
            } else {
                // Failed to retrieve items
                println("Failed to retrieve items of type 1.")
            }
        }
    }

    private fun onSelectAction(position: Int) {
        viewModel.cancelBooking(bookingList[position].apId, "canceled") { isSuccess ->
            if (isSuccess) {
                // Update successful
                println("Successfully canceled.")
                getBookingList()
            } else {
                // Update failed
                println("Failed to update booking.")
                Toast.makeText(context, "Failed to  update booking.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}