package com.pethiio.android.ui.main.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pethiio.android.data.model.User
import com.pethiio.android.databinding.FragmentHomeBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.base.ViewModelFactory
import com.pethiio.android.ui.main.adapter.*
import com.pethiio.android.ui.main.viewmodel.MainViewModel
import com.pethiio.android.utils.Status
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment(), FilterItemClickListener {


    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: MainAdapter
    private lateinit var mainViewModel: MainViewModel

    override var bottomNavigationViewVisibility = View.VISIBLE


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupViewModel()
        setupObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root



        return view
    }

    private fun setupUI() {


        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MainAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter



        binding.animalRv.layoutManager = GridLayoutManager(requireContext(), 3)
        val adapter = FilterAdapter(
            requireContext(),
            arrayListOf("All", "Cat", "Dog"),
            this@HomeFragment
        )
        binding.animalRv.adapter = adapter


        binding.animalListRv.layoutManager = GridLayoutManager(requireContext(), 2)
        val animalAdapter = AnimalAdapter(
            requireContext(),
            arrayListOf("Lucky", "Kont", "Hera","Fluffy")
        )
        binding.animalListRv.adapter = animalAdapter


    }

    private fun setupObserver() {
        mainViewModel.getUsers().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { users -> renderList(users) }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }


    private fun renderList(users: List<User>) {
        adapter.addData(users)
        adapter.notifyDataSetChanged()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
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