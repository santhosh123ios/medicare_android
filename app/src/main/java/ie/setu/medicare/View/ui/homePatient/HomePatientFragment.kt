package ie.setu.medicare.View.ui.homePatient

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.medicare.Model.CategoryList
import ie.setu.medicare.Model.Users
import ie.setu.medicare.R
import ie.setu.medicare.View.Adapter.ItemAdapter
import ie.setu.medicare.View.Adapter.ItemCatHorizontalAdapter
import ie.setu.medicare.View.AppointmentCreateActivity
import ie.setu.medicare.View.PatientActivity
import ie.setu.medicare.View.ui.homeDr.HomeDrViewModel
import ie.setu.medicare.ViewModels.SignInActivityVM
import ie.setu.medicare.databinding.FragmentHomeDrBinding
import ie.setu.medicare.databinding.FragmentHomePatientBinding
import kotlinx.android.parcel.Parcelize

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomePatientFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomePatientFragment : Fragment() {

    private var _binding: FragmentHomePatientBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemCatHorizontalAdapter
    val usersList = mutableListOf<Users>()
    private val viewModel: SignInActivityVM by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomePatientViewModel::class.java)

        _binding = FragmentHomePatientBinding.inflate(inflater, container, false)
        val root: View = binding.root

        ////val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            ///textView.text = it
        }
//        binding.catButton.setOnClickListener {
//            val intent = Intent(activity, CategoryActivity::class.java)
//            startActivity(intent)
//        }

        // Initialize your RecyclerView and Adapter
        recyclerView = binding.recyclerView
        adapter = ItemCatHorizontalAdapter(usersList, ::onSelectAction)
        //recyclerView.layoutManager = LinearLayoutManager(this)
        // Set a horizontal LinearLayoutManager on the RecyclerView
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter



        // Add some sample data to the list
        ////items.addAll(listOf("Item 1", "Item 2", "Item 3"))
        ///getCategoriesData()
        getDrList()

        return root
    }
    private fun onSelectAction(position: Int) {

        val intent = Intent(requireContext(), AppointmentCreateActivity::class.java)
        //val user = Users("John Doe", "john.doe@example.com", "1234567890", 1, "password", "catId", "place")
        intent.putExtra("name", usersList[position].name)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getDrList() {

        viewModel.getDrList { items ->
            if (items != null) {
                // Successfully retrieved items of type 1
                usersList.removeAll(usersList)
                usersList.addAll(items)
                adapter.notifyDataSetChanged()
//                for (item in items) {
//                    println("Name: ${item.name}, Email: ${item.email}, Phone: ${item.phone}, Type: ${item.type}, CatId: ${item.catId}, Place: ${item.place}")
//                }
            } else {
                // Failed to retrieve items
                println("Failed to retrieve items of type 1.")
            }
        }
//        viewModel.getCategories { categories ->
//            if (categories != null) {
//                // Successfully retrieved categories
//                categoryList.removeAll(categoryList)
//                categoryList.addAll(categories)
//                adapter.notifyDataSetChanged()
//            } else {
//                // Failed to retrieve categories
//                println("Failed to retrieve categories")
//            }
//        }
    }
}