package com.pethiio.android.ui.main.view.fragments.mainFragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
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
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import co.mobiwise.materialintro.animation.MaterialIntroListener
import co.mobiwise.materialintro.shape.Focus
import co.mobiwise.materialintro.shape.FocusGravity
import co.mobiwise.materialintro.shape.ShapeType
import co.mobiwise.materialintro.view.MaterialIntroView
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.pethiio.android.R
import com.pethiio.android.data.EventBus.FilterEvent
import com.pethiio.android.data.EventBus.LikeEvent
import com.pethiio.android.data.EventBus.MatchEvent
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
import com.pethiio.android.utils.*
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.fragment_pet_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


class DashboardFragment : BaseFragment(), CardStackListener,
    AdapterView.OnItemSelectedListener, MaterialIntroListener {

    private var manager: CardStackLayoutManager? = null
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

    private lateinit var viewModel: DashBoardViewModel
    private var memberListResponse = emptyList<MemberListResponse>()
    private var isSelectedMemberFirstTime = true
    private var isLocationSended = false // TODO: 8.07.2021 false yap
    private var selectedMemberId = 0
    private var memberListSize = 0
    private var fromNotification = false
    var animalId = -1

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

    override fun onResume() {
        super.onResume()
        checkGpsPermission()

    }

    override fun onDestroy() {
        //unregister event bus
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onUserClicked(materialIntroViewId: String?) {
    }

    override fun onStart() {
        super.onStart()
        val locationRequest = LocationRequest.create().apply {
            interval = 3000
            fastestInterval = 1500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (location != null) {

                        if (!isLocationSended) {
                            viewModel.fetchLocations(
                                LocationsRequest(
                                    location.latitude.toString(),
                                    location.longitude.toString()
                                )
                            )
                            isLocationSended = true
                            viewModel.fetchMemberList()
                        }

                        //TODO: UI burda cagır xiomi de
                    }
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        LocationServices.getFusedLocationProviderClient(requireContext())
            .requestLocationUpdates(locationRequest, mLocationCallback, null);

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view = binding.root

        manager = CardStackLayoutManager(requireContext(), this)

        viewModel =
            ViewModelProviders.of(this, ViewModelFactory()).get(DashBoardViewModel::class.java)
        isSelectedMemberFirstTime = true


        setupUI()


        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    requireActivity().finish() // TODO: 7.07.2021 loginden sonra kontrol et
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        requestCurrentLocation()

        return view
    }

    //region Location

    fun checkGpsPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
//            CommonMethods.onSNACK(binding.root, getString(R.string.no_location_detected))
            //main activityde handle edildi
        } else {
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestCurrentLocation() {

        val currentLocationTask: Task<Location> = fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        )

//        samsung da izin aldıktan sonra giriyor

        currentLocationTask.addOnCompleteListener { task: Task<Location> ->
            if (task.isSuccessful && task.result != null) {
                val result: Location = task.result
//                "Location (success): ${result.latitude}, ${result.longitude}"

                if (!isLocationSended) {
                    viewModel.fetchLocations(
                        LocationsRequest(
                            result.latitude.toString(),
                            result.longitude.toString()
                        )
                    )
                    isLocationSended = true

                }

            } else {
                viewModel.fetchMemberList()
                val exception = task.exception
                "Location (failure): $exception"
            }

//            Log.d("location", "getCurrentLocation() result: $result")
        }
        //xiaomi de giriyor

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let { it: Location ->
                if (!isLocationSended) {
                    viewModel.fetchLocations(
                        LocationsRequest(
                            it.latitude.toString(),
                            it.longitude.toString()
                        )
                    )
                    isLocationSended = true
                    viewModel.fetchMemberList()
                }

            } ?: kotlin.run {
                viewModel.fetchMemberList()
                // Handle Null case or Request periodic location update https://developer.android.com/training/location/receive-location-updates
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {

                        findNavController().navigateUp()

                    }

                } else {
//                    Toast.makeText(requireContext(), "else e girdi", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //endregion


    private fun setupUI() {

        fromNotification = arguments?.getBoolean("fromNotification", false) == true
        if (fromNotification) {
            val bundle =
                bundleOf(
                    "fromNotification" to true,
                    "purpose" to arguments?.getString("purpose", "")!!,
                    "memberId" to arguments?.getInt("memberId", 0)!!,
                    "roomId" to arguments?.getInt("roomId", 0)!!,
                    "sourceName" to arguments?.getString("sourceName", "")!!,
                    "sourceAvatar" to arguments?.getString("sourceAvatar", "")!!,
                    "targetName" to arguments?.getString("targetName", "")!!,
                    "targetAvatar" to arguments?.getString("targetAvatar", "")!!
                )

            findNavController().navigate(R.id.navigation_match, bundle)


        }

        binding.progressAvi.hide()

        binding.noResultImg.setAnimation("bulunamadi.json")
        setupButton()
        viewModel.fetchPetSearchPageData()

        viewModel.getLocations().observe(viewLifecycleOwner, {

            when (it.status) {
                Status.SUCCESS -> {
                    if (!isLocationSended) {
                        viewModel.fetchMemberList()
                        isLocationSended = true
                    }

                }
            }

        })
        if (isLocationSended) {
            viewModel.fetchMemberList()
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
                    binding.progressAvi.show()
                }

                Status.SUCCESS -> {
                    val fields = it.data?.fields
                    binding.titleTv.text = getLocalizedString(Constants.petSearchTitle, fields)
                    binding.noAnimalTv.text =
                        getLocalizedString(Constants.petSearchEmptyMessageTitle, fields)


                }

            }

        })

        viewModel.getMemberList().observe(viewLifecycleOwner, {
            CommonFunctions.checkLogin(it.errorCode, findNavController())

            when (it.status) {
                Status.LOADING -> {
                    binding.progressAvi.show()
                }
                Status.SUCCESS -> {
                    if (it.data != null) {
                        if (it.data.isNotEmpty()) {
                            memberListSize = it.data.size
                            binding.memberlistSpinner.visibility = View.VISIBLE
                            binding.searchFilterButton.visibility = View.VISIBLE
                            if (adapter?.getPetSearchList()?.isEmpty() == false)
                                binding.noResultLayout.visibility = View.GONE

                            memberListResponse = it.data
                            setMembeListSpinner(memberListResponse)
//                    setMemberList(memberListResponse)
                        } else {
                            memberListSize = 0

                            binding.noResultLayout.visibility = View.VISIBLE
                            binding.memberlistSpinner.visibility = View.GONE
                            binding.searchFilterButton.visibility = View.INVISIBLE

                        }
                    }
                    binding.progressAvi.hide()


                }
                Status.ERROR -> {
                    CommonFunctions.checkLogin(it.errorCode, findNavController())

                }
            }

        })

//        viewModel.postSearchResponse().observe(viewLifecycleOwner, {
//            when (it.status) {
//                Status.SUCCESS -> {
//                }
//            }
//
//        })
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
                        binding.noResultLayout.visibility = View.VISIBLE
                        binding.skipButton.visibility = View.GONE
                        binding.likeButton.visibility = View.GONE
                    } else {
                        spotlightLike()
                        binding.noResultLayout.visibility = View.GONE
                        binding.skipButton.visibility = View.VISIBLE
                        binding.likeButton.visibility = View.VISIBLE
                    }

                    if (memberListSize == 0) {
                        binding.noResultLayout.visibility = View.VISIBLE
                    }


                    binding.progressAvi.hide()
                }
            }


        })

