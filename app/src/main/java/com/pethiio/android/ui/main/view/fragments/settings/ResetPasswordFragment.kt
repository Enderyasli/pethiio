package com.pethiio.android.ui.main.view.fragments.settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pethiio.android.R
import com.pethiio.android.data.model.login.ChangePassRequest
import com.pethiio.android.data.model.resetPass.ResetPasswordRequest
import com.pethiio.android.databinding.FragmentResetPasswordBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.viewmodel.PasswordViewModel
import com.pethiio.android.utils.CommonMethods
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status
import java.util.regex.Pattern


class ResetPasswordFragment : BaseFragment() {

    private var _binding: FragmentResetPasswordBinding? = null

    override var bottomNavigationViewVisibility = View.GONE
    private lateinit var viewModel: PasswordViewModel

    private var referenceToken = ""
    private var pinValue = ""
    private val binding get() = _binding!!
    private var showPass: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        val view = binding.root

        referenceToken = arguments?.getString("referenceToken", "")!!
        pinValue = arguments?.getString("pinValue", "")!!

        setupViewModel()
        setUpObserver()

        binding.passwordPlaceholderTv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.isEmpty())
                        binding.eye.visibility = View.GONE
                    else
                        binding.eye.visibility = View.VISIBLE
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.eye.setOnClickListener {

            if (!showPass) {
                binding.passwordPlaceholderTv.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                showPass = true
            } else {
                binding.passwordPlaceholderTv.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                showPass = false
            }

        }
        return view
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(PasswordViewModel::class.java)
        viewModel.fetchResetPasswordLastPageData()
    }

    private fun setUpObserver() {


        viewModel.getPinResetPasswordPageData().observe(viewLifecycleOwner, {

            val pageDataFields = it.data?.fields

            binding.resetPassTitle.text =
                getLocalizedString(Constants.registerTitle, pageDataFields)
            binding.resetPassDescription.text =
                getLocalizedString(Constants.registerSubTitle, pageDataFields)
            binding.passwordTitleTv.text =
                getLocalizedSpan(Constants.passwordTitle, pageDataFields)
            binding.passwordDetailTv.text =
                getLocalizedString(Constants.registerPasswordDetail, pageDataFields)
            binding.passwordPlaceholderTv.hint =
                getLocalizedString(Constants.passwordPlaceholder, pageDataFields)
            binding.newPasswordAgainTitleTv.text =
                getLocalizedSpan(Constants.passwordTitle, pageDataFields)
            binding.newPasswordAgainPlaceholder.hint =
                getLocalizedString(Constants.passwordPlaceholder, pageDataFields)
            binding.sendBtn.text =
                getLocalizedString(Constants.nextButtonTitle, pageDataFields)

            binding.sendBtn.setOnClickListener {
                var validPass = getViewError(
                    binding.passwordPlaceholderTv,
                    getLocalizedString(Constants.passwordEmtpyError, pageDataFields)
                )
                if (!validPass)
                    return@setOnClickListener

                var validNewAgainPass = getViewError(
                    binding.newPasswordAgainPlaceholder,
                    getLocalizedString(Constants.passwordEmtpyError, pageDataFields)
                )
                if (!validNewAgainPass)
                    return@setOnClickListener

                val upperCasePattern = Pattern.compile("[A-Z ]")

                val password = binding.passwordPlaceholderTv.text.toString().trim()

                val validRegex: Boolean =
                    upperCasePattern.matcher(password)
                        .find()
                if (validRegex && password.length >= 8) {

                    if (binding.passwordDetailTv.text.trim()
                            .toString() == binding.newPasswordAgainPlaceholder.text.trim()
                            .toString()
                    )
                        viewModel.postResetPassword(
                            ResetPasswordRequest(
                                binding.passwordPlaceholderTv.text.trim().toString(),
                                pinValue,
                                referenceToken
                            )
                        )
                    else {
                        binding.newPasswordAgainPlaceholder.error =
                            getLocalizedString(Constants.newPasswordEmtpyError, pageDataFields)

                        binding.newPasswordAgainPlaceholder.requestFocus()
                    }


                }
                else {
                    binding.passwordPlaceholderTv.error =
                        getLocalizedString(Constants.passwordPlaceholder, pageDataFields)

                    binding.passwordPlaceholderTv.requestFocus()
                }

                viewModel.getResetPasswordResponse().observe(viewLifecycleOwner, { it1 ->

                    when (it1.status) {
                        Status.SUCCESS -> {

                            if (findNavController().currentDestination?.id == R.id.navigation_reset_password_request)
                                findNavController().navigate(
                                    R.id.action_navigation_reset_password_request_to_navigation_welcome
                                )
                        }
                        Status.ERROR -> {
                            CommonMethods.onSNACK(binding.root, it1.message.toString())
                        }
                    }

                })


            }


        })


    }


}