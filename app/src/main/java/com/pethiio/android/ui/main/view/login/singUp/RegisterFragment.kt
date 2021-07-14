package com.pethiio.android.ui.main.view.login.singUp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.*
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.pethiio.android.R
import com.pethiio.android.data.model.signup.Register
import com.pethiio.android.databinding.FragmentRegisterBinding
import com.pethiio.android.ui.base.RegisterBaseFragment
import com.pethiio.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pethiio.android.utils.*
import java.util.regex.Pattern


class RegisterFragment : RegisterBaseFragment<RegisterBaseViewModel>(),
    AdapterView.OnItemSelectedListener {

    override var bottomNavigationViewVisibility = View.GONE
    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!
    override var useSharedViewModel = true
    private var isSelectedFirstTime = true

    private var languageAdapter: ArrayAdapter<String>? = null
    private var showPass: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n", "ResourceType")
    override fun setUpViews() {
        super.setUpViews()

        isSelectedFirstTime = true


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

        viewModel.getRegisterFields().observe(this, {

            setPethiioResponseList(it)
            binding.signupTitle.text = getLocalizedString(Constants.registerTitle)
            binding.signupDescription.text = getLocalizedString(Constants.registerSubTitle)

            binding.nameLy.titleTv.text =
                getLocalizedSpan(Constants.registerNameTitle)
            binding.nameLy.placeholderTv.hint =
                getLocalizedString(Constants.registerNamePlaceholder)
            binding.surnameTitleTv.text =
                getLocalizedSpan(Constants.registerSurnameTitle)
            binding.surnamePlaceholderTv.hint =
                getLocalizedString(Constants.registerSurnamePlaceholder)
            binding.emailTitleTv.text =
                getLocalizedSpan(Constants.registerEmailTitle)
            binding.emailPlaceholderTv.hint =
                getLocalizedString(Constants.registerEmailPlaceholder)
            binding.passwordTitleTv.text =
                getLocalizedSpan(Constants.registerPasswordTitle)
            binding.passwordDetailTv.text =
                getLocalizedString(Constants.registerPasswordDetail)
            binding.passwordPlaceholderTv.hint =
                getLocalizedString(Constants.registerPasswordPlaceholder)
            binding.signupBtn.text =
                getLocalizedString(Constants.registerButtonTitle)

            val termsLink = getLocalizedString(Constants.registerTermsLinkTitle) + " "
            val termsAndTitle = getLocalizedString(Constants.registerTermsAndTitle) + " "

            val privacyTitle = getLocalizedString(Constants.registerPrivacyLinkTitle) + " "
            val privacyApprove = getLocalizedString(Constants.registerPrivacyApproveTitle) + " "

            val termsPrivacyText = "$termsLink $termsAndTitle $privacyTitle $privacyApprove"

            val spanned = SpannableString(termsPrivacyText)


            val termsSpanOnclick = object : ClickableSpan() {
                override fun onClick(view: View) {
                    val uri: Uri =
                        Uri.parse(getLocalizedString(Constants.registerTermsLink))
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    try {
                        startActivity(intent)
                    } catch (e: Exception) {
                    }

                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = ContextCompat.getColor(requireContext(), R.color.orangeButton)
                    ds.isUnderlineText = false
                }
            }
            val privacySpanOnclick = object : ClickableSpan() {
                override fun onClick(view: View) {
                    val uri: Uri =
                        Uri.parse(getLocalizedString(Constants.registerPrivacyLink))
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    try {
                        startActivity(intent)
                    } catch (e: Exception) {
                    }

                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = ContextCompat.getColor(requireContext(), R.color.orangeButton)
                    ds.isUnderlineText = false
                }
            }

            spanned.setSpan(
                termsSpanOnclick,
                0,
                termsLink.length - 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            spanned.setSpan(
                privacySpanOnclick,
                termsLink.length + termsAndTitle.length,
                termsPrivacyText.length - privacyApprove.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );


            binding.termsLinkTitleTv.text =
                spanned
            binding.termsLinkTitleTv.movementMethod = LinkMovementMethod.getInstance()


//            binding.termsLinkTitleTv.setOnClickListener {
//                val uri: Uri =
//                    Uri.parse(getLocalizedString(Constants.registerTermsLink))
//                val intent = Intent(Intent.ACTION_VIEW, uri)
//                startActivity(intent)
//            }
//            binding.privacyTitleTv.setOnClickListener {
//                val uri: Uri =
//                    Uri.parse(getLocalizedString(Constants.registerPrivacyLink))
//                val intent = Intent(Intent.ACTION_VIEW, uri)
//                startActivity(intent)
//            }


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

        binding.registerCb.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                binding.signupBtn.isEnabled = true
                binding.signupBtn.setBackgroundResource(R.drawable.orange_button_bg)
                binding.signupBtn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
            } else {
                binding.signupBtn.isEnabled = false
                binding.signupBtn.setBackgroundResource(R.drawable.disabled_button_bg)
                binding.signupBtn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.disabled_button_color
                    )
                )
            }
        }

        binding.signupBtn.setOnClickListener {

            var validName = getViewError(
                binding.nameLy.placeholderTv,
                getLocalizedString(Constants.nameEmptyEror)
            )
            var validSur = getViewError(
                binding.surnamePlaceholderTv,
                getLocalizedString(Constants.surnameEmtpyError)
            )
            var validEmail = getViewError(
                binding.emailPlaceholderTv,
                getLocalizedString(Constants.emailEmtpyError)
            )
            var validPass = getViewError(
                binding.passwordPlaceholderTv,
                getLocalizedString(Constants.passwordEmtpyError)
            )

            if (validName && validSur && validEmail && validPass) {

                val upperCasePattern = Pattern.compile("[A-Z ]")

                val password = binding.passwordPlaceholderTv.text.toString().trim()


                val validRegex: Boolean =
                    upperCasePattern.matcher(password)
                        .find()


                if (validRegex && password.length >= 8) {
                    postRegister(
                        Register(
                            binding.emailPlaceholderTv.text.toString().trim(),
                            binding.nameLy.placeholderTv.text.toString().trim(),
                            binding.surnamePlaceholderTv.text.toString().trim(),
                            binding.passwordPlaceholderTv.text.toString().trim(),
                        )
                    )
                } else {
                    binding.passwordPlaceholderTv.error =
                        getLocalizedString(Constants.registerPasswordDetail)
                    binding.passwordPlaceholderTv.requestFocus()
                }

                viewModel.postRegister.observe(viewLifecycleOwner, {
                    when (it.status) {
                        Status.SUCCESS -> {
                            activity?.runOnUiThread {

                                fetchRegisterDetail()

                                binding.progressBar.visibility = View.GONE

                                if (TextUtils.isEmpty(it.data?.emailVerificationToken)) {
                                    val bundle =
                                        bundleOf(
                                            "emailVerificationToken" to it.data?.emailVerificationToken,
                                            "fromLogin" to true
                                        )
                                    if (findNavController().currentDestination?.id == R.id.navigation_register)
                                        findNavController().navigate(
                                            R.id.action_navigation_register_to_navigation_pin_verified,
                                            bundle
                                        )

                                } else {
                                    if (findNavController().currentDestination?.id == R.id.navigation_register)
                                        findNavController().navigate(R.id.action_navigation_register_to_navigation_register_detail)

                                }


                            }

                        }
                        Status.ERROR -> {
                            binding.progressBar.visibility = View.GONE

                            CommonMethods.onSNACK(binding.root, it.message.toString())

                        }
                        Status.LOADING -> {

                            binding.progressBar.visibility = View.VISIBLE
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