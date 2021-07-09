package com.pethiio.android.ui.main.view.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pethiio.android.R
import com.pethiio.android.databinding.FragmentAboutBinding
import com.pethiio.android.databinding.FragmentSettingsBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.viewmodel.FAQViewModel
import com.pethiio.android.utils.Constants


class AboutFragment : BaseFragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FAQViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        setupViewModel()
        setUpObserver()



        return view
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(FAQViewModel::class.java)
        viewModel.fetchAboutPageData()
    }

    private fun setUpObserver() {

        viewModel.getAboutPageData().observe(viewLifecycleOwner, {

            val pageDataFields = it.data?.fields

            binding.titleTv.text = getLocalizedString(Constants.registerTitle, pageDataFields)
            binding.descriptionTv.text =
                getLocalizedString(Constants.registerSubTitle, pageDataFields)


        })


    }


}