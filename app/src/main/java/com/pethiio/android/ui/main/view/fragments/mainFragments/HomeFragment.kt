package com.pethiio.android.ui.main.view.fragments.mainFragments

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.pethiio.android.R
import com.pethiio.android.data.model.PetListResponse
import com.pethiio.android.data.model.User
import com.pethiio.android.databinding.FragmentHomeBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.base.ViewModelFactory
import com.pethiio.android.ui.main.adapter.FilterItemClickListener
import com.pethiio.android.ui.main.adapter.HomePetListAdapter
import com.pethiio.android.ui.main.adapter.MainAdapter
import com.pethiio.android.ui.main.viewmodel.HomeViewModel
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status


@Suppress("UNCHECKED_CAST")
class HomeFragment : BaseFragment() {


    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: MainAdapter
    private lateinit var viewModel: HomeViewModel

    override var bottomNavigationViewVisibility = View.VISIBLE
    override var dashboardClicked: Boolean = false

    var petList: List<PetListResponse>? = null


    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState) }

//    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putParcelableArrayList("key", petList as ArrayList<PetListResponse>)
//        super.onSaveInstanceState(outState)
//    }

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

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            val pet = savedInstanceState.getParcelableArrayList<Parcelable>("key")
        }

        setupViewModel()
        setupUI()



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
                        getLocalizedString(Constants.petEmpty, fields)


                }

            }

        })

        binding.addAnimal.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.navigation_home)
                findNavController().navigate(R.id.navigation_pet_add)
        }


        viewModel.getPetList().observe(viewLifecycleOwner, {


            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {

                    if (it.data != null) {

                        petList = it.data
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

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            requireActivity(),
            ViewModelFactory()
        ).get(HomeViewModel::class.java)
    }




}