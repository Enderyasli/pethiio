package com.pethiio.android.ui.main.view.fragments.mainFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pethiio.android.databinding.FragmentVetBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.adapter.HomePetListAdapter
import com.pethiio.android.ui.main.adapter.VetAdapter
import com.pethiio.android.ui.main.viewmodel.VetViewModel
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status


class VetFragment : BaseFragment() {

    private var _binding: FragmentVetBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: VetViewModel


    override var bottomNavigationViewVisibility = View.VISIBLE
    override var dashboardClicked: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentVetBinding.inflate(inflater, container, false)
        val view = binding.root
        setupViewModel()
        setUpObserver()

        return view

    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(VetViewModel::class.java)
    }

    private fun setUpObserver() {

        viewModel.getVetPageData().observe(viewLifecycleOwner, {

            when (it.status) {
                Status.LOADING -> {
//                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {

//                    binding.progressBar.visibility = View.GONE
//
                    val pageDataFields = it.data?.fields
//
                    binding.titleTv.text =
                        getLocalizedString(Constants.registerTitle, pageDataFields)
                    binding.descriptionTv.text =
                        getLocalizedString(Constants.registerSubTitle, pageDataFields)
                }
            }


        })

        viewModel.getVetInfo().observe(viewLifecycleOwner, {

            when (it.status) {
                Status.LOADING -> {
//                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {

                    binding.vetRecycler.layoutManager = LinearLayoutManager(requireContext())
                    val adapter =
                        it.data?.let { it1 ->
                            VetAdapter(findNavController(), requireContext(),
                                it1
                            )
                        }
                    binding.vetRecycler.adapter = adapter





//                    binding.progressBar.visibility = View.GONE

                }
            }

        })
    }


    override fun setUpViews() {
        super.setUpViews()

    }

}