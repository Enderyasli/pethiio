package com.pethiio.android.ui.main.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.pethiio.android.R
import com.pethiio.android.data.EventBus.FilterEvent
import com.pethiio.android.data.model.member.LocationsRequest
import com.pethiio.android.data.model.member.MemberListResponse
import com.pethiio.android.data.model.member.PetSearchRequest
import com.pethiio.android.data.model.member.PetSearchResponse
import com.pethiio.android.databinding.FragmentDashboardBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.base.ViewModelFactory
import com.pethiio.android.ui.main.adapter.CardStack.CardSackDiffCallback
import com.pethiio.android.ui.main.adapter.CardStack.CardStackAdapter
import com.pethiio.android.ui.main.view.customViews.MemberListSpinner
import com.pethiio.android.ui.main.viewmodel.DashBoardViewModel
import com.pethiio.android.utils.CommonFunctions
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status
import com.yuyakaido.android.cardstackview.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


class DashboardFragment : BaseFragment(), CardStackListener,
    AdapterView.OnItemSelectedListener {

    private val manager get() = CardStackLayoutManager(requireContext(), this)
    private var adapter: CardStackAdapter? = null

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    override var bottomNavigationViewVisibility = View.VISIBLE
    override var dashboardClicked: Boolean = true


    private lateinit var viewModel: DashBoardViewModel

    private var memberListResponse = emptyList<MemberListResponse>()
    private var isSelectedMemberFirstTime = true
    private var selectedMemberId = 0


    var searchList: List<PetSearchResponse>? = null
    var memberId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        EventBus.getDefault().register(this)

        checkLocationPermission()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel =
            ViewModelProviders.of(this, ViewModelFactory()).get(DashBoardViewModel::class.java)
        isSelectedMemberFirstTime = true

        setupUI()

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(false) {
                override fun handleOnBackPressed() {
                    Toast.makeText(requireContext(), "back", Toast.LENGTH_LONG).show()
                    // Handle the back button event
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)



        return view
    }


    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                // TODO: 28.06.2021 burada tasarım ac izne gönder

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(requireContext())
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
//                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
//                requestLocationPermission()
            }
        }
    }


    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }

    private fun setupUI() {
        setupButton()
        viewModel.fetchPetSearchPageData()

        viewModel.fetchLocations(
            LocationsRequest(
                "37.414972210438144",
                "41.37366053245295"
            )
        )
        viewModel.fetchMemberList()

        viewModel.getPostSearchFilter().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    viewModel.fetchMemberList()
                }
            }
        })

        viewModel.getPetSearchPageData().observe(viewLifecycleOwner, {

            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    val fields = it.data?.fields
                    binding.titleTv.text = getLocalizedString(Constants.petSearchTitle, fields)
                    binding.petEmptyErrorTv.text =
                        getLocalizedString(Constants.petSearchEmptyMessageTitle, fields)

                    binding.progressBar.visibility = View.GONE

                }

            }

        })

        viewModel.getMemberList().observe(viewLifecycleOwner, {
            CommonFunctions.checkLogin(it.errorCode, findNavController())

            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    if (it.data != null)
                        memberListResponse = it.data
                    setMembeListSpinner(memberListResponse)
                    binding.progressBar.visibility = View.GONE
                }
                Status.ERROR -> {
                    CommonFunctions.checkLogin(it.errorCode, findNavController())

                }
            }

        })

        viewModel.postSearchResponse().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                }
            }

        })
        viewModel.getSearchList().observe(viewLifecycleOwner, { it ->

            when (it.status) {
//                Status.LOADING -> { binding.progressBar.visibility = View.VISIBLE }

                Status.SUCCESS -> {

                    it.data?.let {
                        adapter = CardStackAdapter(findNavController(), it)
                        searchList = it
                        initialize()
                    }
                    if (it.data?.size == 0)
                        binding.emptyLayout.visibility = View.VISIBLE
                    else
                        binding.emptyLayout.visibility = View.GONE


                    binding.progressBar.visibility = View.GONE

                }
            }


        })
    }

    private fun removeFirst() {
        if (adapter?.getPetSearchList()?.isEmpty() == true) {
            return
        }

        val old = adapter?.getPetSearchList()
        val new = mutableListOf<PetSearchResponse>().apply {
            old?.let { addAll(it) }

            removeAt(manager.topPosition)

        }
        val callback = old?.let { CardSackDiffCallback(it, new) }
        val result = callback?.let { DiffUtil.calculateDiff(it) }
        adapter?.setPetSearchList(new)
        adapter?.let { result?.dispatchUpdatesTo(it) }
    }

    @SuppressLint("ResourceType")
    fun setMembeListSpinner(memberListResponse: List<MemberListResponse>) {
        val customAdapter = MemberListSpinner(requireContext(), memberListResponse)

        if (isSelectedMemberFirstTime)
            with(binding.memberlistSpinner)
            {
                id = 1
                adapter = customAdapter
                onItemSelectedListener = this@DashboardFragment
                gravity = Gravity.CENTER
                if (memberListResponse.isNotEmpty() && isSelectedMemberFirstTime) {
                    binding.memberlistSpinner.setSelection(selectedMemberId)
                    isSelectedMemberFirstTime = false
                }
            }


    }

    private fun setupButton() {

        binding.skipButton.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            binding.cardStackView.swipe()
        }


        binding.searchFilterButton.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.navigation_dashboard)
                findNavController().navigate(R.id.action_navigation_dashboard_to_bottomSheetDialog)
        }



        binding.likeButton.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            binding.cardStackView.swipe()
        }
    }

    private fun initialize() {
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        binding.cardStackView.layoutManager = manager
        binding.cardStackView.adapter = adapter
        binding.cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: FilterEvent?) {
        viewModel.fetchPetSearch(memberId)


//        CommonFunctions.goWelcome(findNavController())
//        findNavController().navigate(R.id.navigation_welcome, null)
//        Toast.makeText(context, "event", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        //unregister event bus
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }


    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }


    override fun onCardSwiped(direction: Direction?) {


        removeFirst()

        if (adapter?.getPetSearchList()?.isEmpty() == true) {
            return
        }

        val petSearch = adapter?.getPetSearchList()?.get(manager.topPosition)

        if (direction != null && memberId > 0) {
            petSearch?.petId?.let {
                PetSearchRequest(
                    memberId,
                    it,
                    direction == Direction.Right,
                    "DATING"
                )
            }?.let {
                viewModel.postPetSearch(
                    it
                )
            }

        }

//        Log.d("petsize",adapter.getPetSearchList().size.toString())


    }

    override fun onCardRewound() {
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            1 -> {
                memberListResponse[position].id.let {
                    viewModel.fetchPetSearch(it)
                    memberId = it
                    selectedMemberId = position
                }

            }

        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


}