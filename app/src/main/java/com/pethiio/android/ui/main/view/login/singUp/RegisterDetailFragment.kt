package com.pethiio.android.ui.main.view.login.singUp

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bumptech.glide.Glide
import com.pethiio.android.R
import com.pethiio.android.data.EventBus.LoginEvent
import com.pethiio.android.data.model.signup.RegisterInfo
import com.pethiio.android.databinding.FragmentRegisterDetailBinding
import com.pethiio.android.ui.base.RegisterBaseFragment
import com.pethiio.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pethiio.android.utils.CommonMethods
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.PreferenceHelper
import com.pethiio.android.utils.Status
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.util.*
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options

class RegisterDetailFragment : RegisterBaseFragment<RegisterBaseViewModel>(),
    AdapterView.OnItemSelectedListener {

    override var bottomNavigationViewVisibility = View.GONE
    private var _binding: FragmentRegisterDetailBinding? = null

    override var useSharedViewModel = true
    private var formattedString = ""


    private val binding get() = _binding!!

    var profileUri: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setUpViews() {
        super.setUpViews()

        binding.progressAvi.hide()


        viewModel.getRegisterDetailFields().observe(this, {

            setPethiioResponseList(it)
            binding.signupTitle.text = getLocalizedString(Constants.registerTitle)
            binding.signupDescription.text =
                getLocalizedString(Constants.registerSubTitle)
            binding.birthTitleTv.text =
                getLocalizedSpan(Constants.registerDateOfBirthTitle)
            binding.aboutTitleTv.text =
                getLocalizedSpan(Constants.registerAboutMe)
            binding.aboutPlaceholderTv.hint =
                getLocalizedString(Constants.registerAboutMePlaceholder)
            binding.birthPlaceholderTv.hint =
                getLocalizedString(Constants.registerDateOfBirthPlaceholder)
            binding.genderLy.titleTv.text =
                getLocalizedSpan(Constants.registerGenderTitle)
            binding.genderLy.spinner.prompt =
                getLocalizedString(Constants.registerGenderTitle)
            binding.addAnimalBtn.text =
                getLocalizedString(Constants.registerAnimalInsertButtonTitle)
            binding.goWithoutAnimalTv.text =
                getLocalizedString(Constants.registerSkipButtonTitle)

        })

        viewModel.getRegisterDetailLookUps().observe(this, {


            setLookUps(it)

            val gender = getLookUps(Constants.lookUpGender)
            val genderAdapter =
                ArrayAdapter(requireContext(), R.layout.spinner_item_default_selected, gender)
            genderAdapter.setDropDownViewResource(R.layout.spinner_item_default)


            with(binding.genderLy.spinner)
            {
                adapter = genderAdapter
                onItemSelectedListener = this@RegisterDetailFragment
                gravity = Gravity.CENTER

            }

        })

        binding.addAnimalBtn.setOnClickListener {

            val validSpinner = binding.genderLy.spinner.selectedItem != null

            if (TextUtils.isEmpty(binding.birthPlaceholderTv.text.trim())) {
                binding.birthPlaceholderTv.error =
                    getLocalizedString(Constants.dateOfBirthEmtpyError)
                return@setOnClickListener
            } else
                binding.birthPlaceholderTv.error = null

            val valid = getViewError(
                binding.aboutPlaceholderTv,
                getLocalizedString(Constants.aboutMeEmtpyError)
            )

            if (valid && !validSpinner) {
                CommonMethods.onSNACK(binding.root, getLocalizedString(Constants.genderEmtpyError))
                return@setOnClickListener

            }
//            if (TextUtils.isEmpty(profileUri)) {
//                CommonMethods.onSNACK(
//                    binding.root,
//                    getLocalizedString(Constants.imageEmtpyError)
//                )
//                return@setOnClickListener
//
//            }

            if (validSpinner && valid) {

                postRegisterInfo(
                    RegisterInfo(
                        aboutMe = binding.aboutPlaceholderTv.text.trim().toString(),
                        dateOfBirth = formattedString,
                        gender = getLookUpKey(
                            Constants.lookUpGender,
                            binding.genderLy.spinner.selectedItem.toString()
                        )
                    )
                )

                viewModel.postRegisterInfo.observe(viewLifecycleOwner, {
                    when (it.status) {
                        Status.SUCCESS -> {
                            binding.progressAvi.hide()

                            activity?.runOnUiThread {

                                if (!TextUtils.isEmpty(profileUri)) {

                                    val file = File(profileUri)
                                    val requestBody =
                                        file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                                    val filePart =
                                        MultipartBody.Part.createFormData(
                                            "file",
                                            file.name,
                                            requestBody
                                        )
                                    postRegisterAvatar(filePart)

                                    viewModel.postRegisterAvatar.observe(
                                        viewLifecycleOwner,
                                        { it1 ->
                                            when (it1.status) {
                                                Status.SUCCESS -> {
                                                    binding.progressAvi.hide()

                                                    activity?.runOnUiThread {
                                                        fetchPetAddPageData()
                                                        fetchAddAnimalDetail("1")
                                                        viewModel.getAddAnimalDetails()
                                                            .observe(viewLifecycleOwner, {
                                                                if (findNavController().currentDestination?.id == R.id.navigation_register_detail)
                                                                    findNavController().navigate(R.id.action_navigation_register_detail_to_navigation_add_animal)
                                                            })

                                                    }
                                                }

                                                Status.ERROR -> {
                                                    CommonMethods.onSNACK(
                                                        binding.root,
                                                        it1.message.toString()
                                                    )
                                                    binding.progressAvi.hide()
                                                }

                                                Status.LOADING -> {
                                                    binding.progressAvi.show()

                                                }
                                            }
                                        })

                                } else {
                                    PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn =
                                        true
                                    Handler().postDelayed({
                                        EventBus.getDefault().post(LoginEvent())
                                    }, 1000)
                                    
                                    activity?.runOnUiThread {
                                        fetchPetAddPageData()
                                        fetchAddAnimalDetail("1")
                                        viewModel.getAddAnimalDetails()
                                            .observe(viewLifecycleOwner, {
                                                if (findNavController().currentDestination?.id == R.id.navigation_register_detail)
                                                    findNavController().navigate(R.id.action_navigation_register_detail_to_navigation_add_animal)
                                            })

                                    }
                                }
                            }
                        }
                        Status.ERROR -> {
                            CommonMethods.onSNACK(binding.root, it.message.toString())
                            binding.progressAvi.hide()
                        }
                        Status.LOADING -> {
                            binding.progressAvi.show()

                        }
                    }
                })

            }

        }

        binding.goWithoutAnimalTv.setOnClickListener {

            val validSpinner = binding.genderLy.spinner.selectedItem != null

            if (TextUtils.isEmpty(binding.birthPlaceholderTv.text.trim())) {
                binding.birthPlaceholderTv.error =
                    getLocalizedString(Constants.dateOfBirthEmtpyError)
                return@setOnClickListener
            } else
                binding.birthPlaceholderTv.error = null

            val valid = getViewError(
                binding.aboutPlaceholderTv,
                getLocalizedString(Constants.aboutMeEmtpyError)
            )

            if (valid && !validSpinner) {
                CommonMethods.onSNACK(binding.root, getLocalizedString(Constants.genderEmtpyError))
                return@setOnClickListener

            }
//            if (TextUtils.isEmpty(profileUri)) {
//                CommonMethods.onSNACK(
//                    binding.root,
//                    getLocalizedString(Constants.imageEmtpyError)
//                )
//                return@setOnClickListener
//
//            }

            if (validSpinner && valid) {

                postRegisterInfo(
                    RegisterInfo(
                        aboutMe = binding.aboutPlaceholderTv.text.trim().toString(),
                        dateOfBirth = formattedString,
                        gender = getLookUpKey(
                            Constants.lookUpGender,
                            binding.genderLy.spinner.selectedItem.toString()
                        )
                    )
                )

                viewModel.postRegisterInfo.observe(viewLifecycleOwner, {
                    when (it.status) {
                        Status.SUCCESS -> {
                            binding.progressAvi.hide()

                            activity?.runOnUiThread {

                                if (!TextUtils.isEmpty(profileUri)) {

                                    val file = File(profileUri)
                                    val requestBody =
                                        file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                                    val filePart =
                                        MultipartBody.Part.createFormData(
                                            "file",
                                            file.name,
                                            requestBody
                                        )
                                    postRegisterAvatar(filePart)

                                    viewModel.postRegisterAvatar.observe(
                                        viewLifecycleOwner,
                                        { it1 ->
                                            when (it1.status) {

                                                Status.SUCCESS -> {
                                                    binding.progressAvi.hide()

                                                    PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn =
                                                        true
                                                    Handler().postDelayed({
                                                        EventBus.getDefault().post(LoginEvent())
                                                    }, 1000)
                                                    if (findNavController().currentDestination?.id == R.id.navigation_register_detail)
                                                        findNavController().navigate(R.id.action_navigation_register_detail_to_navigation_welcome)
                                                }

                                                Status.ERROR -> {
                                                    CommonMethods.onSNACK(
                                                        binding.root,
                                                        it.message.toString()
                                                    )
                                                    binding.progressAvi.hide()
                                                }
                                                Status.LOADING -> {
                                                    binding.progressAvi.show()
                                                }
                                            }
                                        })

                                } else {
                                    PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn =
                                        true
                                    Handler().postDelayed({
                                        EventBus.getDefault().post(LoginEvent())
                                    }, 1000)
                                    if (findNavController().currentDestination?.id == R.id.navigation_register_detail)
                                        findNavController().navigate(R.id.action_navigation_register_detail_to_navigation_welcome)
                                }
                            }
                        }

                        Status.LOADING -> {
                            binding.progressAvi.show()
                        }
                        Status.ERROR -> {
                            CommonMethods.onSNACK(binding.root, it.message.toString())
                            binding.progressAvi.hide()
                        }
                    }
                })

            }

        }

        binding.birthPlaceholderTv.setOnClickListener {

            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)

            val startDate = Calendar.getInstance()
            startDate.set(1900, 0, 1)
            val endDate = Calendar.getInstance()
            endDate.add(Calendar.YEAR, -18)
            //TimePicker
            val pvTime = TimePickerBuilder(requireContext()) { date, v ->
                val newDate = Constants.datePickerFormat.format(date)
                val newDateTv = Constants.datePickerFormatTv.format(date)


                formattedString = newDate.toString()
                binding.birthPlaceholderTv.text = newDateTv.toString()
                binding.birthPlaceholderTv.error = null

            }
                .setRangDate(startDate, endDate)
                .setDate(endDate)
                .setCancelText(getString(R.string.lcl_cancel_datepicker))
                .setSubmitText(getString(R.string.lcl_apply_datepicker))
                .setLabel("", "", "", "", "", "")
                .setCancelColor(ContextCompat.getColor(requireContext(), R.color.orangeButton))
                .setSubmitColor(ContextCompat.getColor(requireContext(), R.color.orangeButton))
                .build()

            pvTime.show()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        if (PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn == true) {
            if (findNavController().currentDestination?.id == R.id.navigation_register_detail)
                findNavController().navigate(R.id.action_global_navigation_welcome)
        }

        val cropImage = registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                // use the returned uri
                val uriContent = result.uriContent
                val uriFilePath = result.getUriFilePath(requireContext()) // optional usage
                profileUri = uriFilePath.toString()

                Glide.with(requireContext())
                    .load(uriContent)
                    .into(binding.imageProfile)

                binding.imagePlaceholder.visibility = View.GONE


            } else {
                val exception = result.error
            }
        }

        binding.imagelY.setOnClickListener {

            cropImage.launch(
                options {
                    setGuidelines(CropImageView.Guidelines.ON)
                    setAspectRatio(1, 1)
                    setOutputCompressQuality(50)
                }
            )

        }


        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getViewModelClass() = RegisterBaseViewModel::class.java
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


}