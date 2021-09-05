package com.pethiio.android.ui.main.view.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pethiio.android.PethiioApplication
import com.pethiio.android.R
import com.pethiio.android.data.EventBus.ChatEvent
import com.pethiio.android.data.model.chat.ChatUpdateStateRequest
import com.pethiio.android.data.model.socket.ChatSendMessage
import com.pethiio.android.data.socket.SocketIO
import com.pethiio.android.databinding.FragmentChatScreenBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.adapter.ChatAdapter
import com.pethiio.android.ui.main.viewmodel.ChatViewModel
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class ChatScreenFragment : BaseFragment() {

    private var _binding: FragmentChatScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChatViewModel
    override var bottomNavigationViewVisibility = View.GONE
    private var petAvatar: String = ""
    private var roomId: Int = 0
    private var petId: Int = 0
    var memberId: Int = 0
    var messageTitle: String = ""
    private var adapter: ChatAdapter? = null
    var lastMessageId: Int = 0

    private val socketIO = SocketIO()
    var socketConnected: Boolean = false


    override fun onDestroy() {
        //unregister event bus
        EventBus.getDefault().unregister(this)
        PethiioApplication.setCurrentRoom(0)
        socketIO.disconnect(roomId)

        viewModel.updateChatState(
            ChatUpdateStateRequest(
                lastMessageId,
                roomId,
                senderMemberId = memberId,
                Constants.chatSeen
            )
        )
        socketConnected=false
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ChatEvent) {

        lastMessageId = event.id
        adapter?.addMessage(event)
        adapter?.listSize?.let { binding.recyclerView.smoothScrollToPosition(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onDestroy()
                    findNavController().navigate(R.id.navigation_message)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        _binding = FragmentChatScreenBinding.inflate(inflater, container, false)
        val view = binding.root

        petAvatar = arguments?.getString("petAvatar", "")!!
        roomId = arguments?.getInt("roomId", 0)!!
        petId = arguments?.getInt("petId", 0)!!
        memberId = arguments?.getInt("memberId", 0)!!
        messageTitle = arguments?.getString("memberName", "")!!

        binding.nameTv.text = messageTitle

        Glide.with(binding.profileImage)
            .load(petAvatar)
            .apply(RequestOptions().override(200, 200))
            .into(binding.profileImage)


        if (!socketConnected) {
            socketIO.connectSocket()
            socketIO.setRooms(roomId)
            PethiioApplication.setCurrentRoom(roomId)

        }





        binding.backBtn.setOnClickListener {
            onDestroy()
            findNavController().navigate(R.id.navigation_message)
        }

        setupViewModel()
        setUpObserver()


        binding.btnSend.setOnClickListener {

            if (!TextUtils.isEmpty(binding.etMessage.text.trim().toString())) {

                socketIO.sendMessage(
                    ChatSendMessage(
                        binding.etMessage.text.trim().toString(),
                        roomId,
                        memberId
                    )
                )
                binding.etMessage.setText("")
            }


        }

        return view
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)

    }

    private fun setUpObserver() {


        binding.reportIv.setOnClickListener {

            onDestroy()

            val bundle =
                bundleOf(
                    "memberId" to memberId, // TODO: 26.08.2021 önceki sayfadan petID ve selectedMemberId'yi al
                    "animalId" to petId.toString(),
                    "isOwner" to true,
                    "fromChat" to true
                )
            findNavController().navigate(R.id.navigation_pet_detail, bundle)
        }

        viewModel.fetchChatPageData()
        viewModel.fetchChatRoom(roomId)
        viewModel.getChatPageData().observe(viewLifecycleOwner, {

            val pageDataFields = it.data?.fields

            binding.etMessage.hint = getLocalizedString(Constants.inputPlaceholder, pageDataFields)

        })

        viewModel.getChatRoomList().observe(viewLifecycleOwner, {

            when (it.status) {
                Status.ERROR -> {
                    binding.progressAvi.hide()
                }
                Status.LOADING -> {
                    binding.progressAvi.show()
                }

                Status.SUCCESS -> {
                    binding.progressAvi.hide()
                    adapter = it.data?.let { it1 ->
                        ChatAdapter(
                            requireContext(), it1, memberId
                        )
                    }



                    if (it.data?.size!! > 0)
                        ChatUpdateStateRequest(
                            it.data[it.data.size - 1].id,
                            roomId,
                            senderMemberId = memberId,
                            "SEEN"
                        ).let { it2 -> viewModel.updateChatState(it2) }

                    it.data.forEach { chat ->

                    }




                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    (binding.recyclerView.layoutManager as LinearLayoutManager).stackFromEnd = true
                    (binding.recyclerView.layoutManager as LinearLayoutManager).reverseLayout =
                        false
                    binding.recyclerView.adapter = adapter

//                    it.data?.let { it1 -> binding.recyclerView.smoothScrollToPosition(it1.size) }


                }
            }


        })

    }


}