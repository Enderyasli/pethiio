package com.pethiio.android.ui.main.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pethiio.android.databinding.FragmentChatScreenBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.adapter.ChatAdapter
import com.pethiio.android.ui.main.viewmodel.ChatViewModel
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter


class ChatScreenFragment : BaseFragment() {

    private var _binding: FragmentChatScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChatViewModel
    override var bottomNavigationViewVisibility = View.GONE
    private var roomId: Int = 0
    var memberId: Int = 0
    private var adapter: ChatAdapter? = null

    lateinit var mSocket: Socket



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatScreenBinding.inflate(inflater, container, false)
        val view = binding.root

        roomId = arguments?.getInt("roomId", 0)!!
        memberId = arguments?.getInt("memberId", 0)!!

        binding.backIv.setOnClickListener {
            findNavController().navigateUp()
        }

        setupViewModel()
        setUpObserver()

        binding.btnSend.setOnClickListener {
            socket()
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
                            requireContext(), it1,memberId)
                    }
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    binding.recyclerView.adapter = adapter
                    it.data?.let { it1 -> binding.recyclerView.smoothScrollToPosition(it1.size) }


                }
            }


        })

    }

    private fun socket(){

        var onConnect = Emitter.Listener {
            //After getting a Socket.EVENT_CONNECT which indicate socket has been connected to server,
//            //send userName and roomName so that they can join the room.
//            val data = initialData(userName, roomName)
//            val jsonData = gson.toJson(data) // Gson changes data object to Json type.
//            mSocket.emit("subscribe", jsonData)
        }

        try {
            //This address is the way you can connect to localhost with AVD(Android Virtual Device)
            mSocket = IO.socket("http://api.pawtind.com:9092")
            Log.d("success", mSocket.id())

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect")
        }

        mSocket.connect()
        mSocket.on(Socket.EVENT_CONNECT, onConnect)


        //Register all the listener and callbacks here.
        mSocket.on(Socket.EVENT_CONNECT, onConnect)

    }



}