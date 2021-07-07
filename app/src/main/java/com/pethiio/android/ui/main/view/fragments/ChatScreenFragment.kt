package com.pethiio.android.ui.main.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.pethiio.android.R
import com.pethiio.android.databinding.FragmentChatScreenBinding
import com.pethiio.android.databinding.FragmentMessageBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.viewmodel.ChatViewModel


class ChatScreenFragment : BaseFragment() {

    private var _binding: FragmentChatScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChatViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatScreenBinding.inflate(inflater, container, false)
        val view = binding.root


        setupViewModel()
        setUpObserver()


        return view
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)

    }

    private fun setUpObserver() {

        viewModel.fetchChatPageData()
        viewModel.getChatPageData().observe(viewLifecycleOwner, {

        })

    }


}