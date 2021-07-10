package com.pethiio.android.ui.main.view.fragments

import android.os.Bundle
import android.text.Editable
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

    private var referenceToken = ""
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

        referenceToken = arguments?.getString("referenceToken", "")!!


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
            viewModel.postPostPinVerification(
                PinVerificationRequest(
                    binding.pinPlaceholderTv.text.trim().toString(),
                    referenceToken
                )
            )
        }

        viewModel.getPinVerificationResponse().observe(viewLifecycleOwner, {

            when (it.status) {
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
                }
                Status.ERROR -> {
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

            val pageDataFields = it.data?.fields

            binding.pinTitle.text =
                getLocalizedString(Constants.registerTitle, pageDataFields)
            binding.pinDescription.text =
                getLocalizedString(Constants.registerSubTitle, pageDataFields)

            binding.pinPlaceholderTv.hint =
                getLocalizedString(Constants.pinPlaceholder, pageDataFields)


            binding.sendBtn.text =
                getLocalizedString(Constants.nextButtonTitle, pageDataFields)


        })


    }


}