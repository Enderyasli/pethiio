package com.pethiio.android.ui.main.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pethiio.android.PethiioApplication
import com.pethiio.android.data.EventBus.ChatEvent
import com.pethiio.android.data.model.socket.ChatSendMessage
import com.pethiio.android.data.socket.SocketIO
import com.pethiio.android.data.socket.SocketIOService
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
    private var roomId: Int = 0
    var memberId: Int = 0
    var messageTitle: String = ""
    private var adapter: ChatAdapter? = null

    val socketIO = SocketIO()



    override fun onDestroy() {
        //unregister event bus
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ChatEvent) {

        adapter?.addMessage(event)
        adapter?.listSize?.let { binding.recyclerView.smoothScrollToPosition(it) }
//


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatScreenBinding.inflate(inflater, container, false)
        val view = binding.root

        roomId = arguments?.getInt("roomId", 0)!!
        memberId = arguments?.getInt("memberId", 0)!!
        messageTitle = arguments?.getString("memberName", "")!!

        binding.nameTv.text = messageTitle

        // TODO: 13.07.2021 aç bunu
        socketIO.connectSocket()
        socketIO.setRooms(roomId)




        binding.backIv.setOnClickListener {
            findNavController().navigateUp()
        }

        setupViewModel()
        setUpObserver()

        binding.btnSend.setOnClickListener {

            if (binding.etMessage.text.isNotEmpty()) {

                /*
                activity?.let {
                     var socketService: SocketIOService? = null
                    socketService = SocketIOService()
                    socketService.changeRoomId(roomId)
                    val service = Intent(it, socketService.javaClass)



//                    val service = Intent(it, SocketIOService::class.java)
                    service.putExtra(
                        SocketIOService.EXTRA_EVENT_TYPE,
                        SocketIOService.EVENT_TYPE_MESSAGE
                    )


                    // TODO: 14.07.2021 burda roomID yolla
                    service.putExtra(
                        SocketIOService.EXTRA_DATA,
                        ChatSendMessage(
                            binding.etMessage.text.trim().toString(),
                            roomId,
                            memberId
                        )
                    )
                    it.startService(service)

                }
                */

//                val service = Intent(this@ChatScreenFragment, SocketIOService::class.java)
//                service.putExtra(SocketIOService.EXTRA_EVENT_TYPE, SocketIOService.EVENT_TYPE_JOIN)
////                service.putExtra(SocketIOService.EXTRA_USER_NAME, response.getData().getId())
//                requireContext().startService(service)

                socketIO.sendMessage(
                    ChatSendMessage(
                        binding.etMessage.text.trim().toString(),
                        roomId,
                        memberId
                    )
                ,roomId
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

        viewModel.fetchChatPageData()
        viewModel.fetchChatRoom(roomId)
        viewModel.getChatPageData().observe(viewLifecycleOwner, {

            val pageDataFields = it.data?.fields

            binding.etMessage.hint = getLocalizedString(Constants.inputPlaceholder, pageDataFields)

        })

        viewModel.getChatRoomList().observe(viewLifecycleOwner, {

            when (it.status) {

                Status.SUCCESS -> {

                    adapter = it.data?.let { it1 ->
                        ChatAdapter(
                            requireContext(), it1, memberId
                        )
                    }
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    (binding.recyclerView.layoutManager as LinearLayoutManager).stackFromEnd = true
                    (binding.recyclerView.layoutManager as LinearLayoutManager).reverseLayout = false
                    binding.recyclerView.adapter = adapter

//                    it.data?.let { it1 -> binding.recyclerView.smoothScrollToPosition(it1.size) }


                }
            }


        })

    }


}