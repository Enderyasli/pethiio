package com.pawtind.android.ui.main.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.DefaultItemAnimator
import com.pawtind.android.data.model.Spot
import com.pawtind.android.databinding.FragmentDashboardBinding
import com.pawtind.android.ui.base.BaseFragment
import com.pawtind.android.ui.main.adapter.CardStackAdapter
import com.yuyakaido.android.cardstackview.*
import java.util.ArrayList


class DashboardFragment : BaseFragment(), CardStackListener {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!


    override var bottomNavigationViewVisibility = View.VISIBLE

    private val manager get() = CardStackLayoutManager(requireContext(), this)
    private val adapter get() = CardStackAdapter(createSpots())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view = binding.root



        setupUI()

        return view
    }

    private fun setupUI() {
        initialize()
        setupButton()
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

    private fun createSpots(): List<Spot> {
        val spots = ArrayList<Spot>()
        spots.add(
            Spot(
                name = "Yasaka Shrine",
                city = "Kyoto",
                url = "https://source.unsplash.com/Xq1ntWruZQI/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Fushimi Inari Shrine",
                city = "Kyoto",
                url = "https://source.unsplash.com/NYyCqdBOKwc/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Bamboo Forest",
                city = "Kyoto",
                url = "https://source.unsplash.com/buF62ewDLcQ/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Brooklyn Bridge",
                city = "New York",
                url = "https://source.unsplash.com/THozNzxEP3g/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Empire State Building",
                city = "New York",
                url = "https://source.unsplash.com/USrZRcRS2Lw/600x800"
            )
        )
        spots.add(
            Spot(
                name = "The statue of Liberty",
                city = "New York",
                url = "https://source.unsplash.com/PeFk7fzxTdk/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Louvre Museum",
                city = "Paris",
                url = "https://source.unsplash.com/LrMWHKqilUw/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Eiffel Tower",
                city = "Paris",
                url = "https://source.unsplash.com/HN-5Z6AmxrM/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Big Ben",
                city = "London",
                url = "https://source.unsplash.com/CdVAUADdqEc/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Great Wall of China",
                city = "China",
                url = "https://source.unsplash.com/AWh9C-QjhE4/600x800"
            )
        )
        return spots
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
    }

    override fun onCardRewound() {
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }

}