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
import com.pethiio.android.utils.Constants
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

    override fun onResume() {
        super.onResume()
        viewModel.fetchPetList()
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



        return view
    }


    private fun setupUI() {

        viewModel.fetchHomePageData()

        binding.noAnimalAnim.setAnimation("evim.json")

        viewModel.getHomePageData().observe(viewLifecycleOwner, {

            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    val fields = it.data?.fields
                    binding.titleTv.text = getLocalizedString(Constants.petSearchTitle, fields)
                    binding.addAnimalTv.text = getLocalizedString(Constants.addPet, fields)
                    binding.petEmptyErrorTv.text =
                        getLocalizedString(Constants.petSearchEmptyMessageTitle, fields)


                }

            }

        })

        binding.addAnimal.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.navigation_home)
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
            if (findNavController().currentDestination?.id == R.id.navigation_home)
                findNavController().navigate(R.id.navigation_settings)
        }


    }

    private fun setupObserver() {

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
//        if (!item.equals("All")) {
//            binding.noAnimalImg.visibility = View.VISIBLE
//            binding.noAnimalTv.visibility = View.VISIBLE
//            binding.animalListRv.visibility = View.GONE
//        } else {
//            binding.noAnimalImg.visibility = View.GONE
//            binding.noAnimalTv.visibility = View.GONE
//            binding.animalListRv.visibility = View.VISIBLE
//
//        }
    }


}