package com.pethiio.android.ui.main.view.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pethiio.android.R
import com.pethiio.android.data.model.resetPass.PinVerificationRequest
import com.pethiio.android.data.model.resetPass.ResetPassDemand
import com.pethiio.android.databinding.FragmentPinVerifiedBinding
import com.pethiio.android.databinding.FragmentResetPassBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.viewmodel.PasswordViewModel
import com.pethiio.android.utils.CommonMethods
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status


class PinVerifiedFragment : BaseFragment() {

    private var _binding: FragmentPinVerifiedBinding? = null

    override var bottomNavigationViewVisibility = View.GONE
    private lateinit var viewModel: PasswordViewModel

    private var fromLogin: Boolean? = false
    private var referenceToken: String? = ""
    private var emailVerificationToken: String? = ""

    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPinVerifiedBinding.inflate(inflater, container, false)
        val view = binding.root


        fromLogin = arguments?.getBoolean("fromLogin", false)
        referenceToken = arguments?.getString("referenceToken", "")
        emailVerificationToken = arguments?.getString("emailVerificationToken", "")


        setupViewModel()
        setUpObserver()

        binding.pinPlaceholderTv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length >= 6) {

                    binding.sendBtn.isEnabled = true
                    binding.sendBtn.setBackgroundResource(R.drawable.orange_button_bg)
                    binding.sendBtn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )

                } else {

                    binding.sendBtn.isEnabled = false
                    binding.sendBtn.setBackgroundResource(R.drawable.disabled_button_bg)
                    binding.sendBtn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.disabled_button_color
                        )
                    )

                }

            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.sendBtn.setOnClickListener {
//            // TODO: 10.07.2021 sil
//            if (findNavController().currentDestination?.id == R.id.navigation_pin_verified)
//                findNavController().navigate(
//                    R.id.action_navigation_pin_verified_to_navigation_reset_password_request
//                )

            if (fromLogin == false) {
                referenceToken?.let { it1 ->
                    PinVerificationRequest(
                        binding.pinPlaceholderTv.text.trim().toString(),
                        it1
                    )
                }?.let { it2 ->
                    viewModel.postPostPinVerification(
                        it2
                    )
                }
            } else {

                emailVerificationToken?.let { it1 ->
                    PinVerificationRequest(
                        binding.pinPlaceholderTv.text.trim().toString(),
                        it1
                    )
                }?.let { it2 ->
                    viewModel.postEmailVerification(
                        it2
                    )
                }


            }

        }

        viewModel.getEmailVerificationResponse().observe(viewLifecycleOwner, {

            when (it.status) {
                Status.LOADING -> {
                    binding.progressAvi.show()
                }
                Status.SUCCESS -> {

                    val bundle = bundleOf(
                        "referenceToken" to referenceToken,
                        "pinValue" to binding.pinPlaceholderTv.text.trim().toString()
                    )

                    if (findNavController().currentDestination?.id == R.id.navigation_pin_verified)
                        findNavController().navigate(
                            R.id.action_navigation_pin_verified_to_navigation_register_detail,
                            bundle
                        )
                    binding.progressAvi.hide()
                }
                Status.ERROR -> {
                    CommonMethods.onSNACK(binding.root, it.message.toString())
                    binding.progressAvi.hide()

                }
            }
        })

        viewModel.getPinVerificationResponse().observe(viewLifecycleOwner, {

            when (it.status) {
                Status.LOADING -> {
                    binding.progressAvi.show()
                }
                Status.SUCCESS -> {

                    val bundle = bundleOf(
                        "referenceToken" to referenceToken,
                        "pinValue" to binding.pinPlaceholderTv.text.trim().toString()
                    )

                    if (findNavController().currentDestination?.id == R.id.navigation_pin_verified)
                        findNavController().navigate(
                            R.id.action_navigation_pin_verified_to_navigation_reset_password_request,
                            bundle
                        )
                    binding.progressAvi.hide()

                }
                Status.ERROR -> {
                    binding.progressAvi.hide()
                    CommonMethods.onSNACK(binding.root, it.message.toString())
                }
            }
        })

        return view
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(PasswordViewModel::class.java)
        viewModel.fetchPinVerifiedPageData()
    }

    private fun setUpObserver() {


        viewModel.getPinVerifiedPageData().observe(viewLifecycleOwner, {

            when (it.status) {
                Status.LOADING -> {
                    binding.progressAvi.show()
                }

                Status.ERROR -> {
                    binding.progressAvi.hide()
                    CommonMethods.onSNACK(binding.root, it.message.toString())
                }

                Status.SUCCESS -> {

                    val pageDataFields = it.data?.fields

                    binding.pinTitle.text =
                        getLocalizedString(Constants.registerTitle, pageDataFields)
                    if (fromLogin == true) {
                        binding.pinDescription.text =
                            getLocalizedString(Constants.subTitleForEmail, pageDataFields)
                    } else {
                        binding.pinDescription.text =
                            getLocalizedString(Constants.registerSubTitle, pageDataFields)
                    }


                    binding.pinPlaceholderTv.hint =
                        getLocalizedString(Constants.pinPlaceholder, pageDataFields)


                    binding.sendBtn.text =
                        getLocalizedString(Constants.nextButtonTitle, pageDataFields)

                    binding.progressAvi.hide()


                }
            }


        })


    }


}