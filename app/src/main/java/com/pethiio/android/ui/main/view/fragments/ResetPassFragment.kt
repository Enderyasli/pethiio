package com.pethiio.android.ui.main.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pethiio.android.R
import com.pethiio.android.data.model.resetPass.ResetPassDemand
import com.pethiio.android.databinding.FragmentPetAddBinding
import com.pethiio.android.databinding.FragmentResetPassBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.viewmodel.FAQViewModel
import com.pethiio.android.ui.main.viewmodel.PasswordViewModel
import com.pethiio.android.utils.CommonMethods
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status


class ResetPassFragment : BaseFragment() {

    override var bottomNavigationViewVisibility = View.GONE
    private var _binding: FragmentResetPassBinding? = null

    private lateinit var viewModel: PasswordViewModel


    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPassBinding.inflate(inflater, container, false)
        val view = binding.root

        setupViewModel()
        setUpObserver()


        return view
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(PasswordViewModel::class.java)
        viewModel.fetchResetPassDemandPageData()
    }

    private fun setUpObserver() {


        viewModel.getResetPassDemandPageData().observe(viewLifecycleOwner, {

            val pageDataFields = it.data?.fields

            binding.resetPassTitle.text =
                getLocalizedString(Constants.registerTitle, pageDataFields)
            binding.resetPassDescription.text =
                getLocalizedString(Constants.registerSubTitle, pageDataFields)

            binding.emailTitleTv.text =
                getLocalizedSpan(Constants.registerEmailTitle, pageDataFields)
            binding.emailPlaceholderTv.hint =
                getLocalizedString(Constants.registerEmailPlaceholder, pageDataFields)

            binding.sendBtn.text =
                getLocalizedString(Constants.sendButtonTitle, pageDataFields)

            binding.sendBtn.setOnClickListener {

                viewModel.getPostResetDemand().observe(viewLifecycleOwner, {
                    when (it.status) {
                        Status.SUCCESS -> {

                            val bundle = bundleOf("referenceToken" to it.data?.referenceToken)

                            if (findNavController().currentDestination?.id == R.id.navigation_reset_password)
                                findNavController().navigate(
                                    R.id.action_navigation_reset_password_to_navigation_pin_verified,
                                    bundle
                                )


                        }
                        Status.ERROR -> {
                            CommonMethods.onSNACK(binding.root, it.message.toString())

                        }
                    }
                })
                
                val validEmail = getViewError(
                    binding.emailPlaceholderTv,
                    getLocalizedString(Constants.emailEmtpyError, pageDataFields)
                )

                if (!validEmail)
                    return@setOnClickListener

                viewModel.postResetDemandPass(
                    ResetPassDemand(
                        binding.emailPlaceholderTv.text.trim().toString()

                    )
                )

//                if (findNavController().currentDestination?.id == R.id.navigation_reset_password)
//                    findNavController().navigate(R.id.action_navigation_reset_password_to_navigation_pin_verified)
            }

        })


    }


}