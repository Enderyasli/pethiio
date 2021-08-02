package com.pethiio.android.ui.main.view.fragments.mainFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pethiio.android.databinding.FragmentVetBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.adapter.VetAdapter


class VetFragment : BaseFragment() {

    private var _binding: FragmentVetBinding? = null

    private val binding get() = _binding!!


    override var bottomNavigationViewVisibility = View.VISIBLE
    override var dashboardClicked: Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentVetBinding.inflate(inflater, container, false)
        val view = binding.root

        return view

    }


    override fun setUpViews() {
        super.setUpViews()

        binding.vetRecycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter =
            VetAdapter(findNavController(), requireContext(), emptyList())
        binding.vetRecycler.adapter = adapter
    }

}