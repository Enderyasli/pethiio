package com.pethiio.android.ui.main.view.fragments

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.pethiio.android.R
import com.pethiio.android.databinding.FragmentDashboardBinding
import com.pethiio.android.databinding.FragmentLocationBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.base.ViewModelFactory
import com.pethiio.android.ui.main.viewmodel.DashBoardViewModel
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status


class LocationFragment : BaseFragment() {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!
    override var bottomNavigationViewVisibility = View.GONE

    val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private lateinit var viewModel: DashBoardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel =
            ViewModelProviders.of(this, ViewModelFactory()).get(DashBoardViewModel::class.java)


        binding.locationAnim.setAnimation("konum_hizmetleri.json")

        viewModel.fetchLocationPageData()

        viewModel.getLocationPageData().observe(viewLifecycleOwner, {

            when (it.status) {
                Status.LOADING -> {
                }

                Status.SUCCESS -> {
                    val fields = it.data?.fields
                    binding.titleTv.text = getLocalizedString(Constants.registerTitle, fields)
                    binding.descriptionTv.text =
                        getLocalizedString(Constants.registerSubTitle, fields)
                    binding.completeBtn.text = getLocalizedString(Constants.locationButton, fields)

                }

            }
        })

        binding.completeBtn.setOnClickListener {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }



        return view

    }

}