//        viewModel.getSearchFilterList().observe(viewLifecycleOwner, {
//            when (it.status) {
//                Status.SUCCESS -> {
//
//                    purposeFilter = it.data?.purpose.toString()
//
//                }
//            }
//        })
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
                onItemSelectedListener = this@DashboardFragment
                gravity = Gravity.CENTER
                if (memberListResponse.isNotEmpty() && isSelectedMemberFirstTime) {
                    binding.memberlistSpinner.setSelection(selectedMemberId)
                    isSelectedMemberFirstTime = false
                }
            }

    }

    private fun removeFirst() {
        if (adapter?.getPetSearchList()?.isEmpty() == true) {
            return
        }

        val old = adapter?.getPetSearchList()
        val new = mutableListOf<PetSearchResponse>().apply {
            old?.let { addAll(it) }


            manager?.topPosition?.minus(1)?.let { removeAt(it) }

//            removeAt(manager.topPosition-1)

        }

        viewModel.postSearchResponse().observe(viewLifecycleOwner, {

            when (it.status) {
                Status.SUCCESS -> {
                    if (new.isEmpty()) {
                        viewModel.fetchPetSearch(memberId)
                    }
                }
            }
        })


        val callback = old?.let { CardSackDiffCallback(it, new) }
        val result = callback?.let { DiffUtil.calculateDiff(it) }
        adapter?.setPetSearchList(new)
        adapter?.let { result?.dispatchUpdatesTo(it) }
    }

    private fun setupButton() {

        binding.skipButton.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager?.setSwipeAnimationSetting(setting)
            binding.cardStackView.swipe()
        }


        binding.searchFilterButton.setOnClickListener {

            if (animalId != -1) {
                val bundle = bundleOf("animalId" to animalId)

                if (findNavController().currentDestination?.id == R.id.navigation_dashboard)
                    findNavController().navigate(
                        R.id.action_navigation_dashboard_to_bottomSheetDialog,
                        bundle
                    )
            }

        }



        binding.likeButton.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager?.setSwipeAnimationSetting(setting)
            binding.cardStackView.swipe()
        }
    }

    private fun initialize() {
        manager?.setStackFrom(StackFrom.None)
        manager?.setVisibleCount(3)
        manager?.setTranslationInterval(8.0f)
        manager?.setScaleInterval(0.95f)
        manager?.setSwipeThreshold(0.3f)
        manager?.setMaxDegree(20.0f)
        manager?.setDirections(Direction.HORIZONTAL)
        manager?.setCanScrollHorizontal(true)
        manager?.setCanScrollVertical(true)
        manager?.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager?.setOverlayInterpolator(LinearInterpolator())
        if (binding.cardStackView.layoutManager == null)
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MatchEvent?) {

        //Eşleşme anlık mesaj

        if (event != null) {
            val bundle =
                bundleOf(
                    "fromNotification" to true,
                    "purpose" to event.purpose,
                    "memberId" to event.memberId,
                    "roomId" to event.roomId,
                    "sourceName" to event.sourceName,
                    "sourceAvatar" to event.sourceAvatar,
                    "targetName" to event.targetName,
                    "targetAvatar" to event.targetAvatar
                )

            findNavController().navigate(R.id.navigation_match, bundle)

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: LikeEvent?) {
        var direction: Direction = if (event?.like == true)
            Direction.Right
        else
            Direction.Left


        val setting = SwipeAnimationSetting.Builder()
            .setDirection(direction)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()
        manager?.setSwipeAnimationSetting(setting)
        binding.cardStackView.swipe()

    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {

        spotlightMemberList()


        if (adapter?.getPetSearchList()?.isEmpty() == true) {
            return
        }

        val petSearch = adapter?.getPetSearchList()?.get(0)

        removeFirst()

        if (direction != null && memberId > 0) {
            petSearch?.memberId?.let {
                PetSearchRequest(
                    memberId,
                    it,
                    direction == Direction.Right
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
                memberListResponse[position].animalId.let {
                    animalId = it
                }

                memberListResponse[position].id.let {
                    viewModel.fetchPetSearch(it)
                    memberId = it
                    PreferenceHelper.SharedPreferencesManager.getInstance().memberId = it
                    selectedMemberId = position
                    PreferenceHelper.SharedPreferencesManager.getInstance().selectedSpinnerId =
                        selectedMemberId

                    if (PreferenceHelper.SharedPreferencesManager.getInstance().isLikedOnce)
                        spotlightSettings()

                }

            }

        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private fun spotlightMemberList() {
        if (findNavController().currentDestination?.id == R.id.navigation_dashboard)
            MaterialIntroView.Builder(requireActivity())
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(500)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText(getString(R.string.spotlight_memberlist))
                .setShape(ShapeType.RECTANGLE)
                .setTarget(binding.memberlistSpinner)
                .setUsageId(TUTORIAL_MEMBER) //THIS SHOULD BE UNIQUE ID
                .setListener(this)
                .show()

        PreferenceHelper.SharedPreferencesManager.getInstance().isLikedOnce = true

    }

    private fun spotlightLike() {

        if (findNavController().currentDestination?.id == R.id.navigation_dashboard)
            MaterialIntroView.Builder(requireActivity())
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(500)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText(getString(R.string.spotlight_like))
                .setShape(ShapeType.RECTANGLE)
                .setTarget(binding.cardStackView)
                .setUsageId(TUTORIAL_LIKE)
                .setListener(this)
                .show()

    }

    private fun spotlightSettings() {

        if (findNavController().currentDestination?.id == R.id.navigation_dashboard)
            MaterialIntroView.Builder(requireActivity())
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(500)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText(getString(R.string.spotlight_settings))
                .setShape(ShapeType.RECTANGLE)
                .setTarget(binding.searchFilterButton)
                .setUsageId("TUTORIAL_SETTINGSs1sadd")
                .setListener(this)
                .show()
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        const val TUTORIAL_LIKE = "like"
        const val TUTORIAL_MEMBER = "memberlist"
        const val TUTORIAL_SETTINGS = "settings"
    }


}