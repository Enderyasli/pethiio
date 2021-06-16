package com.pawtind.android.ui.main.view.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.pawtind.android.R
import com.pawtind.android.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_on_boarding.*
import kotlinx.android.synthetic.main.fragment_on_boarding_inside.*

class OnBoardingInsideFragment : BaseFragment() {

    private lateinit var title: String
    private var imageResource = 0

    private lateinit var mViewPager: ViewPager
    private lateinit var textSkip: TextView
    private lateinit var textNextStep: TextView

    override var bottomNavigationViewVisibility = View.GONE


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            title = requireArguments().getString(ARG_PARAM1)!!
            imageResource = requireArguments().getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_on_boarding_inside, container, false)

        val titleTv: TextView = view.findViewById(R.id.titleTv) as TextView
        val imageView: ImageView = view.findViewById(R.id.img_onboard) as ImageView

        titleTv.text = title
        imageView.setImageResource(imageResource)

        return view
    }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        fun newInstance(
            title: String?,
            imageResource: Int,
        ): OnBoardingInsideFragment {
            val fragment = OnBoardingInsideFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, title)
            args.putInt(ARG_PARAM2, imageResource)
            fragment.arguments = args
            return fragment
        }
    }

}