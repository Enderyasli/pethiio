package com.pethiio.android.ui.main.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.pethiio.android.databinding.FragmentMatchBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.viewmodel.MatchViewModel
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status


class MatchFragment : BaseFragment() {


    private var _binding: FragmentMatchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MatchViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMatchBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.animBelow.setAnimation("kalp_match.json")
        binding.animTop.setAnimation("kalp_match.json")

        setupViewModel()
        setUpObserver()

        return view

    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MatchViewModel::class.java)
    }

    private fun setUpObserver() {

        viewModel.getMatchPageData().observe(viewLifecycleOwner, {

            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE

                }
                Status.SUCCESS -> {

                    binding.progressBar.visibility = View.GONE
//
                    val pageDataFields = it.data?.fields
//
                    binding.titleTv.text =
                        getLocalizedString(Constants.animalListHeader, pageDataFields)
//                    binding.newMessageTv.text =
//                        getLocalizedString(Constants.notificationNewMesageTitle, pageDataFields)
//                    binding.newMatchTv.text =
//                        getLocalizedString(Constants.notificationNewMatchTitle, pageDataFields)
//                    binding.promotionalTv.text =
//                        getLocalizedString(Constants.notificationPromotionalTitle, pageDataFields)

                }
            }


        })
    }


}