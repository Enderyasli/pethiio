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
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.AdapterView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
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
import com.pethiio.android.utils.Status
import com.yuyakaido.android.cardstackview.*
import java.util.*


class DashboardFragment : BaseFragment(), CardStackListener,
    AdapterView.OnItemSelectedListener {

    private val manager get() = CardStackLayoutManager(requireContext(), this)
    private var adapter: CardStackAdapter = CardStackAdapter(emptyList())

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    override var bottomNavigationViewVisibility = View.VISIBLE

    private lateinit var viewModel: DashBoardViewModel

    private var memberListResponse: List<MemberListResponse>? = null

    var searchList: List<PetSearchResponse>? = null
    var memberId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        setupUI()

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

        viewModel.fetchLocations(
            LocationsRequest(
                "37.414972210438144",
                "41.37366053245295"
            )
        )
        viewModel.fetchMemberList()

        viewModel.getMemberList().observe(viewLifecycleOwner, {
            memberListResponse = it.data
            memberListResponse?.let { it1 ->
                setMembeListSpinner(it1)

            }

        })

        viewModel.postSearchResponse().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                }
            }

        })
    }
    private fun removeFirst() {
        if (adapter.getPetSearchList().isEmpty()) {
            return
        }

        val old = adapter.getPetSearchList()
        val new = mutableListOf<PetSearchResponse>().apply {
            addAll(old)

                removeAt(manager.topPosition)

        }
        val callback = CardSackDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setPetSearchList(new)
        result.dispatchUpdatesTo(adapter)
    }

    @SuppressLint("ResourceType")
    fun setMembeListSpinner(memberListResponse: List<MemberListResponse>) {
        val customAdapter = MemberListSpinner(requireContext(), memberListResponse)

        viewModel.getSearchList().observe(viewLifecycleOwner, { it ->

            it.data?.let {
                adapter = CardStackAdapter(it)
                searchList = it
                initialize()
            }
        })
        with(binding.memberlistSpinner)
        {
            id = 1
            adapter = customAdapter
            onItemSelectedListener = this@DashboardFragment
            gravity = Gravity.CENTER
        }
        if (memberListResponse.isNotEmpty())
            binding.memberlistSpinner.setSelection(0)

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


        binding.sliderHorizontal.setOnClickListener {
            val setting = RewindAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(DecelerateInterpolator())
                .build()
            manager.setRewindAnimationSetting(setting)
            binding.cardStackView.rewind()
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

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }


    override fun onCardSwiped(direction: Direction?) {

        removeFirst()
        val petSearch = adapter.getPetSearchList()[manager.topPosition]

        if (direction != null && memberId > 0) {
            viewModel.postPetSearch(
                PetSearchRequest(
                    memberId,
                    petSearch.petId,
                    direction == Direction.Right,
                    "DATING"
                )
            )

        }

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
                memberListResponse?.get(position)?.id?.let {
                    viewModel.fetchPetSearch(it)
                    memberId = it
                }

            }

        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


}