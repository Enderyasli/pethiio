package com.pethiio.android.ui.main.view.fragments

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProviders
import com.pethiio.android.databinding.FragmentReportBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.viewmodel.ReportViewModel
import com.pethiio.android.utils.Constants


class ReportFragment : BaseFragment() {

    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    override var bottomNavigationViewVisibility = View.GONE

    private lateinit var viewModel: ReportViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel =
            ViewModelProviders.of(this).get(ReportViewModel::class.java)


        return view
    }

    override fun setUpViews() {
        super.setUpViews()
        viewModel.fetchReportPageData()

        viewModel.getReportPageData().observe(viewLifecycleOwner, {

            val pageDataFields = it.data?.fields
            val pageDataLookUps = it.data?.lookups

            binding.reportTitle.text = getLocalizedString(Constants.reportTitle, pageDataFields)
            binding.reportSubtitle.text =
                getLocalizedString(Constants.reportSubTitle, pageDataFields)
            binding.reasonLy.titleTv.text =
                getLocalizedSpan(Constants.reportTypeTitle, pageDataFields)
            binding.detailTitleTv.text =
                getLocalizedSpan(Constants.reportDetailTitle, pageDataFields)
            binding.detailPlaceholderTv.hint =
                getLocalizedString(Constants.reportDetailPlaceholder, pageDataFields)
            binding.reportBtn.text = getLocalizedString(Constants.reportButton, pageDataFields)


            val typeAdapter = ArrayAdapter(
                requireContext(),
                R.layout.simple_spinner_item,
                getLookUps(Constants.lookUpType, pageDataLookUps)
            )
            typeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

            with(binding.reasonLy.spinner)
            {
                adapter = typeAdapter
                gravity = android.view.Gravity.CENTER
                setSelection(0)
            }

        })
    }
}