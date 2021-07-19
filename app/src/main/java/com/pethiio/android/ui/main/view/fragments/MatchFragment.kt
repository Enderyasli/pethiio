package com.pethiio.android.ui.main.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pethiio.android.R
import com.pethiio.android.databinding.FragmentMatchBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.viewmodel.MatchViewModel
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status


class MatchFragment : BaseFragment() {


    private var _binding: FragmentMatchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MatchViewModel
    override var bottomNavigationViewVisibility = View.GONE

    private var fromNotification = false
    var memberId: Int = 0
    var roomId: Int = 0
    private var purpose: String = ""
    private var sourceName: String = ""
    private var sourceAvatar: String = ""
    private var targetName: String = ""
    private var targetAvatar: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMatchBinding.inflate(inflater, container, false)
        val view = binding.root



        fromNotification = arguments?.getBoolean("fromNotification", false) == true
        if (fromNotification) {
            roomId = arguments?.getInt("roomId", 0)!!
            memberId = arguments?.getInt("memberId", 0)!!
            purpose = arguments?.getString("purpose", "")!!
            sourceName = arguments?.getString("sourceName", "")!!
            sourceAvatar = arguments?.getString("sourceAvatar", "")!!
            targetName = arguments?.getString("targetName", "")!!
            targetAvatar = arguments?.getString("targetAvatar", "")!!
        }
        if (purpose == "DATING") {
            binding.animBelow.setAnimation("kalp_match.json")
            binding.animTop.setAnimation("kalp_match.json")
        } else {
            binding.animBelow.setAnimation("arkadas_match.json")
            binding.animTop.setAnimation("arkadas_match.json")
        }

        Glide.with(binding.targetAvatar)
            .load(targetAvatar)
            .apply(RequestOptions().override(1024, 1024))
            .into(binding.targetAvatar)
        binding.targetNameTv.text = targetName

        Glide.with(binding.sourceAvatar)
            .load(sourceAvatar)
            .apply(RequestOptions().override(1024, 1024))
            .into(binding.sourceAvatar)
        binding.sourceNameTv.text = sourceName


        setupViewModel()
        setUpObserver()

        binding.continueBtn.setOnClickListener {
            val bundle =
                bundleOf(
                    "fromNotification" to false
                )
            findNavController().navigate(R.id.navigation_dashboard, bundle)
        }
        binding.goChatBtn.setOnClickListener {

            val bundle =
                bundleOf(
                    "fromNotification" to true, "roomId" to roomId, "memberId" to memberId
                )
            findNavController().navigate(R.id.navigation_message, bundle)

        }

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
                        getLocalizedString(Constants.registerTitle, pageDataFields)
                    binding.goChatBtn.text =
                        getLocalizedString(Constants.chatButton, pageDataFields)
                    binding.continueBtn.text =
                        getLocalizedString(Constants.matchSkipButton, pageDataFields)

                }
            }


        })
    }


}