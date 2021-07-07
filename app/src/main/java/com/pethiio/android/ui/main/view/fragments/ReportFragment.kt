package com.pethiio.android.ui.main.view.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pethiio.android.data.model.report.ReportRequest
import com.pethiio.android.data.model.report.SupportRequest
import com.pethiio.android.databinding.FragmentReportBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.viewmodel.ReportViewModel
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status


class ReportFragment : BaseFragment() {

    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    override var bottomNavigationViewVisibility = View.GONE

    private lateinit var viewModel: ReportViewModel


    var userId: Int? = 0
    var fromFAQ: Boolean? = false
    var detailEmtpyError: String = ""

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

        userId = arguments?.getInt("userId")
        fromFAQ = arguments?.getBoolean("fromFAQ")
        if (fromFAQ == true) {
            viewModel.fetchSupportPageData()
            viewModel.fetchSupportSuccessPageData()

        } else {
            viewModel.fetchReportPageData()
        }

        binding.reportBtn.setOnClickListener {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            postReport()
        }




        return view
    }


    private fun postReport() {

        if (binding.detailPlaceholderTv.text.trim().toString() == "") {
            binding.detailPlaceholderTv.error = detailEmtpyError
            return
        }
        if (fromFAQ == false) {

            if (userId != null && userId!! > 0 && binding.reasonLy.spinner.selectedItem != null) {

                val type = getLookUpKey(
                    Constants.lookUpType,
                    binding.reasonLy.spinner.selectedItem.toString()
                )
                if (type.isNotEmpty())
                    viewModel.postReport(
                        ReportRequest(
                            binding.detailPlaceholderTv.text.toString(),
                            userId!!,
                            type
                        )
                    )
            }
        } else {
            val type = getLookUpKey(
                Constants.lookUpType,
                binding.reasonLy.spinner.selectedItem.toString()
            )
            if (type.isNotEmpty())
                viewModel.postSupport(
                    SupportRequest(
                        binding.detailPlaceholderTv.text.toString(),
                        type
                    )
                )

        }


    }

    override fun setUpViews() {
        super.setUpViews()
        viewModel.getReportSuccessPageData().observe(viewLifecycleOwner, {
            when (it.status) {

                Status.SUCCESS -> {

                    val pageDataFields = it.data?.fields

                    binding.messageTitle.text =
                        getLocalizedString(Constants.reportTitle, pageDataFields)
                    binding.msgContent.text =
                        getLocalizedString(Constants.reportSubTitle, pageDataFields)
                }
            }

        })


        viewModel.getReportPageData().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {


                    val pageDataFields = it.data?.fields
                    val pageDataLookUps = it.data?.lookups
                    pageDataLookUps?.let { it1 -> setLookUps(it1) }

                    binding.reportTitle.text =
                        getLocalizedString(Constants.reportTitle, pageDataFields)
                    binding.reportSubtitle.text =
                        getLocalizedString(Constants.reportSubTitle, pageDataFields)
                    binding.reasonLy.titleTv.text =
                        getLocalizedSpan(Constants.reportTypeTitle, pageDataFields)
                    binding.detailTitleTv.text =
                        getLocalizedSpan(Constants.reportDetailTitle, pageDataFields)
                    binding.detailPlaceholderTv.hint =
                        getLocalizedString(Constants.reportDetailPlaceholder, pageDataFields)
                    binding.reportBtn.text =
                        getLocalizedString(Constants.reportButton, pageDataFields)

                    detailEmtpyError =
                        getLocalizedString(Constants.reportDetailEmptyError, pageDataFields)


                    val typeAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        getLookUps(Constants.lookUpType, pageDataLookUps)
                    )
                    typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    with(binding.reasonLy.spinner)
                    {
                        adapter = typeAdapter
                        gravity = android.view.Gravity.CENTER
                        setSelection(0)
                    }

                    binding.progressBar.visibility = View.GONE
                }


            }

        })

        viewModel.getReportResponse().observe(viewLifecycleOwner, {
            when (it.status) {

                Status.SUCCESS -> {
                    if (fromFAQ == false)
                        findNavController().navigateUp()
                    else {

                        Handler().postDelayed({ findNavController().navigateUp() }, 2500)

                        binding.mainLayout.visibility = View.GONE
                        binding.msgLy.visibility = View.VISIBLE

                    }
                }
            }
        })
    }
}