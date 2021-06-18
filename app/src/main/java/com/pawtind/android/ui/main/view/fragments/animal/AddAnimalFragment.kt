package com.pawtind.android.ui.main.view.fragments.animal

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.pawtind.android.R
import com.pawtind.android.databinding.FragmentAddAnimalBinding
import com.pawtind.android.ui.base.RegisterBaseFragment
import com.pawtind.android.ui.main.adapter.CharacterAdapter
import com.pawtind.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pawtind.android.utils.Constants
import kotlinx.android.synthetic.main.common_rounded_input_tv.view.*
import kotlinx.android.synthetic.main.fragment_home.*

class AddAnimalFragment : RegisterBaseFragment<RegisterBaseViewModel>(),
    AdapterView.OnItemSelectedListener {

    override var bottomNavigationViewVisibility = View.GONE
    private var _binding: FragmentAddAnimalBinding? = null

    private val binding get() = _binding!!
    override var useSharedViewModel = true

    var gender = arrayOf("Kadın", "Erkek")
    var type = arrayOf("Köpek", "Kedi", "Kuş")
    var breed = arrayOf("Labrador", "Doberman")
    var color = arrayOf("Kahve", "Siyah", "Beyaz")

    val GENDER_ID = 1
    val TYPE_ID = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setUpViews() {
        super.setUpViews()

        viewModel.getAddAnimalFields().observe(this, {


            setPawtindResponseList(it)

            binding.nameLy.titleTv.text = getLocalizedSpan(Constants.registerNameTitle)
            binding.nameLy.placeholderTv.hint =
                getLocalizedString(Constants.registerNamePlaceholder)


            binding.yearLy.titleTv.text = getLocalizedSpan(Constants.animalAddYearTitle)
            binding.yearLy.spinner.prompt="test"
            binding.monthLy.titleTv.text = getLocalizedString(Constants.animalAddMonthTitle)

            binding.genderLy.titleTv.text = getLocalizedSpan(Constants.animalAddGenderTitle)
            binding.typeLy.titleTv.text = getLocalizedSpan(Constants.animalAddTypeTitle)
            binding.breedLy.titleTv.text = getLocalizedSpan(Constants.animalAddBreedTitle)
            binding.colorLy.titleTv.text = getLocalizedSpan(Constants.animalAddColorTitle)
            binding.characterTitleTv.text = getLocalizedString(Constants.animalAddCharacterTitle)


        })

        viewModel.getAddAnimalLookUps().observe(this, {

            setLookUps(it)

            val genderAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    getLookUps("gender")
                )
            genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            val typeAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    getLookUps("animals")
                )
            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            val breedAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, breed)
            breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            val colorAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    getLookUps("color")
                )
            colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.genderLy.spinner.id = GENDER_ID

            with(binding.genderLy.spinner)
            {
                adapter = genderAdapter
                setSelection(0, false)
                onItemSelectedListener = this@AddAnimalFragment
                gravity = Gravity.CENTER

            }

            with(binding.typeLy.spinner)
            {
                adapter = typeAdapter
                setSelection(0, false)
                onItemSelectedListener = this@AddAnimalFragment
                gravity = Gravity.CENTER

            }

            with(binding.breedLy.spinner)
            {
                adapter = breedAdapter
                setSelection(0, false)
                onItemSelectedListener = this@AddAnimalFragment
                gravity = Gravity.CENTER

            }

            with(binding.colorLy.spinner)
            {
                adapter = colorAdapter
                setSelection(0, false)
                onItemSelectedListener = this@AddAnimalFragment
                gravity = Gravity.CENTER

            }

            binding.characterRc.layoutManager = GridLayoutManager(requireContext(), 3)

            var adapter = CharacterAdapter(
                requireContext(),
                arrayListOf("Item", "Item", "Item", "Item", "Item", "Item")
            )

            binding.characterRc.adapter = adapter
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAnimalBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.skipBtn.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_add_animal_to_navigation_photo)
        }

        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getViewModelClass() = RegisterBaseViewModel::class.java

    private fun showToast(
        context: Context = requireContext(),
        message: String,
        duration: Int = Toast.LENGTH_LONG
    ) {
        Toast.makeText(context, message, duration).show()
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (view?.id) {
            1 -> showToast(message = "Spinner 2 Position:${position} and language: ${gender[position]}")
            else -> {
                showToast(message = "Spinner 1 Position:${position} and language: ${gender[position]}")
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}