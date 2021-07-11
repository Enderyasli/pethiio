package com.pethiio.android.ui.main.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pethiio.android.data.model.notification.NotificationListResponse
import com.pethiio.android.databinding.FragmentNotificationBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.viewmodel.FAQViewModel
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status
import com.suke.widget.SwitchButton


class NotificationFragment : BaseFragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FAQViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        setupViewModel()
        setUpObserver()

        binding.newMessageSwitchButton.setOnCheckedChangeListener { _, isChecked ->

            viewModel.fetchPostNotification(
                NotificationListResponse(
                    isChecked,
                    Constants.notificationNewMesage
                )
            )
        }
        binding.newMatchSwitchButton.setOnCheckedChangeListener { _, isChecked ->

            viewModel.fetchPostNotification(
                NotificationListResponse(
                    isChecked,
                    Constants.notificationNewMatch
                )
            )
        }
        binding.promotionalSwitchButton.setOnCheckedChangeListener { _, isChecked ->

            viewModel.fetchPostNotification(
                NotificationListResponse(
                    isChecked,
                    Constants.notificationPromotional
                )
            )
        }



        return view

    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(FAQViewModel::class.java)
        viewModel.fetchNotificationSettingsPageData()
        viewModel.fetchNotificationListPageData()
    }

    private fun setUpObserver() {

        viewModel.getNotificationSettingsPageData().observe(viewLifecycleOwner, {

            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE

                }
                Status.SUCCESS -> {

                    binding.progressBar.visibility = View.GONE

                    val pageDataFields = it.data?.fields

                    binding.titleTv.text =
                        getLocalizedString(Constants.registerTitle, pageDataFields)
                    binding.newMessageTv.text =
                        getLocalizedString(Constants.notificationNewMesageTitle, pageDataFields)
                    binding.newMatchTv.text =
                        getLocalizedString(Constants.notificationNewMatchTitle, pageDataFields)
                    binding.promotionalTv.text =
                        getLocalizedString(Constants.notificationPromotionalTitle, pageDataFields)

                }
            }


        })

        viewModel.getNotificationList().observe(viewLifecycleOwner, { it ->

            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE

                }
                Status.SUCCESS -> {

                    val notificationList = it.data

                    notificationList?.forEach {
                        if (it.type == Constants.notificationNewMesage) {
                            binding.newMessageSwitchButton.isChecked = it.active
                        }
                        if (it.type == Constants.notificationNewMatch) {
                            binding.newMatchSwitchButton.isChecked = it.active
                        }
                        if (it.type == Constants.notificationPromotional) {
                            binding.promotionalSwitchButton.isChecked = it.active
                        }

                    }


                    binding.progressBar.visibility = View.GONE


                }
            }


        })


    }

}