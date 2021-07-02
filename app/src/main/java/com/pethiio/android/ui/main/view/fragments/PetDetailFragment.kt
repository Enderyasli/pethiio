package com.pethiio.android.ui.main.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.pethiio.android.R
import com.pethiio.android.data.model.petDetail.PetDetailResponse
import com.pethiio.android.databinding.FragmentPetDetailBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.base.ViewModelFactory
import com.pethiio.android.ui.main.adapter.ViewPagerAdapter
import com.pethiio.android.ui.main.viewmodel.DashBoardViewModel
import com.pethiio.android.ui.main.viewmodel.PetDetailViewModel
import com.pethiio.android.utils.Status


class PetDetailFragment : BaseFragment() {

    private var _binding: FragmentPetDetailBinding? = null
    private val binding get() = _binding!!
    override var bottomNavigationViewVisibility = View.GONE

    private lateinit var viewModel: PetDetailViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel =
            ViewModelProviders.of(this, ViewModelFactory()).get(PetDetailViewModel::class.java)






        return view
    }

    override fun setUpViews() {
        super.setUpViews()

        viewModel.fetchPetDetailPageData()
        viewModel.fetchPetDetail()
        viewModel.getPetDetail().observe(viewLifecycleOwner, {

            val petDetail: PetDetailResponse? = it.data
            when (it.status) {

                Status.SUCCESS -> {

                    petDetail?.photos?.let { it1 ->
                        val mViewPagerAdapter = ViewPagerAdapter(requireContext(), it1)
                        binding.imagePager.adapter = mViewPagerAdapter
                    }
                    binding.petNameTv.text = petDetail?.name
                    binding.petBreedTv.text = petDetail?.breed

                    binding.colorLy.value.text = petDetail?.color
                    binding.listLy.value.text = petDetail?.purpose
                    binding.detailLy.value.text = petDetail?.about


                }
                Status.ERROR -> {

                }
            }


        })


    }


}