package com.pethiio.android.ui.main.view.fragments

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.pethiio.android.R
import com.pethiio.android.data.model.User
import com.pethiio.android.databinding.FragmentHomeBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.base.ViewModelFactory
import com.pethiio.android.ui.main.adapter.*
import com.pethiio.android.ui.main.viewmodel.HomeViewModel
import com.pethiio.android.utils.Status


class HomeFragment : BaseFragment(), FilterItemClickListener {


    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: MainAdapter
    private lateinit var viewModel: HomeViewModel

    override var bottomNavigationViewVisibility = View.VISIBLE
    override var dashboardClicked: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        setupViewModel()
        setupUI()
        setupObserver()

        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !==
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // TODO: 6.07.2021 burda location izin ekranına gönder, gelişte location actır, sonra lat, lon al
//                findNavController().navigate(R.id.navigation_report)
//                ActivityCompat.requestPermissions(requireActivity(),
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
//                findNavController().navigate(R.id.navigation_report)
//                ActivityCompat.requestPermissions(requireActivity(),
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }



        return view
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) ===
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    private fun setupUI() {

//        createLocationRequest()


        binding.addAnimal.setOnClickListener {
            findNavController().navigate(R.id.navigation_pet_add)
        }


//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        adapter = MainAdapter(arrayListOf())
//        recyclerView.addItemDecoration(
//            DividerItemDecoration(
//                recyclerView.context,
//                (recyclerView.layoutManager as LinearLayoutManager).orientation
//            )
//        )
//        recyclerView.adapter = adapter


//        binding.animalRv.layoutManager = GridLayoutManager(requireContext(), 3)
//        val adapter = FilterAdapter(
//            requireContext(),
//            arrayListOf("All", "Cat", "Dog"),
//            this@HomeFragment
//        )
//        binding.animalRv.adapter = adapter

        viewModel.getPetList().observe(viewLifecycleOwner, {

            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE

                }
                Status.SUCCESS -> {

                    if (it.data != null) {
                        binding.animalListRv.layoutManager = GridLayoutManager(requireContext(), 2)
                        val adapter =
                            HomePetListAdapter(findNavController(), requireContext(), it.data)
                        binding.animalListRv.adapter = adapter
                    }
                    if (it.data?.size == 0) {
                        binding.emptyLayout.visibility = View.VISIBLE
                    } else {
                        binding.emptyLayout.visibility = View.GONE
                    }



                    binding.progressBar.visibility = View.GONE

                }
            }


        })
        binding.settingsImg.setOnClickListener {
            findNavController().navigate(R.id.navigation_support)
        }


    }

    private fun setupObserver() {

    }


    fun createLocationRequest() {
        val locationRequest = LocationRequest.create().apply {
            interval = 3000
            fastestInterval = 1500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)


        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnCompleteListener {
            try {
                task.getResult(ApiException::class.java)


            } catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        if (e is ResolvableApiException) {

                            try {
                                e.startResolutionForResult(requireActivity(), 6989)
                            } catch (sendEx: IntentSender.SendIntentException) {
                                Log.e("sednex", sendEx.toString())

                            }
                        }
                    }
                }


            }
        }

//        task.addOnSuccessListener { locationSettingsResponse ->
//            // All location settings are satisfied. The client can initialize
//            // location requests here.
//            // ...
//        }
//
//        task.addOnFailureListener { exception ->
//            if (exception is ResolvableApiException){
//                // Location settings are not satisfied, but this can be fixed
//                // by showing the user a dialog.
//                try {
//                    // Show the dialog by calling startResolutionForResult(),
//                    // and check the result in onActivityResult().
//                    exception.startResolutionForResult(requireActivity(),
//                        100)
//                } catch (sendEx: IntentSender.SendIntentException) {
//                    // Ignore the error.
//                }
//            }
//        }
    }

    private fun renderList(users: List<User>) {
        adapter.addData(users)
        adapter.notifyDataSetChanged()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory()
        ).get(HomeViewModel::class.java)
    }

    override fun onFilterItemClick(item: String?) {
        if (!item.equals("All")) {
            binding.noAnimalImg.visibility = View.VISIBLE
            binding.noAnimalTv.visibility = View.VISIBLE
            binding.animalListRv.visibility = View.GONE
        } else {
            binding.noAnimalImg.visibility = View.GONE
            binding.noAnimalTv.visibility = View.GONE
            binding.animalListRv.visibility = View.VISIBLE

        }
    }


}