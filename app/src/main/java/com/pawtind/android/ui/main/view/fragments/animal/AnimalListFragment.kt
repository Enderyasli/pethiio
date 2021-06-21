package com.pawtind.android.ui.main.view.fragments.animal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.pawtind.android.R
import com.pawtind.android.databinding.FragmentAnimalListBinding
import com.pawtind.android.ui.base.BaseFragment

class AnimalListFragment : BaseFragment() {

    override var bottomNavigationViewVisibility = View.GONE
    private var _binding: FragmentAnimalListBinding? = null

    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAnimalListBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.completeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_animal_list_to_navigation_main)
        }


        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}