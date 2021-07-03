package com.pethiio.android.ui.main.view.fragments

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pethiio.android.data.model.petDetail.PetSearchDetailResponse
import com.pethiio.android.databinding.FragmentPetDetailBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.base.ViewModelFactory
import com.pethiio.android.ui.main.adapter.CharacterAdapter
import com.pethiio.android.ui.main.adapter.PetSearchDetailCharacterAdapter
import com.pethiio.android.ui.main.adapter.ViewPagerAdapter
import com.pethiio.android.ui.main.viewmodel.PetDetailViewModel
import com.pethiio.android.utils.Constants
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

    @SuppressLint("SetTextI18n")
    override fun setUpViews() {
        super.setUpViews()

        viewModel.fetchPetDetailPageData()
        viewModel.fetchPetDetail()

        viewModel.getPetDetailPageData().observe(viewLifecycleOwner, {


            val fields = it.data?.fields

            binding.colorLy.title.text =
                getLocalizedString(Constants.petSearchDetailAboutColorTitle, fields)
            binding.listLy.title.text =
                getLocalizedString(Constants.petSearchDetailListTypeTitle, fields)
            binding.detailLy.title.text =
                getLocalizedString(Constants.petSearchDetailDetailTitle, fields)

        })

        viewModel.getPetDetail().observe(viewLifecycleOwner, {

            val petDetail: PetSearchDetailResponse? = it.data
            when (it.status) {

                Status.SUCCESS -> {

                    petDetail?.photos?.let { it1 ->
                        val mViewPagerAdapter = ViewPagerAdapter(requireContext(), it1)
                        binding.imagePager.adapter = mViewPagerAdapter
                    }

                    binding.characterRv.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                    val adapterCharacter = petDetail?.personalities?.let { it1 ->
                        PetSearchDetailCharacterAdapter(
                            requireContext(),
                            it1
                        )
                    }

                    binding.characterRv.adapter = adapterCharacter

                    binding.petNameTv.text = petDetail?.name
                    binding.petBreedTv.text = petDetail?.breed

                    binding.genderTv.text = petDetail?.gender

                    binding.colorLy.value.text = petDetail?.color
                    binding.listLy.value.text = petDetail?.purpose
                    binding.detailLy.value.text = petDetail?.about

                    binding.ageTv.text = petDetail?.age
                    binding.distanceTv.text = petDetail?.distance.toString() + " km"


                }
                Status.ERROR -> {

                }
            }


        })


    }


}