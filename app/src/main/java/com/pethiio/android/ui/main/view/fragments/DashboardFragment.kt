package com.pethiio.android.ui.main.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.AdapterView
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
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
import com.pethiio.android.utils.PreferenceHelper
import com.pethiio.android.utils.Status
import com.yuyakaido.android.cardstackview.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


class DashboardFragment : BaseFragment(), CardStackListener,
    AdapterView.OnItemSelectedListener {

    private val manager by lazy { CardStackLayoutManager(requireContext(), this) }
    private var adapter: CardStackAdapter? = null

    // The Fused Location Provider provides access to location APIs.
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }
    private var cancellationTokenSource = CancellationTokenSource()


    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    override var bottomNavigationViewVisibility = View.VISIBLE
    override var dashboardClicked: Boolean = true

    private var purposeFilter: String = "DATING"

    private lateinit var viewModel: DashBoardViewModel

    private var memberListResponse = emptyList<MemberListResponse>()
    private var isSelectedMemberFirstTime = true
    private var isLocationSended = false

    private var selectedMemberId = 0


    var searchList: List<PetSearchResponse>? = null
    var memberId: Int = 0

    override fun onStop() {
        super.onStop()
        // Cancels location request (if in flight).
        cancellationTokenSource.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        EventBus.getDefault().register(this)

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
//        val socketIO = SocketIO()
//        socketIO.main()

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    requireActivity().finish() // TODO: 7.07.2021 loginden sonra kontrol et
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

//        checkLocationPermission()

        requestCurrentLocation()

        return view
    }

    @SuppressLint("MissingPermission")
    private fun requestCurrentLocation() {


        val currentLocationTask: Task<Location> = fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        )

        currentLocationTask.addOnCompleteListener { task: Task<Location> ->
            if (task.isSuccessful && task.result != null) {
                val result: Location = task.result
//                "Location (success): ${result.latitude}, ${result.longitude}"

                viewModel.fetchLocations(
                    LocationsRequest(
                        result.latitude.toString(),
                        result.longitude.toString()
                    )
                )
            } else {
                val exception = task.exception
                "Location (failure): $exception"
            }

//            Log.d("location", "getCurrentLocation() result: $result")
        }
    }


    private fun checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // TODO: 6.07.2021 burda location izin ekranına gönder, gelişte location actır, sonra lat, lon al
                findNavController().navigate(R.id.navigation_location)
//                ActivityCompat.requestPermissions(requireActivity(),
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                findNavController().navigate(R.id.navigation_location)
//                ActivityCompat.requestPermissions(requireActivity(),
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }

    }


    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }

    private fun setupUI() {

        binding.noResultImg.setAnimation("evim.json")
        setupButton()
        viewModel.fetchPetSearchPageData()

        viewModel.getLocations().observe(viewLifecycleOwner, {

            when (it.status) {
                Status.SUCCESS -> {
                    if (!isLocationSended) {
                        viewModel.fetchMemberList()
                        viewModel.fetchFilterList()
                        isLocationSended = true
                    }

                }
            }

        })
        if (isLocationSended) {
            viewModel.fetchMemberList()
            viewModel.fetchFilterList()
        }




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
//                    setMemberList(memberListResponse)
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
                    if (it.data?.size == 0) {
                        binding.emptyLayout.visibility = View.VISIBLE
                        binding.skipButton.visibility = View.GONE
                        binding.likeButton.visibility = View.GONE
                    } else {
                        binding.emptyLayout.visibility = View.GONE
                        binding.skipButton.visibility = View.VISIBLE
                        binding.likeButton.visibility = View.VISIBLE
                    }


                    binding.progressBar.visibility = View.GONE

                }
            }


        })

        viewModel.getSearchFilterList().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {

                    purposeFilter = it.data?.purpose.toString()

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

            removeAt(manager.topPosition - 1)

//            removeAt(manager.topPosition-1)

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
        viewModel.fetchFilterList()
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


        if (adapter?.getPetSearchList()?.isEmpty() == true) {
            return
        }


        val petSearch = adapter?.getPetSearchList()?.get(manager.topPosition)

        removeFirst()

        if (direction != null && memberId > 0) {
            petSearch?.petId?.let {
                PetSearchRequest(
                    memberId,
                    it,
                    direction == Direction.Right,
                    purposeFilter
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
                    PreferenceHelper.SharedPreferencesManager.getInstance().memberId = it
                    selectedMemberId = position
                }

            }

        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


}