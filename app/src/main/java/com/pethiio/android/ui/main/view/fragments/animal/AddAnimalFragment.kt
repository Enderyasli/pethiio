package com.pethiio.android.ui.main.view.fragments.animal

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.pethiio.android.R
import com.pethiio.android.data.model.PetAdd
import com.pethiio.android.databinding.FragmentAddAnimalBinding
import com.pethiio.android.ui.base.RegisterBaseFragment
import com.pethiio.android.ui.main.adapter.CharacterAdapter
import com.pethiio.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status
import kotlinx.android.synthetic.main.common_rounded_input_tv.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.properties.Delegates


class AddAnimalFragment : RegisterBaseFragment<RegisterBaseViewModel>(),
    AdapterView.OnItemSelectedListener {

    override var bottomNavigationViewVisibility = View.GONE
    private var _binding: FragmentAddAnimalBinding? = null

    private val binding get() = _binding!!
    override var useSharedViewModel = true

    lateinit var gender: List<String>
    lateinit var type: List<String>
    var breed = arrayOf("Labrador", "Doberman")
    lateinit var color: List<String>
    private val GENDER_ID = 1
    private val TYPE_ID = 2
    private val BREED_ID = 3
    private val COLOR_ID = 4


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
            binding.monthLy.titleTv.text = getLocalizedString(Constants.animalAddMonthTitle)
            binding.genderLy.titleTv.text = getLocalizedSpan(Constants.animalAddGenderTitle)
            binding.typeLy.titleTv.text = getLocalizedSpan(Constants.animalAddTypeTitle)
            binding.breedLy.titleTv.text = getLocalizedSpan(Constants.animalAddBreedTitle)
            binding.colorLy.titleTv.text = getLocalizedSpan(Constants.animalAddColorTitle)
            binding.characterTitleTv.text = getLocalizedString(Constants.animalAddCharacterTitle)


        })
        viewModel.getAddAnimalDetails().observe(this, {
            setAnimalDetail(it)

            binding.characterRc.layoutManager = GridLayoutManager(requireContext(), 3)

            val adapterCharacter = CharacterAdapter(
                requireContext(),
                getAnimalPersonalities()
            )

            binding.characterRc.adapter = adapterCharacter

            val breedAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                getAnimalBreeds()
            )
            breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            with(binding.breedLy.spinner)
            {
                adapter = breedAdapter
                setSelection(0, false)
                onItemSelectedListener = this@AddAnimalFragment
                gravity = Gravity.CENTER

            }
        })

        viewModel.getAddAnimalLookUps().observe(this, {

            setLookUps(it)
            gender = getLookUps(Constants.lookUpGender)
            type = getLookUps(Constants.lookUpAnimals)
            color = getLookUps(Constants.lookUpColor)

            val genderAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    gender
                )
            genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            val typeAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    type
                )
            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


            val colorAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    color
                )
            val array = arrayListOf<String>()
            for (i in 0..20) {
                array.add(i.toString())
            }
            val yearAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    array

                )
            colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.genderLy.spinner.id = GENDER_ID
            binding.typeLy.spinner.id = TYPE_ID
            binding.breedLy.spinner.id = BREED_ID
            binding.colorLy.spinner.id = COLOR_ID
            binding.genderLy.spinner.onItemSelectedListener = this
            binding.typeLy.spinner.onItemSelectedListener = this

            binding.genderLy.spinner.onItemSelectedListener = this@AddAnimalFragment


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


            with(binding.colorLy.spinner)
            {
                adapter = colorAdapter
                setSelection(0, false)
                onItemSelectedListener = this@AddAnimalFragment
                gravity = Gravity.CENTER

            }

            with(binding.yearLy.spinner)
            {
                adapter = yearAdapter
                setSelection(0, false)
                onItemSelectedListener = this@AddAnimalFragment
                gravity = Gravity.CENTER

            }

            binding.genderLy.spinner

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

            val breedId = getBreedsKey(
                binding.breedLy.spinner.selectedItem.toString()
            ).toIntOrNull()

            val animalId = getLookUpKey(
                Constants.lookUpAnimals,
                binding.typeLy.spinner.selectedItem.toString()
            ).toIntOrNull()

            if (breedId != null && animalId != null)
                postPetAdd(
                    PetAdd(
                        about = "test",
                        animalId =
                        animalId,
                        breedId = breedId,
                        animalPersonalities = arrayListOf(1),
                        color = getLookUpKey(
                            Constants.lookUpColor,
                            binding.colorLy.spinner.selectedItem.toString()
                        ),
                        gender = getLookUpKey(
                            Constants.lookUpGender,
                            binding.genderLy.spinner.selectedItem.toString()
                        ),
                        month = 1,
                        name = "string",
                        purpose = "ADOPTION",
                        year = 20
                    )
                )
            viewModel.postPetAdd.observe(viewLifecycleOwner, {
                when (it.status) {
                    Status.SUCCESS -> {
                        activity?.runOnUiThread {
                            if (findNavController().currentDestination?.id == R.id.navigation_add_animal)
                                findNavController().navigate(R.id.action_navigation_add_animal_to_navigation_photo)
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                    }
                }
            })
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
        when (parent?.id) {
            1 -> {
            }
            2 -> {
                fetchAddAnimalDetail(
                    getLookUpKey(
                        Constants.lookUpAnimals,
                        binding.typeLy.spinner.selectedItem.toString()
                    )
                )
            }
            3 -> {
            }
            4 -> {
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


}