package com.pethiio.android.ui.main.view.fragments.mainFragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.pethiio.android.databinding.FragmentShopBinding
import com.pethiio.android.ui.base.BaseFragment


class ShopFragment : BaseFragment() {

    private var _binding: FragmentShopBinding? = null

    private val binding get() = _binding!!


    override var bottomNavigationViewVisibility = View.GONE
    override var dashboardClicked: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentShopBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        return view

    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun setUpViews() {
        super.setUpViews()


        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true


        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { view?.loadUrl(it) }
                return true
            }
        }
        binding.webView.loadUrl("https://shop.pethiio.com")
    }


}