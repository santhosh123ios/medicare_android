package ie.setu.medicare.View.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ie.setu.medicare.View.BookingListActivity
import ie.setu.medicare.View.CategoryActivity
import ie.setu.medicare.View.ReportListActivity
import ie.setu.medicare.View.SlotActivity
import ie.setu.medicare.View.UsersListActivity
import ie.setu.medicare.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        ////val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            ///textView.text = it
        }
        binding.catButton.setOnClickListener {
            val intent = Intent(activity, CategoryActivity::class.java)
            startActivity(intent)
        }

        binding.slotButton.setOnClickListener {
            val intent = Intent(activity, SlotActivity::class.java)
            startActivity(intent)
        }

        binding.reportButton.setOnClickListener {
            val intent = Intent(activity, ReportListActivity::class.java)
            startActivity(intent)
        }

        binding.bookingButton.setOnClickListener {
            val intent = Intent(activity, BookingListActivity::class.java)
            startActivity(intent)
        }
        binding.usersButton.setOnClickListener {
            val intent = Intent(activity, UsersListActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}