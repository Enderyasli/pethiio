package com.pethiio.android.ui.main.view.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
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
                    binding.progressBar.visibility=View.VISIBLE
                }

                Status.SUCCESS -> {
                    val fields = it.data?.fields
                    binding.titleTv.text = getLocalizedString(Constants.registerTitle, fields)
                    binding.descriptionTv.text =
                        getLocalizedString(Constants.registerSubTitle, fields)
                    binding.completeBtn.text = getLocalizedString(Constants.locationButton, fields)

                    binding.progressBar.visibility=View.GONE

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

    // TODO: 9.07.2021 buraya girmiyor
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {

                        findNavController().navigateUp()

                    }

                } else {
                }
            }
        }
    }

}