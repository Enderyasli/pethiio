package com.pethiio.android.ui.main.view.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pethiio.android.R
import com.pethiio.android.data.model.login.ChangePassRequest
import com.pethiio.android.databinding.FragmentChangePasswordBinding
import com.pethiio.android.databinding.FragmentSettingsBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.viewmodel.FAQViewModel
import com.pethiio.android.utils.CommonMethods
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status
import java.util.regex.Pattern

class ChangePasswordFragment : BaseFragment() {


    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FAQViewModel
    override var bottomNavigationViewVisibility = View.GONE

    private var showPass: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        setupViewModel()
        setUpObserver()




        binding.currentPasswordPlaceholder.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.isEmpty())
                        binding.currentEye.visibility = View.GONE
                    else
                        binding.currentEye.visibility = View.VISIBLE
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        binding.newPasswordPlaceholder.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.isEmpty())
                        binding.newEye.visibility = View.GONE
                    else
                        binding.newEye.visibility = View.VISIBLE
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        binding.newPasswordAgainPlaceholder.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.isEmpty())
                        binding.newAgainEye.visibility = View.GONE
                    else
                        binding.newAgainEye.visibility = View.VISIBLE
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.currentEye.setOnClickListener {

            if (!showPass) {
                binding.currentPasswordPlaceholder.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                showPass = true
            } else {
                binding.currentPasswordPlaceholder.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                showPass = false
            }

        }
        binding.newEye.setOnClickListener {

            if (!showPass) {
                binding.newPasswordPlaceholder.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                showPass = true
            } else {
                binding.newPasswordPlaceholder.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                showPass = false
            }

        }
        binding.newAgainEye.setOnClickListener {

            if (!showPass) {
                binding.newPasswordAgainPlaceholder.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                showPass = true
            } else {
                binding.newPasswordAgainPlaceholder.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                showPass = false
            }

        }





        return view
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(FAQViewModel::class.java)
        viewModel.fetchChangePassPageData()
    }

    private fun setUpObserver() {
        viewModel.getChangePass().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {

                }
                Status.ERROR -> {
                    it.message?.let { it1 -> CommonMethods.onSNACK(binding.root, it1) }
                }
            }
        })

        viewModel.getChangePassPageData().observe(viewLifecycleOwner, {

            val pageDataFields = it.data?.fields

            binding.titleTv.text = getLocalizedString(Constants.registerTitle, pageDataFields)
            binding.descriptionTv.text =
                getLocalizedString(Constants.registerSubTitle, pageDataFields)
            binding.currentPasswordTitleTv.text =
                getLocalizedSpan(Constants.currentPasswordTitle, pageDataFields)
            binding.currentPasswordPlaceholder.hint =
                getLocalizedString(Constants.currentPasswordPlaceholder, pageDataFields)
            binding.newPasswordTitleTv.text =
                getLocalizedSpan(Constants.newPasswordTitle, pageDataFields)
            binding.newPasswordPlaceholder.hint =
                getLocalizedString(Constants.newPasswordPlaceholder, pageDataFields)
            binding.passwordDetailTv.text =
                getLocalizedString(Constants.newPasswordDetail, pageDataFields)
            binding.newPasswordAgainTitleTv.text =
                getLocalizedSpan(Constants.passwordTitle, pageDataFields)
            binding.newPasswordAgainPlaceholder.hint =
                getLocalizedString(Constants.newPasswordPlaceholder, pageDataFields)
            binding.changeBtn.text = getLocalizedString(Constants.changeButtonTitle, pageDataFields)


            binding.changeBtn.setOnClickListener {

                var validPass = getViewError(
                    binding.currentPasswordPlaceholder,
                    getLocalizedString(Constants.passwordEmtpyError, pageDataFields)
                )
                if (!validPass)
                    return@setOnClickListener

                var validNewPass = getViewError(
                    binding.newPasswordPlaceholder,
                    getLocalizedString(Constants.newPasswordEmtpyError, pageDataFields)
                )
                if (!validNewPass)
                    return@setOnClickListener
                var validNewAgainPass = getViewError(
                    binding.newPasswordAgainPlaceholder,
                    getLocalizedString(Constants.newPasswordEmtpyError, pageDataFields)
                )
                if (!validNewAgainPass)
                    return@setOnClickListener

                val upperCasePattern = Pattern.compile("[A-Z ]")

                val password = binding.newPasswordPlaceholder.text.toString().trim()

                val validRegex: Boolean =
                    upperCasePattern.matcher(password)
                        .find()

                if (validRegex && password.length >= 8) {

                    if (binding.newPasswordPlaceholder.text.trim()
                            .toString() == binding.newPasswordAgainPlaceholder.text.trim()
                            .toString()
                    )
                        viewModel.postChangePass(
                            ChangePassRequest(
                                binding.currentPasswordPlaceholder.text.trim().toString(),
                                binding.newPasswordPlaceholder.text.trim().toString()
                            )
                        )
                    else {
                        binding.newPasswordAgainPlaceholder.error =
                            getLocalizedString(Constants.passwordDoNotMatch, pageDataFields)

                        binding.newPasswordAgainPlaceholder.requestFocus()
                    }


                } else {
                    binding.newPasswordPlaceholder.error =
                        getLocalizedString(Constants.newPasswordEmtpyError, pageDataFields)

                    binding.newPasswordPlaceholder.requestFocus()
                }


            }

        })


    }


}