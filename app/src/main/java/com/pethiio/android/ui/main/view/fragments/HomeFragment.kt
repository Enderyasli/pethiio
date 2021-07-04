package com.pethiio.android.ui.main.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pethiio.android.data.model.PetListResponse
import com.pethiio.android.data.model.User
import com.pethiio.android.databinding.FragmentHomeBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.base.ViewModelFactory
import com.pethiio.android.ui.main.adapter.*
import com.pethiio.android.ui.main.viewmodel.MainViewModel
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment(), FilterItemClickListener {


    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: MainAdapter
    private lateinit var viewModel: MainViewModel

    override var bottomNavigationViewVisibility = View.VISIBLE
    override var dashboardClicked: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        setupViewModel()
        setupUI()
        setupObserver()



        return view
    }

    private fun setupUI() {


//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        adapter = MainAdapter(arrayListOf())
//        recyclerView.addItemDecoration(
//            DividerItemDecoration(
//                recyclerView.context,
//                (recyclerView.layoutManager as LinearLayoutManager).orientation
//            )
//        )
//        recyclerView.adapter = adapter


//        binding.animalRv.layoutManager = GridLayoutManager(requireContext(), 3)
//        val adapter = FilterAdapter(
//            requireContext(),
//            arrayListOf("All", "Cat", "Dog"),
//            this@HomeFragment
//        )
//        binding.animalRv.adapter = adapter

        viewModel.getPetList().observe(viewLifecycleOwner, {

            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility=View.VISIBLE

                }
                Status.SUCCESS -> {

                    if (it.data != null) {
                        binding.animalListRv.layoutManager = GridLayoutManager(requireContext(), 2)
                        val adapter = HomePetListAdapter(requireContext(), it.data)
                        binding.animalListRv.adapter = adapter
                    }


                    binding.progressBar.visibility=View.GONE

                }
            }


        })


    }

    private fun setupObserver() {

    }


    private fun renderList(users: List<User>) {
        adapter.addData(users)
        adapter.notifyDataSetChanged()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory()
        ).get(MainViewModel::class.java)
    }

    override fun onFilterItemClick(item: String?) {
        if (!item.equals("All")) {
            binding.noAnimalImg.visibility = View.VISIBLE
            binding.noAnimalTv.visibility = View.VISIBLE
            binding.animalListRv.visibility = View.GONE
        } else {
            binding.noAnimalImg.visibility = View.GONE
            binding.noAnimalTv.visibility = View.GONE
            binding.animalListRv.visibility = View.VISIBLE

        }
    }


}