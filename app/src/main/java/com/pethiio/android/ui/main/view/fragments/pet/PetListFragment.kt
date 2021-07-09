package com.pethiio.android.ui.main.view.fragments.pet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.pethiio.android.R
import com.pethiio.android.data.model.PetListResponse
import com.pethiio.android.databinding.FragmentPetListBinding
import com.pethiio.android.ui.base.RegisterBaseFragment
import com.pethiio.android.ui.main.adapter.PetListAdapter
import com.pethiio.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pethiio.android.utils.Constants

class PetListFragment : RegisterBaseFragment<RegisterBaseViewModel>() {

    override var bottomNavigationViewVisibility = View.GONE
    private var _binding: FragmentPetListBinding? = null

    private val binding get() = _binding!!

    override var useSharedViewModel = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPetListBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.completeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_animal_list_to_navigation_main)
        }


        return view

    }

    override fun setUpViews() {
        super.setUpViews()


        viewModel.petListPageData.observe(viewLifecycleOwner, {petData->

            setPethiioResponseList(petData.data)
            binding.petlistTitle.text = getLocalizedString(Constants.animalListTitle)
            binding.petlistSubtitle.text = getLocalizedString(Constants.animalListSubTitle)
            binding.completeBtn.text = getLocalizedString(Constants.animalListCompleteButtonTitle)


            viewModel.getPetList().observe(viewLifecycleOwner, {

                if(it.data!=null){
                    val list2 = ArrayList<PetListResponse>(it.data)

                    binding.petlistRv.layoutManager = GridLayoutManager(requireContext(), 2)
                    val adapter = PetListAdapter(requireContext(),findNavController(), list2, getLocalizedString(Constants.animalListAddNewTitle))
                    binding.petlistRv.adapter = adapter
                }


            })


        })

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun getViewModelClass() = RegisterBaseViewModel::class.java


}