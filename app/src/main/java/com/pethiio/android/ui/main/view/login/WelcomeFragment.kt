package com.pethiio.android.ui.main.view.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.pethiio.android.R
import com.pethiio.android.databinding.FragmentWelcomeBinding
import com.pethiio.android.ui.base.RegisterBaseFragment
import com.pethiio.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pethiio.android.utils.PreferenceHelper


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

        if (PreferenceHelper.SharedPreferencesManager.getInstance().isFirstDownload == true)
            findNavController().navigate(R.id.action_navigation_welcome_to_navigation_onboarding)

        binding.signupBtn.setOnClickListener {
            fetchRegister()
            findNavController().navigate(R.id.action_navigation_welcome_to_navigation_signup)
        }
        binding.loginBtn.setOnClickListener {
            fetchLogin()
            findNavController().navigate(R.id.action_navigation_welcome_to_navigation_login)
        }

        return view

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getViewModelClass() = RegisterBaseViewModel::class.java


}