package com.pethiio.android.ui.main.view.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pethiio.android.R
import com.pethiio.android.data.model.login.LoginRequest
import com.pethiio.android.databinding.FragmentLoginBinding
import com.pethiio.android.ui.base.RegisterBaseFragment
import com.pethiio.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pethiio.android.utils.CommonMethods
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status


class LoginFragment : RegisterBaseFragment<RegisterBaseViewModel>() {

    override var bottomNavigationViewVisibility = View.GONE
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override var useSharedViewModel = true

    private var showPass: Boolean = false


    override fun setUpViews() {
        super.setUpViews()


        binding.passwordPlaceholderTv.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if(s.isEmpty())
                        binding.eye.visibility=View.GONE
                    else
                        binding.eye.visibility=View.VISIBLE
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


        viewModel.getFields().observe(this, {

            setPethiioResponseList(it)
            binding.signupTitle.text = getLocalizedString(Constants.registerTitle)
            binding.emailLayout.titleTv.text =
                getLocalizedString(Constants.registerEmailTitle)
            binding.emailLayout.placeholderTv.hint =
                getLocalizedString(Constants.registerEmailPlaceholder)
            binding.passwordTitleTv.text =
                getLocalizedString(Constants.registerPasswordTitle)
            binding.passwordPlaceholderTv.hint =
                getLocalizedString(Constants.registerPasswordPlaceholder)
            binding.forgotPassword.text =
                getLocalizedString(Constants.registerForgotPasswordButtonTitle)
            binding.loginBtn.text =
                getLocalizedString(Constants.registerLoginButtonTitle)

            binding.parentLayout.visibility = View.VISIBLE
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.loginBtn.setOnClickListener {

            var validEmail = getViewError(
                binding.emailLayout.placeholderTv,
                getLocalizedString(Constants.emailEmtpyError)
            )
            if (!validEmail)
                return@setOnClickListener

            var validPass = getViewError(
                binding.passwordPlaceholderTv,
                getLocalizedString(Constants.passwordEmtpyError)
            )
            if (!validPass)
                return@setOnClickListener

            postLogin(
                LoginRequest(
                    binding.emailLayout.placeholderTv.text.trim().toString(),
                    binding.passwordPlaceholderTv.text.trim().toString()
                )
            )

            viewModel.getPostLogin().observe(viewLifecycleOwner, {

                when (it.status) {
                    Status.SUCCESS -> {
                        activity?.runOnUiThread {
                            if (findNavController().currentDestination?.id == R.id.navigation_login)
                                findNavController().navigate(R.id.action_navigation_login_to_navigation_main)
                        }

                    }
                    Status.ERROR -> {
                        CommonMethods.onSNACK(binding.root,it.message.toString())
//                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                    }
                    Status.LOADING -> {
                    }
                }
            })


        }

        return view

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getViewModelClass() = RegisterBaseViewModel::class.java
}