package com.pethiio.android.ui.main.view.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.pethiio.android.R
import com.pethiio.android.databinding.FragmentWelcomeBinding
import com.pethiio.android.ui.base.RegisterBaseFragment
import com.pethiio.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pethiio.android.utils.PreferenceHelper
import com.pethiio.android.utils.Status


class WelcomeFragment : RegisterBaseFragment<RegisterBaseViewModel>() {

    override var bottomNavigationViewVisibility = View.GONE
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!


    override var useSharedViewModel = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        val view = binding.root

//        PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn = false // TODO: 5.07.2021 sil
        //Check Onboarding
        if (PreferenceHelper.SharedPreferencesManager.getInstance().isFirstDownload == true) {
            if (findNavController().currentDestination?.id == R.id.navigation_welcome)
                findNavController().navigate(R.id.action_navigation_welcome_to_navigation_onboarding)

        }
        else if (PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn == false) {// TODO: 5.07.2021 true yap   //Check Logged in
            if (findNavController().currentDestination?.id == R.id.navigation_welcome)
                findNavController().navigate(R.id.action_navigation_start_to_navigation_main)
        }

        binding.signupBtn.setOnClickListener {
            fetchRegister()

            viewModel.register.observe(viewLifecycleOwner, {
                when (it.status) {
                    Status.SUCCESS -> {
                        activity?.runOnUiThread {
                            if (findNavController().currentDestination?.id == R.id.navigation_welcome)
                                findNavController().navigate(R.id.action_navigation_welcome_to_navigation_signup)
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
        binding.loginBtn.setOnClickListener {
            fetchLogin()

            viewModel.getLoginPageData().observe(viewLifecycleOwner, {
                when (it.status) {
                    Status.SUCCESS -> {
                        activity?.runOnUiThread {
                            if (findNavController().currentDestination?.id == R.id.navigation_welcome)
                                findNavController().navigate(R.id.action_navigation_welcome_to_navigation_login)
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

        return view

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getViewModelClass() = RegisterBaseViewModel::class.java


}