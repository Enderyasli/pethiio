package com.pethiio.android.ui.main.view.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.pethiio.android.R
import com.pethiio.android.databinding.FragmentSettingsBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.view.customViews.PethiioDialog
import com.pethiio.android.ui.main.viewmodel.FAQViewModel
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.PreferenceHelper
import com.pethiio.android.utils.Status


class SettingsFragment : BaseFragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FAQViewModel

    private var logOutTitle = ""
    private var logoutAlertTitle = ""
    private var logoutAlertDone = ""
    private var logoutAlertCancel = ""
    private var isDialogShowing = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.editProfileLy.setOnClickListener {
            findNavController().navigate(R.id.navigation_user_edit)
        }

        binding.supportLy.setOnClickListener {
            findNavController().navigate(R.id.navigation_support)
        }

        binding.aboutLy.setOnClickListener {
            findNavController().navigate(R.id.navigation_about)
        }
        binding.logoutLy.setOnClickListener {
            openLogOut()
        }

        binding.passwordLy.setOnClickListener {
            findNavController().navigate(R.id.navigation_change_password)
        }

        binding.notificationLy.setOnClickListener {
            findNavController().navigate(R.id.navigation_notification)
        }

        setupViewModel()
        setUpObserver()





        return view
    }

    private fun openLogOut() {

        if (!isDialogShowing) {

            isDialogShowing = true

            val pethiioDialog =
                PethiioDialog(
                    requireContext(),
                    true,
                    logoutAlertTitle,
                    logoutAlertDone,
                    logoutAlertCancel
                )

            pethiioDialog.getPositiveButton().setOnClickListener {
                pethiioDialog.dissmiss()
                isDialogShowing = false

                PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn = false
                PreferenceHelper.SharedPreferencesManager.getInstance().accessToken = ""
                PreferenceHelper.SharedPreferencesManager.getInstance().topicUserId = 0
                unSubscribeToTopic()

                findNavController().navigate(R.id.action_global_navigation_welcome)
            }
            pethiioDialog.getNegativeButton().setOnClickListener {
                pethiioDialog.dissmiss()
                isDialogShowing = false
            }
            pethiioDialog.getPositiveButton().visibility = View.VISIBLE

        }
    }

    private fun unSubscribeToTopic() {
        Firebase.messaging.unsubscribeFromTopic("topic-user-" + PreferenceHelper.SharedPreferencesManager.getInstance().topicUserId)
            .addOnFailureListener {
//                it
            }
            .addOnCompleteListener { task ->
//                var msg = getString(com.google.android.gms.location.R.string.msg_subscribed)
                if (!task.isSuccessful) {
//                    msg = getString(com.google.android.gms.location.R.string.msg_subscribe_failed)
                }
//                Log.d(TAG, msg)
//                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(FAQViewModel::class.java)
        viewModel.fetchSettingsPageData()
    }

    private fun setUpObserver() {

        viewModel.getSettingsPageData().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressAvi.hide()
                }
                Status.ERROR -> {
                    binding.progressAvi.hide()
                }
                Status.SUCCESS -> {
                    val pageDataFields = it.data?.fields

                    binding.titleTv.text =
                        getLocalizedString(Constants.registerTitle, pageDataFields)
                    binding.supportTv.text =
                        getLocalizedString(Constants.supportTitle, pageDataFields)

                    binding.editProfileTv.text =
                        getLocalizedString(Constants.profileEditTitle, pageDataFields)
                    binding.passwordChangeTv.text =
                        getLocalizedString(Constants.changePasswordTitle, pageDataFields)
                    binding.notificationTv.text =
                        getLocalizedString(Constants.notificationTitle, pageDataFields)
                    binding.aboutTv.text =
                        getLocalizedString(Constants.aboutAppTitle, pageDataFields)
                    binding.logoutTv.text =
                        getLocalizedString(Constants.logoutTitle, pageDataFields)

                    logOutTitle = getLocalizedString(Constants.logoutTitle, pageDataFields)
                    logoutAlertDone = getLocalizedString(Constants.logoutAlertDone, pageDataFields)
                    logoutAlertCancel =
                        getLocalizedString(Constants.logoutAlertCancel, pageDataFields)
                    logoutAlertTitle =
                        getLocalizedString(Constants.logoutAlertTitle, pageDataFields)

                    binding.progressAvi.hide()
                }
            }


        })


    }


}