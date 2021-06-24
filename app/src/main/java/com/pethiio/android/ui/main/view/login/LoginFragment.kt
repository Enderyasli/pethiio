package com.pethiio.android.ui.main.view.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pethiio.android.R
import com.pethiio.android.databinding.FragmentLoginBinding
import com.pethiio.android.ui.base.RegisterBaseFragment
import com.pethiio.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pethiio.android.utils.Constants


class LoginFragment : RegisterBaseFragment<RegisterBaseViewModel>() {

    override var bottomNavigationViewVisibility = View.GONE
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override var useSharedViewModel = true


//    private lateinit var loginViewModel: LoginViewModel

    override fun setUpViews() {
        super.setUpViews()
        viewModel.getFields().observe(this, Observer {

            setPawtindResponseList(it)
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
            binding.parentLayout.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        })
    }
//    override fun observeData() {
//        super.observeData()
//        viewModel.getLogin().observe(this, Observer { it ->
//            when (it.status) {
//                Status.SUCCESS -> {
//                    it.data?.let { it ->
//
//                        binding.signupTitle.text = getLocaizedString(Constants.registerTitle)
//                        binding.emailLayout.titleTv.text =
//                            getLocaizedString(Constants.registerEmailTitle)
//                        binding.emailLayout.placeholderTv.hint =
//                            getLocaizedString(Constants.registerEmailPlaceholder)
//                        binding.passwordTitleTv.text =
//                            getLocaizedString(Constants.registerPasswordTitle)
//                        binding.passwordPlaceholderTv.hint =
//                            getLocaizedString(Constants.registerPasswordPlaceholder)
//                        binding.forgotPassword.text =
//                            getLocaizedString(Constants.registerForgotPasswordButtonTitle)
//                        binding.parentLayout.visibility = View.VISIBLE
//                        binding.progressBar.visibility = View.GONE
//
//
//
//                        Log.d("gelenresponse", it.toString())
//                    }
//                }
//                Status.LOADING -> {
//                    binding.progressBar.visibility = View.VISIBLE
//                }
//                Status.ERROR -> {
//                    //Handle Error
//                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
//                }
//            }
//        })
//
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setupViewModel()
//        setupObserver()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_login_to_navigation_main)
        }

        return view

    }

//    private fun setupViewModel() {
//        loginViewModel = ViewModelProviders.of(
//            this,
//            ViewModelFactory(ApiHelper(ApiServiceImpl()))
//        ).get(LoginViewModel::class.java)
//    }
//
//    private fun setupObserver() {
//        loginViewModel.getLogin().observe(this, Observer { it ->
//            when (it.status) {
//                Status.SUCCESS -> {
//                    it.data?.let { it ->
//
//
//                        val pawtindResponse: List<PawtindResponse> = it.fields
//                        var loginInfoMapper = LoginInfoMapper()
//
//                        pawtindResponse.forEach { pawtindResponse: PawtindResponse ->
//
//
//                            loginInfoMapper.javaClass.declaredFields.forEach {
//
//                                if (pawtindResponse.key == it.name) {
//                                    val field: Field =
//                                        LoginInfoMapper::class.java.getDeclaredField(it.name)
//                                    field.isAccessible = true
//                                    field.set(loginInfoMapper, pawtindResponse.value)
//                                }
//                            }
//
//
//                        }
//
//                        binding.signupTitle.text = loginInfoMapper.title
//                        binding.emailLayout.titleTv.text = loginInfoMapper.emailTitle
//                        binding.emailLayout.placeholderTv.hint = loginInfoMapper.emailPlaceholder
//                        binding.passwordTitleTv.text = loginInfoMapper.passwordTitle
//                        binding.passwordPlaceholderTv.hint = loginInfoMapper.title
//                        binding.forgotPassword.text = loginInfoMapper.forgotPasswordButtonTitle
//                        binding.parentLayout.visibility = View.VISIBLE
//                        binding.progressBar.visibility = View.GONE
//
//
//
//                        Log.d("gelenresponse", it.toString())
//                    }
//                }
//                Status.LOADING -> {
//                    binding.progressBar.visibility = View.VISIBLE
//                }
//                Status.ERROR -> {
//                    //Handle Error
//                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
//                }
//            }
//        })
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getViewModelClass() = RegisterBaseViewModel::class.java
}