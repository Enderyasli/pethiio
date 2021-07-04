package com.pethiio.android.ui.main.view.fragments.pet

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.pethiio.android.R
import com.pethiio.android.data.model.PetAdd
import com.pethiio.android.databinding.FragmentPetAddBinding
import com.pethiio.android.ui.base.RegisterBaseFragment
import com.pethiio.android.ui.main.adapter.CharacterAdapter
import com.pethiio.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.common_rounded_input_tv.view.*
import kotlinx.android.synthetic.main.fragment_home.*


class PetAddFragment : RegisterBaseFragment<RegisterBaseViewModel>(),
    AdapterView.OnItemSelectedListener {

    override var bottomNavigationViewVisibility = View.GONE
    private var _binding: FragmentPetAddBinding? = null

    private val binding get() = _binding!!
    override var useSharedViewModel = true

    lateinit var gender: List<String>
    lateinit var type: List<String>
    lateinit var color: List<String>
    lateinit var purposeList: List<String>
    private val GENDER_ID = 1
    private val TYPE_ID = 2
    private val BREED_ID = 3
    private val COLOR_ID = 4
     var adapterCharacter: CharacterAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    @SuppressLint("ResourceType")
    override fun setUpViews() {
        super.setUpViews()

        viewModel.getAddAnimalFields().observe(this, {


            setPethiioResponseList(it)
            binding.petAddTitle.text = getLocalizedString(Constants.petaboutTitle)

            binding.skipBtn.text = getLocalizedString(Constants.animalAddNextButtonTitle)
            binding.nameLy.titleTv.text = getLocalizedSpan(Constants.registerNameTitle)
            binding.nameLy.placeholderTv.hint =
                getLocalizedString(Constants.registerNamePlaceholder)


            binding.yearLy.titleTv.text = getLocalizedSpan(Constants.animalAddYearTitle)
            binding.monthLy.titleTv.visibility = View.INVISIBLE
            binding.genderLy.titleTv.text = getLocalizedSpan(Constants.animalAddGenderTitle)
            binding.typeLy.titleTv.text = getLocalizedSpan(Constants.animalAddTypeTitle)
            binding.breedLy.titleTv.text = getLocalizedSpan(Constants.animalAddBreedTitle)
            binding.colorLy.titleTv.text = getLocalizedSpan(Constants.animalAddColorTitle)
            binding.characterTitleTv.text = getLocalizedSpan(Constants.animalAddCharacterTitle)
            binding.aboutTitleTv.text = getLocalizedSpan(Constants.petaboutTitle)
            binding.aboutPlaceholderTv.hint =
                getLocalizedString(Constants.petaboutPlaceholder)

            binding.purposeTitleTv.text = getLocalizedSpan(Constants.animalAddCharacterTitle)


            binding.genderLy.spinner.prompt =
                getLocalizedString(Constants.registerGenderTitle)
            binding.yearLy.spinner.prompt =
                getLocalizedString(Constants.animalAddYearTitle)
            binding.monthLy.spinner.prompt =
                getLocalizedString(Constants.animalAddMonthPlaceholder)
            binding.typeLy.spinner.prompt =
                getLocalizedString(Constants.animalAddTypeTitle)
            binding.breedLy.spinner.prompt =
                getLocalizedString(Constants.animalAddBreedTitle)
            binding.colorLy.spinner.prompt =
                getLocalizedString(Constants.animalAddColorTitle)

            binding.mainLayout.visibility=View.VISIBLE

        })
        viewModel.getAddAnimalDetails().observe(this, {
            setAnimalDetail(it)

            binding.characterRc.layoutManager = GridLayoutManager(requireContext(), 3)

            adapterCharacter = CharacterAdapter(
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
                onItemSelectedListener = this@PetAddFragment
                gravity = Gravity.CENTER

            }
        })

        viewModel.getAddAnimalLookUps().observe(this, { it ->

            setLookUps(it)
            gender = getLookUps(Constants.lookUpGender)
            type = getLookUps(Constants.lookUpAnimals)
            color = getLookUps(Constants.lookUpColor)
            purposeList = getLookUps(Constants.lookUpPurpose)

            binding.radioGroup.removeAllViews()
            purposeList.forEach {
                addRadioButton(it)
            }
            if (purposeList.isNotEmpty())
                binding.radioGroup.check(1)

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
            val arrayYear = arrayListOf<String>()
            for (i in 0..20) {
                arrayYear.add(i.toString())
            }
            val arrayMonth = arrayListOf<String>()
            for (i in 1..12) {
                arrayMonth.add(i.toString())
            }
            val yearAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    arrayYear
                )
            yearAdapter.setDropDownViewResource(R.layout.spinner_item_default)

            val monthAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    arrayMonth

                )
            monthAdapter.setDropDownViewResource(R.layout.spinner_item_default)

            colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.genderLy.spinner.id = GENDER_ID
            binding.typeLy.spinner.id = TYPE_ID
            binding.breedLy.spinner.id = BREED_ID
            binding.colorLy.spinner.id = COLOR_ID
            binding.genderLy.spinner.onItemSelectedListener = this
            binding.typeLy.spinner.onItemSelectedListener = this

            binding.genderLy.spinner.onItemSelectedListener = this@PetAddFragment


            with(binding.genderLy.spinner)
            {
                adapter = genderAdapter
                onItemSelectedListener = this@PetAddFragment
                gravity = Gravity.CENTER

            }

            with(binding.typeLy.spinner)
            {
                adapter = typeAdapter
                onItemSelectedListener = this@PetAddFragment
                gravity = Gravity.CENTER

            }


            with(binding.colorLy.spinner)
            {
                adapter = colorAdapter
                onItemSelectedListener = this@PetAddFragment
                gravity = Gravity.CENTER

            }

            with(binding.yearLy.spinner)
            {
                adapter = yearAdapter
                onItemSelectedListener = this@PetAddFragment
                gravity = Gravity.CENTER

            }
            with(binding.monthLy.spinner)
            {
                adapter = monthAdapter
                onItemSelectedListener = this@PetAddFragment
                gravity = Gravity.CENTER

            }


        })

    }

    fun addRadioButton(string: String) {

        val radioButton = RadioButton(requireContext())

        radioButton.text = string
        val typeface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            resources.getFont(R.font.typo_round_regular)
        } else {
            ResourcesCompat.getFont(requireContext(), R.font.typo_round_regular)
        }


        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_enabled)
            ), intArrayOf(
                ContextCompat.getColor(requireContext(), R.color.grey),  //disabled
                ContextCompat.getColor(requireContext(), R.color.orangeButton) //enabled
            )
        )

        radioButton.buttonTintList = colorStateList

        radioButton.textSize = 14F
        radioButton.typeface = typeface
        radioButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))

        binding.radioGroup.addView(radioButton)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetAddBinding.inflate(inflater, container, false)
        val view = binding.root



        binding.skipBtn.setOnClickListener {


            var breedId: Int? = null

            if (binding.breedLy.spinner.selectedItem != null)
                breedId = getBreedsKey(
                    binding.breedLy.spinner.selectedItem.toString()
                ).toIntOrNull()


            var animalId: Int? = null

            if (binding.typeLy.spinner.selectedItem != null)
                animalId = getLookUpKey(
                    Constants.lookUpAnimals,
                    binding.typeLy.spinner.selectedItem.toString()
                ).toIntOrNull()


            var selectedPersonalities: ArrayList<Int>? = null
                selectedPersonalities =
                    adapterCharacter?.getSelectedItems()?.let { it1 ->
                        getSelectedAnimalPersonality(
                            it1
                        )
                    }


            var purpose: String = ""
            val purposeId = binding.radioGroup.checkedRadioButtonId
            if (purposeId != -1) {
                val selectedRadioButton = binding.radioGroup.findViewById<RadioButton>(purposeId)

                purpose = getLookUpKey(
                    Constants.lookUpPurpose,
                    selectedRadioButton.text.toString()
                )
            }



            if (breedId != null && animalId != null && binding.monthLy.spinner.selectedItem != null
                && binding.genderLy.spinner.selectedItem != null && binding.yearLy.spinner.selectedItem != null
                && !TextUtils.isEmpty(binding.nameLy.placeholderTv.text) && !TextUtils.isEmpty(
                    binding.aboutPlaceholderTv.text
                )
                && !TextUtils.isEmpty(purpose)
            ) {
                if (selectedPersonalities?.size!! > 0) {
                    PetAdd(
                        about = binding.aboutPlaceholderTv.text.toString(),
                        animalId =
                        animalId,
                        breedId = breedId,
                        animalPersonalities = selectedPersonalities!!,
                        color = getLookUpKey(
                            Constants.lookUpColor,
                            binding.colorLy.spinner.selectedItem.toString()
                        ),
                        gender = getLookUpKey(
                            Constants.lookUpGender,
                            binding.genderLy.spinner.selectedItem.toString()
                        ),
                        month = binding.monthLy.spinner.selectedItem.toString().toInt(),
                        name = binding.nameLy.placeholderTv.text.toString(),
                        purpose = purpose,
                        year = binding.yearLy.spinner.selectedItem.toString().toInt()
                    ).let { it2 ->
                        postPetAdd(
                            it2
                        )
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Tüm alanları doldurunuz!", Toast.LENGTH_LONG)
                    .show()
            }

            viewModel.postPetAdd.observe(viewLifecycleOwner, {
                when (it.status) {
                    Status.SUCCESS -> {
                        activity?.runOnUiThread {
                            if (findNavController().currentDestination?.id == R.id.navigation_pet_add)
                                findNavController().navigate(R.id.action_navigation_pet_add_to_navigation_photo)
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

    private fun hideKeyBoard(v: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }


}