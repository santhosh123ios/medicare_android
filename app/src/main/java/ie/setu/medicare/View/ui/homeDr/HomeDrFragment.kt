package ie.setu.medicare.View.ui.homeDr

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import ie.setu.medicare.R
import ie.setu.medicare.View.CategoryActivity
import ie.setu.medicare.View.ui.home.HomeViewModel
import ie.setu.medicare.databinding.FragmentHomeBinding
import ie.setu.medicare.databinding.FragmentHomeDrBinding

/**
 * A simple [Fragment] subclass.
 * Use the [HomeDrFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeDrFragment : Fragment() {

    private var _binding: FragmentHomeDrBinding? = null

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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}