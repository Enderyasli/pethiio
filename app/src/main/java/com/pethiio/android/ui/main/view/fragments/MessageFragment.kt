package com.pethiio.android.ui.main.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pethiio.android.PethiioApplication
import com.pethiio.android.R
import com.pethiio.android.data.model.member.MemberListResponse
import com.pethiio.android.data.model.socket.ChatSendMessage
import com.pethiio.android.data.socket.SocketIO
import com.pethiio.android.databinding.FragmentMessageBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.adapter.MessageAdapter
import com.pethiio.android.ui.main.view.customViews.MemberListSpinner
import com.pethiio.android.ui.main.viewmodel.ChatViewModel
import com.pethiio.android.utils.CommonFunctions
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.PreferenceHelper
import com.pethiio.android.utils.Status


class MessageFragment : BaseFragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    override var bottomNavigationViewVisibility = View.VISIBLE
    override var dashboardClicked: Boolean = false
    private lateinit var viewModel: ChatViewModel

    private var memberListResponse = emptyList<MemberListResponse>()
    private var isSelectedMemberFirstTime = true
    private var selectedMemberId = 0
    var memberId: Int = 0
    var roomId: Int = 0
    private var fromNotification = false


    private var adapter: MessageAdapter? = null


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


        binding.noMsgAnim.setAnimation("mesaj_yok.json")

        fromNotification = arguments?.getBoolean("fromNotification", false) == false

        if (fromNotification) {
            roomId = arguments?.getInt("roomId", 0)!!
            memberId = arguments?.getInt("memberId", 0)!!
        }



        setupViewModel()
        setUpObserver()

        PethiioApplication.getCurrentRoom()


        return view
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        isSelectedMemberFirstTime = true

        viewModel.fetchMemberList()


    }

    @SuppressLint("ResourceType")
    fun setMembeListSpinner(memberListResponse: List<MemberListResponse>) {
        val customAdapter = MemberListSpinner(requireContext(), memberListResponse)

        if (PreferenceHelper.SharedPreferencesManager.getInstance().selectedSpinnerId <= memberListResponse.size - 1)
            selectedMemberId =
                PreferenceHelper.SharedPreferencesManager.getInstance().selectedSpinnerId
        if (isSelectedMemberFirstTime)
            with(binding.memberlistSpinner)
            {
                id = 1
                adapter = customAdapter
                onItemSelectedListener = this@MessageFragment
                gravity = Gravity.CENTER
                if (memberListResponse.isNotEmpty() && isSelectedMemberFirstTime) {
                    binding.memberlistSpinner.setSelection(selectedMemberId)
                    isSelectedMemberFirstTime = false
                }
            }

    }

    private fun setUpObserver() {

        viewModel.getMemberList().observe(viewLifecycleOwner, {
            CommonFunctions.checkLogin(it.errorCode, findNavController())

            when (it.status) {
                Status.LOADING -> {
//                    binding.progressBar.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    if (it.data != null)
                        memberListResponse = it.data
                    setMembeListSpinner(memberListResponse)
//                    setMemberList(memberListResponse)
//                    binding.progressBar.visibility = View.GONE
                }
                Status.ERROR -> {
                    CommonFunctions.checkLogin(it.errorCode, findNavController())

                }
            }

        })


        viewModel.getChatListPageData().observe(viewLifecycleOwner, {

            val pageDataFields = it.data?.fields

            binding.titleTv.text = getLocalizedString(Constants.registerTitle, pageDataFields)
            binding.noMsgTv.text =
                getLocalizedString(Constants.emptyMessageTitle, pageDataFields)

        })

        viewModel.getChatList().observe(viewLifecycleOwner, { it ->

            when (it.status) {
                Status.SUCCESS -> {

                    adapter = it.data?.let { it1 ->
                        MessageAdapter(
                            requireContext(), findNavController(),
                            it1, memberId
                        )
                    }

                    it.data?.forEach {
                        if (fromNotification)
                            if (it.id == roomId) {
                                val bundle =
                                    bundleOf(
                                        "roomId" to roomId,
                                        "memberId" to memberId,
                                        "memberName" to it.name
                                    )
                                findNavController().navigate(R.id.navigation_chat, bundle)
                            }
                    }
                    binding.messagesRv.layoutManager = LinearLayoutManager(requireContext())
                    binding.messagesRv.adapter = adapter

                    if (it.data?.isEmpty() == true)
                        binding.noMsjLayout.visibility = View.VISIBLE
                    else
                        binding.noMsjLayout.visibility = View.GONE

                    binding.progressBar.visibility = View.GONE
                }

                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                Status.ERROR -> {
                }
            }


        })


    }

    private fun setupUI() {

//        binding.messagesRv.layoutManager = LinearLayoutManager(requireContext())
//        var messageAdapter = MessageAdapter(
//            requireContext(),
//            arrayListOf(
//                Message(
//                    name = "Lucky",
//                    message = "Lorem Ipsum dolor sit amet",
//                    url = "https://source.unsplash.com/Xq1ntWruZQI/600x800"
//                ),
//                Message(
//                    name = "Lucky",
//                    message = "Lorem Ipsum dolor sit amet",
//                    url = "https://source.unsplash.com/NYyCqdBOKwc/600x800"
//                )
//            )
//        )
//        binding.messagesRv.adapter = messageAdapter
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when (parent?.id) {
            1 -> {
                memberListResponse[position].id.let {
                    viewModel.fetchChatList(it)
                    memberId = it
                    PreferenceHelper.SharedPreferencesManager.getInstance().memberId = it
                    selectedMemberId = position
                    PreferenceHelper.SharedPreferencesManager.getInstance().selectedSpinnerId =
                        selectedMemberId

                }

            }

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}