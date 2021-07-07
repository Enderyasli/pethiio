package com.pethiio.android.ui.main.view.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pethiio.android.R
import com.pethiio.android.databinding.FragmentSupportBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.base.ViewModelFactory
import com.pethiio.android.ui.main.adapter.FAQAdapter
import com.pethiio.android.ui.main.adapter.HomePetListAdapter
import com.pethiio.android.ui.main.viewmodel.FAQViewModel
import com.pethiio.android.ui.main.viewmodel.HomeViewModel
import com.pethiio.android.utils.Constants
import kotlinx.android.synthetic.main.fragment_support.view.*

class SupportFragment : BaseFragment() {


    override var
            bottomNavigationViewVisibility = View.GONE
    private var _binding: FragmentSupportBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FAQViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupportBinding.inflate(inflater, container, false)
        val view = binding.root

        setupViewModel()
        setUpObserver()
        binding.goReportLy.setOnClickListener {
            val bundle = bundleOf("fromFAQ" to true)
            findNavController().navigate(R.id.navigation_report, bundle)
        }
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()

        }

        return view

    }


    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(FAQViewModel::class.java)

        viewModel.fetchFAQPageData()
        viewModel.fetchFAQs()
    }

    private fun setUpObserver() {

        viewModel.getFAQPageData().observe(viewLifecycleOwner, {

            val pageDataFields = it.data?.fields

            binding.titleTv.text = getLocalizedString(Constants.registerTitle, pageDataFields)
            binding.supportTv.text =
                getLocalizedString(Constants.supportDetailButton, pageDataFields)

        })

        viewModel.getFaqs().observe(viewLifecycleOwner, {

            if (it.data != null) {
                binding.faqRecycler.layoutManager = LinearLayoutManager(requireContext())
                val adapter =
                    FAQAdapter(it.data)
                binding.faqRecycler.adapter = adapter
            }

        })

    }
}