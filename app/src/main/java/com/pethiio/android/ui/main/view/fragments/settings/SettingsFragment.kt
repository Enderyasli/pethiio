package com.pethiio.android.ui.main.view.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pethiio.android.R
import com.pethiio.android.databinding.FragmentSettingsBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.viewmodel.FAQViewModel
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.PreferenceHelper


class SettingsFragment : BaseFragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FAQViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.supportLy.setOnClickListener {
            findNavController().navigate(R.id.navigation_support)
        }

        binding.aboutLy.setOnClickListener {
            findNavController().navigate(R.id.navigation_about)
        }
        binding.logoutLy.setOnClickListener {
            PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn=false
            PreferenceHelper.SharedPreferencesManager.getInstance().accessToken=""

            findNavController().navigate(R.id.action_global_navigation_welcome)
        }

        binding.passwordChangeTv.setOnClickListener {
            findNavController().navigate(R.id.navigation_change_password)
        }

        setupViewModel()
        setUpObserver()



        return view
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(FAQViewModel::class.java)
        viewModel.fetchSettingsPageData()
    }

    private fun setUpObserver() {

        viewModel.getSettingsPageData().observe(viewLifecycleOwner, {

            val pageDataFields = it.data?.fields

            binding.titleTv.text = getLocalizedString(Constants.registerTitle, pageDataFields)
            binding.supportTv.text =
                getLocalizedString(Constants.supportTitle, pageDataFields)

            binding.passwordChangeTv.text =
                getLocalizedString(Constants.changePasswordTitle, pageDataFields)

            binding.notificationTv.text =
                getLocalizedString(Constants.notificationTitle, pageDataFields)
            binding.aboutTv.text =
                getLocalizedString(Constants.aboutAppTitle, pageDataFields)
            binding.logoutTv.text =
                getLocalizedString(Constants.logoutTitle, pageDataFields)

        })


    }


}