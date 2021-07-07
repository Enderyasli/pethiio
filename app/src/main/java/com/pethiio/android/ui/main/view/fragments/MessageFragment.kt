package com.pethiio.android.ui.main.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.pethiio.android.data.model.Message
import com.pethiio.android.databinding.FragmentMessageBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.adapter.MessageAdapter
import com.pethiio.android.ui.main.viewmodel.ChatViewModel
import com.pethiio.android.ui.main.viewmodel.FAQViewModel
import com.pethiio.android.utils.Constants


class MessageFragment : BaseFragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    override var bottomNavigationViewVisibility = View.VISIBLE
    override var dashboardClicked: Boolean = false
    private lateinit var viewModel: ChatViewModel



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        val view = binding.root


        setupViewModel()
        setUpObserver()

        return view
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
    }
    private fun setUpObserver() {

        viewModel.getChatPageData().observe(viewLifecycleOwner, {

            val pageDataFields = it.data?.fields

            binding.titleTv.text = getLocalizedString(Constants.registerTitle, pageDataFields)
            binding.noMsgTv.text =
                getLocalizedString(Constants.emptyMessageTitle, pageDataFields)



        })


    }

    private fun setupUI() {

        binding.messagesRv.layoutManager = LinearLayoutManager(requireContext())
        var messageAdapter = MessageAdapter(
            requireContext(),
            arrayListOf(
                Message(
                    name = "Lucky",
                    message = "Lorem Ipsum dolor sit amet",
                    url = "https://source.unsplash.com/Xq1ntWruZQI/600x800"
                ),
                Message(
                    name = "Lucky",
                    message = "Lorem Ipsum dolor sit amet",
                    url = "https://source.unsplash.com/NYyCqdBOKwc/600x800"
                )
            )
        )
        binding.messagesRv.adapter = messageAdapter
    }
}