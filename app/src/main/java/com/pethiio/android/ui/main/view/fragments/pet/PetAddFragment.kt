package com.pethiio.android.ui.main.view.fragments.pet

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.pethiio.android.R
import com.pethiio.android.data.model.PetAdd
import com.pethiio.android.data.model.PetEdit
import com.pethiio.android.databinding.FragmentPetAddBinding
import com.pethiio.android.ui.base.RegisterBaseFragment
import com.pethiio.android.ui.main.adapter.CharacterAdapter
import com.pethiio.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pethiio.android.utils.CommonMethods
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.PreferenceHelper
import com.pethiio.android.utils.Status
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.common_rounded_input_tv.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_pet_add.view.*

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
    var selectedCharacters: ArrayList<String> = ArrayList()
    var isRadioButtonAdded = false
    var fromRegister: Boolean = true
    private var petId: String? = ""
    private var isNewPet = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ResourceType")
    override fun setUpViews() {
        super.setUpViews()

        setUpObserver()
        fetchPetAddPageData()

        if (!TextUtils.isEmpty(petId)) {

            petId?.let {
                viewModel.getUserPetDetail(it)
            }
        } else {
            fetchAddAnimalDetail("1")

        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    if (fromRegister) {
                        if (PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn == true)
                            if (findNavController().currentDestination?.id == R.id.navigation_pet_add)
                                findNavController().navigate(R.id.navigation_welcome)
                    } else {
                        findNavController().navigateUp()

//                        findNavController().popBackStack()
                    }
                }

            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.backBtn.setOnClickListener {

            if (PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn == false) {
                if (findNavController().currentDestination?.id == R.id.navigation_pet_add)
                    findNavController().navigate(R.id.navigation_welcome)
            } else {
                findNavController().navigateUp()
            }
        }
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

        if (petId.equals(""))
            petId = arguments?.getString("animalId", "")

        fromRegister = arguments?.getBoolean("from", true) == true

        if (fromRegister) {
            binding.backBtn.visibility = View.GONE
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("petId")
            ?.observe(viewLifecycleOwner) {
                petId = it
                if (isNewPet)
                    setUpViews()
            }

        binding.skipBtn.setOnClickListener {

            val validName = getViewError(
                binding.nameLy.placeholderTv,
                getLocalizedString(Constants.nameEmptyEror)
            )

            if (!validName)
                return@setOnClickListener

            if (binding.yearLy.spinner.selectedItem == null) {
                CommonMethods.onSNACK(
                    binding.root,
                    getLocalizedString(Constants.yearEmtpyError)
                )
                return@setOnClickListener
            }
            if (binding.monthLy.spinner.selectedItem == null) {
                CommonMethods.onSNACK(
                    binding.root,
                    getLocalizedString(Constants.monthEmtpyError)
                )
                return@setOnClickListener
            }
            if (binding.genderLy.spinner.selectedItem == null) {
                CommonMethods.onSNACK(
                    binding.root,
                    getLocalizedString(Constants.genderEmtpyError)
                )
                return@setOnClickListener
            }
            if (binding.typeLy.spinner.selectedItem == null) {
                CommonMethods.onSNACK(
                    binding.root,
                    getLocalizedString(Constants.typeEmtpyError)
                )
                return@setOnClickListener
            }
            if (binding.breedLy.spinner.selectedItem == null) {
                CommonMethods.onSNACK(
                    binding.root,
                    getLocalizedString(Constants.breedEmtpyError)
                )
                return@setOnClickListener
            }
            if (binding.colorLy.spinner.selectedItem == null) {
                CommonMethods.onSNACK(
                    binding.root,
                    getLocalizedString(Constants.colorEmtpyError)
                )
                return@setOnClickListener
            }

            val validAbout = getViewError(
                binding.aboutPlaceholderTv,
                getLocalizedString(Constants.aboutEmtpyError)
            )
            if (!validAbout)
                return@setOnClickListener


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


            var selectedPersonalities: List<Int>
            adapterCharacter?.getSelectedItems()?.let { it1 ->
                getSelectedAnimalPersonality(
                    it1
                )
            }!!.also { selectedPersonalities = it }


            var purpose = ""
            val purposeId = binding.radioGroup.checkedRadioButtonId
            if (purposeId != -1) {
                val selectedRadioButton =
                    binding.radioGroup.findViewById<RadioButton>(purposeId)

                if (selectedRadioButton != null)
                    purpose = getLookUpKey(
                        Constants.lookUpPurpose,
                        selectedRadioButton.text.toString()
                    )
            }



            if (breedId != null && animalId != null && !TextUtils.isEmpty(
                    purpose
                )
            ) {
                if (!TextUtils.isEmpty(petId)) {
                    val id = petId?.toIntOrNull()
                    if (id != null) {
                        PetEdit(
                            id = id,
                            about = binding.aboutPlaceholderTv.text.toString(),
                            animalId =
                            animalId,
                            breedId = breedId,
                            animalPersonalities = selectedPersonalities as ArrayList<Int>,
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
                            postPetEdit(
                                it2
                            )
                        }
                    }
                } else {
                    PetAdd(
                        about = binding.aboutPlaceholderTv.text.toString(),
                        animalId =
                        animalId,
                        breedId = breedId,
                        animalPersonalities = selectedPersonalities as ArrayList<Int>,
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
                if (TextUtils.isEmpty(purpose))
                    CommonMethods.onSNACK(binding.root, getString(R.string.select_purpose))
                else
                    CommonMethods.onSNACK(binding.root, getString(R.string.select_character))
            }

        }
        return view

    }

    @SuppressLint("ResourceType")
    fun setUpObserver() {

        viewModel.getPetAddPageData().observe(viewLifecycleOwner, { it ->
            when (it.status) {
                Status.LOADING -> {
                    binding.progressAvi.show()
                }
                Status.ERROR -> {
                    binding.progressAvi.hide()
                }
                Status.SUCCESS -> {

                    binding.progressAvi.hide()

                    setPethiioResponseList(it.data?.fields)
                    it.data?.lookups?.let { it1 -> setLookUps(it1) }
                    binding.petAddTitle.text = getLocalizedString(Constants.petaboutTitle)
                    binding.skipBtn.text =
                        getLocalizedString(Constants.animalAddNextButtonTitle)
                    binding.nameLy.titleTv.text = getLocalizedSpan(Constants.registerNameTitle)
                    binding.nameLy.placeholderTv.hint =
                        getLocalizedString(Constants.registerNamePlaceholder)
                    binding.yearLy.titleTv.text = getLocalizedSpan(Constants.animalAddYearTitle)
                    binding.monthLy.titleTv.visibility = View.INVISIBLE
                    binding.genderLy.titleTv.text =
                        getLocalizedSpan(Constants.animalAddGenderTitle)
                    binding.typeLy.titleTv.text = getLocalizedSpan(Constants.animalAddTypeTitle)
                    binding.breedLy.titleTv.text =
                        getLocalizedSpan(Constants.animalAddBreedTitle)
                    binding.colorLy.titleTv.text =
                        getLocalizedSpan(Constants.animalAddColorTitle)
                    binding.characterTitleTv.text =
                        getLocalizedString(Constants.animalAddCharacterTitle)
                    binding.aboutTitleTv.text = getLocalizedSpan(Constants.petaboutTitle)
                    binding.aboutPlaceholderTv.hint =
                        getLocalizedString(Constants.petaboutPlaceholder)
                    binding.purposeTitleTv.text =
                        getLocalizedSpan(Constants.animalAddPurposeTitle)
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

                    binding.mainLayout.visibility = View.VISIBLE

                    //region LookUps

                    gender = getLookUps(Constants.lookUpGender)
                    type = getLookUps(Constants.lookUpAnimals)
                    color = getLookUps(Constants.lookUpColor)
                    purposeList = getLookUps(Constants.lookUpPurpose)

                    binding.radioGroup.removeAllViews()

                    purposeList.forEachIndexed { index, s ->
                        addRadioButton(s, index)
                    }
                    if (purposeList.isNotEmpty())
                        if (binding.radioGroup.getChildAt(0) != null && TextUtils.isEmpty(petId))
                            binding.radioGroup.check(0)

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
                    for (i in 0..12) {
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

                    if (!TextUtils.isEmpty(petId))
                        viewModel.getUserPetDetail().observe(viewLifecycleOwner, { it ->
                            when (it.status) {
                                Status.ERROR -> {
                                    it.message?.let { it1 ->
                                        CommonMethods.onSNACK(
                                            binding.root,
                                            it1
                                        )
                                    }
                                    binding.progressAvi.hide()

                                }
                                Status.LOADING -> {
                                    binding.progressAvi.show()

                                }
                                Status.SUCCESS -> {
                                    val petAdd = it.data
                                    binding.nameLy.placeholderTv.setText(petAdd?.name)
                                    binding.aboutPlaceholderTv.setText(petAdd?.about)

                                    val genderIndex = petAdd?.gender?.let { it1 ->
                                        getLookUpIndex(
                                            Constants.lookUpGender,
                                            it1
                                        )
                                    }
                                    genderIndex?.let { it1 ->
                                        binding.genderLy.spinner.setSelection(
                                            it1
                                        )
                                    }

                                    val colorIndex = petAdd?.color?.let { it1 ->
                                        getLookUpIndex(
                                            Constants.lookUpColor,
                                            it1
                                        )
                                    }
                                    colorIndex?.let { it1 ->
                                        binding.colorLy.spinner.setSelection(
                                            it1
                                        )
                                    }

                                    val animalIndex = petAdd?.animalId?.let { it1 ->
                                        getLookUpIndex(
                                            Constants.lookUpAnimals,
                                            it1.toString()
                                        )
                                    }
                                    animalIndex?.let { it1 ->
                                        binding.typeLy.spinner.setSelection(
                                            it1
                                        )
                                    }
                                    //region Animal Details
                                    viewModel.getAddAnimalDetails().observe(viewLifecycleOwner,
                                        { animalDetailResponse ->
                                            setAnimalDetail(animalDetailResponse)

                                            val breedIndex = petAdd?.breedId?.let { it1 ->
                                                getBreedIndex(
                                                    it1.toString()
                                                )
                                            }
                                            Handler().postDelayed({
                                                breedIndex?.let { it1 ->
                                                    binding.breedLy.spinner.setSelection(
                                                        it1
                                                    )
                                                }
                                            }, 200)


                                            binding.characterRc.layoutManager =
                                                GridLayoutManager(requireContext(), 3)

                                            adapterCharacter = CharacterAdapter(
                                                requireContext(),
                                                getAnimalPersonalities(),
                                                selectedCharacters
                                            )

                                            val selectedList: ArrayList<String> = ArrayList()
                                            petAdd?.animalPersonalities?.forEach {
                                                selectedList.add(getAnimalPersonality(it.toString()))
                                            }
                                            Handler().postDelayed({
                                                adapterCharacter?.setSelectedItems(selectedList)
                                            }, 200)

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

                                    //endregion


                                    petAdd?.year?.let { it1 ->
                                        binding.yearLy.spinner.setSelection(
                                            it1
                                        )
                                    }
                                    petAdd?.month?.let { it1 ->
                                        binding.monthLy.spinner.setSelection(
                                            it1
                                        )
                                    }

                                    val purposeIndex = petAdd?.purpose?.let { it1 ->
                                        getLookUpIndex(
                                            Constants.lookUpPurpose,
                                            it1
                                        )
                                    }

                                    Handler().postDelayed({
                                        purposeIndex?.let { it1 ->
                                            binding.radioGroup.check(it1)
                                        }
                                    }, 200)

                                    binding.progressAvi.hide()

                                }

                            }

                            isNewPet = false
                        })
                    else
                        viewModel.getAddAnimalDetails().observe(viewLifecycleOwner,
                            { it ->
                                setAnimalDetail(it)

                                binding.characterRc.layoutManager =
                                    GridLayoutManager(requireContext(), 3)

                                adapterCharacter = CharacterAdapter(
                                    requireContext(),
                                    getAnimalPersonalities(),
                                    selectedCharacters
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



                    binding.progressAvi.hide()

                    //endregion


                }
            }
        })

        viewModel.getPostPetEdit().observe(viewLifecycleOwner, {

            when (it.status) {
                Status.SUCCESS -> {
                    activity?.runOnUiThread {
                        val bundle = bundleOf("petId" to petId)

                        if (findNavController().currentDestination?.id == R.id.navigation_pet_add)
                            findNavController().navigate(
                                R.id.action_navigation_pet_add_to_navigation_photo,
                                bundle
                            )
                    }
                    binding.progressAvi.hide()

                }
                Status.ERROR -> {
                    it.message?.let { it1 ->
                        CommonMethods.onSNACK(
                            binding.root,
                            it1
                        )
                    }
                    binding.progressAvi.hide()

                }
                Status.LOADING -> {
                    binding.progressAvi.show()

                }
            }
        })

        viewModel.postPetAdd.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    activity?.runOnUiThread {

                        if (findNavController().currentDestination?.id == R.id.navigation_pet_add)
                            findNavController().navigate(
                                R.id.action_navigation_pet_add_to_navigation_photo
                            )
                    }
                    binding.progressAvi.hide()

                }
                Status.ERROR -> {
                    it.message?.let { it1 ->
                        CommonMethods.onSNACK(
                            binding.root,
                            it1
                        )
                    }
                    binding.progressAvi.hide()

                }
                Status.LOADING -> {
                    binding.progressAvi.show()

                }
            }
        })


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

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun addRadioButton(string: String, id: Int) {

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
        radioButton.id = id

        binding.radioGroup.addView(radioButton)

        if (!isRadioButtonAdded) {
            isRadioButtonAdded = true
        }
    }


}