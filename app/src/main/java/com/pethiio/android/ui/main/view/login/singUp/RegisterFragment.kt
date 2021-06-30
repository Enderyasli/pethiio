package com.pethiio.android.ui.main.view.login.singUp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.pethiio.android.R
import com.pethiio.android.data.model.signup.Register
import com.pethiio.android.databinding.FragmentRegisterBinding
import com.pethiio.android.ui.base.RegisterBaseFragment
import com.pethiio.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.PreferenceHelper
import com.pethiio.android.utils.Status


class RegisterFragment : RegisterBaseFragment<RegisterBaseViewModel>(),
    AdapterView.OnItemSelectedListener {

    override var bottomNavigationViewVisibility = View.GONE
    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!
    override var useSharedViewModel = true
    var isSelectedFirstTime = true

    var languageAdapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n", "ResourceType")
    override fun setUpViews() {
        super.setUpViews()

        viewModel.getRegisterFields().observe(this, {

            setPethiioResponseList(it)
            binding.signupTitle.text = getLocalizedString(Constants.registerTitle)
            binding.signupDescription.text = getLocalizedString(Constants.registerSubTitle)

            binding.nameLy.titleTv.text =
                getLocalizedSpan(Constants.registerNameTitle)
            binding.nameLy.placeholderTv.hint =
                getLocalizedString(Constants.registerNamePlaceholder)
            binding.surnameLy.titleTv.text =
                getLocalizedSpan(Constants.registerSurnameTitle)
            binding.surnameLy.placeholderTv.hint =
                getLocalizedString(Constants.registerSurnamePlaceholder)
            binding.emailTitleTv.text =
                getLocalizedSpan(Constants.registerEmailTitle)
            binding.emailPlaceholderTv.hint =
                getLocalizedString(Constants.registerEmailPlaceholder)
            binding.emailPlaceholderTv.setText("@gmail.com")
            binding.passwordTitleTv.text =
                getLocalizedSpan(Constants.registerPasswordTitle)
            binding.passwordDetailTv.text =
                getLocalizedString(Constants.registerPasswordDetail)
            binding.passwordPlaceholderTv.hint =
                getLocalizedString(Constants.registerPasswordPlaceholder)
            binding.signupBtn.text =
                getLocalizedString(Constants.registerButtonTitle)
            binding.termsLinkTitleTv.text =
                getLocalizedString(Constants.registerTermsLinkTitle) + " "
            binding.termsAndTitleTv.text =
                getLocalizedString(Constants.registerTermsAndTitle) + " "
            binding.privacyTitleTv.text =
                getLocalizedString(Constants.registerPrivacyLinkTitle) + " "
            binding.privacyApproveTitleTv.text =
                getLocalizedString(Constants.registerPrivacyApproveTitle)

            binding.termsLinkTitleTv.setOnClickListener {
                val uri: Uri =
                    Uri.parse(getLocalizedString(Constants.registerTermsLink))
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
            binding.privacyTitleTv.setOnClickListener {
                val uri: Uri =
                    Uri.parse(getLocalizedString(Constants.registerPrivacyLink))
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }


        })
//        binding.spinner.prompt = PreferenceHelper.SharedPreferencesManager.getInstance().appLanguage

        viewModel.getRegisterLookUps().observe(viewLifecycleOwner, { it ->

            setLookUps(it)

            val language = getLookUps(Constants.lookUpLanguage)
            val upperLanguage = language.map { it.uppercase() }
            var languageString =
                PreferenceHelper.SharedPreferencesManager.getInstance().appLanguage


            if (!PreferenceHelper.SharedPreferencesManager.getInstance().isApplanguageSelected) {

                if (!upperLanguage.contains(languageString.uppercase()))
                    languageString = "EN"

                PreferenceHelper.SharedPreferencesManager.getInstance().isApplanguageSelected = true
            }
            languageAdapter =
                ArrayAdapter(requireContext(), R.layout.spinner_item_register, upperLanguage)
            languageAdapter?.setDropDownViewResource(R.layout.spinner_item_register)

            if (isSelectedFirstTime)
                with(binding.spinner)
                {
                    id = 1
                    adapter = languageAdapter
                    onItemSelectedListener = this@RegisterFragment
                    gravity = Gravity.CENTER
                    setSelection(upperLanguage.indexOf(languageString.uppercase()))
                    isSelectedFirstTime = false
                }

        })

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.signupBtn.setOnClickListener {

            var valid = getViewError(binding.emailPlaceholderTv, "Email cannot be empty")
            valid = getViewError(binding.nameLy.placeholderTv, "Name cannot be empty")
            valid = getViewError(binding.surnameLy.placeholderTv, "Surname cannot be empty")
            valid = getViewError(binding.passwordPlaceholderTv, "Password cannot be empty")

            if (valid) {
                postRegister(
                    Register(
                        binding.emailPlaceholderTv.text.toString().trim(),
                        binding.nameLy.placeholderTv.text.toString().trim(),
                        binding.surnameLy.placeholderTv.text.toString().trim(),
                        binding.passwordPlaceholderTv.text.toString().trim(),
                    )
                )
                viewModel.postRegister.observe(viewLifecycleOwner, {
                    when (it.status) {
                        Status.SUCCESS -> {
                            activity?.runOnUiThread {
                                fetchRegisterDetail()
                                if (findNavController().currentDestination?.id == R.id.navigation_register)
                                    findNavController().navigate(R.id.action_navigation_register_to_navigation_register_detail)

                            }

                        }
                        Status.ERROR -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                        }
                        Status.LOADING -> {
                        }
                    }
                })


            }

        }

        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getViewModelClass() = RegisterBaseViewModel::class.java

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            1 -> {

                PreferenceHelper.SharedPreferencesManager.getInstance().appLanguage =
                    binding.spinner.selectedItem.toString().lowercase()
                fetchRegister()

            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

}