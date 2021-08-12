package com.pethiio.android.ui.main.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.pethiio.android.R
import com.pethiio.android.databinding.FragmentWelcomeBinding
import com.pethiio.android.ui.base.RegisterBaseFragment
import com.pethiio.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pethiio.android.utils.CommonMethods
import com.pethiio.android.utils.PreferenceHelper
import com.pethiio.android.utils.Status


class WelcomeFragment : RegisterBaseFragment<RegisterBaseViewModel>() {

    override var bottomNavigationViewVisibility = View.GONE
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private var signInBtn: SignInButton? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    val RC_SIGN_IN = 100
    val TAG: String = WelcomeFragment::class.java.getCanonicalName()


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

        } else if (PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn == true) {// TODO: 5.07.2021 true yap   //Check Logged in
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
                        CommonMethods.onSNACK(binding.root, it.message.toString())

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
                        CommonMethods.onSNACK(binding.root, it.message.toString())

                    }
                    Status.LOADING -> {
                    }
                }
            })


        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
        signInBtn = view.findViewById(R.id.sign_in_btn)
        signInBtn!!.setSize(SignInButton.SIZE_STANDARD)
        signInBtn!!.setOnClickListener { signIn() }


        return view

    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(
            signInIntent,
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            updateUI(account)
        } catch (e: ApiException) {
            Log.w(
                TAG,
                "signInResult:failed code=" + e.statusCode
            )
            Toast.makeText(activity, "signInResult:failed code=" + e.statusCode, Toast.LENGTH_SHORT)
                .show()
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account == null) {
            return
        }
        val idToken = account.idToken

        Toast.makeText(activity, idToken, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getViewModelClass() = RegisterBaseViewModel::class.java


}