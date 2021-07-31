package com.pethiio.android.ui.main.view.user

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.pethiio.android.R
import com.pethiio.android.data.model.user.UserEditRequest
import com.pethiio.android.databinding.FragmentUserEditBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.viewmodel.UserDetailViewModel
import com.pethiio.android.utils.*
import com.theartofdev.edmodo.cropper.CropImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*


class UserEditFragment : BaseFragment(), AdapterView.OnItemSelectedListener {

    override var bottomNavigationViewVisibility = View.GONE
    private var _binding: FragmentUserEditBinding? = null

    private var formattedString = ""
    private lateinit var viewModel: UserDetailViewModel
    lateinit var gender: List<String>


    private val binding get() = _binding!!

    var profileUri: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun setUpViewmodel() {
        viewModel = ViewModelProviders.of(this).get(UserDetailViewModel::class.java)
        viewModel.fetchUserEditPageData()
        viewModel.fetchUserDetail()
    }

    private fun setImage(
        uri: String,
        imageView: ImageView,
    ) {

        Glide.with(requireContext())
            .load(uri)
            .into(imageView)



        Glide.with(imageView)
            .asBitmap().load(uri).into(object : SimpleTarget<Bitmap?>() {

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {

                    val filesDir = requireActivity().filesDir.toString() + "/profileImage"
                    profileUri = CommonFunctions.saveImage(filesDir, "profile.png", resource)


                }
            })


    }

    override fun setUpViews() {
        super.setUpViews()


        viewModel.getUserEditPageData().observe(this, { it ->

            when (it.status) {

                Status.SUCCESS -> {
                    val pageDataFields = it.data?.fields
                    val lookUps = it.data?.lookups
                    lookUps?.let { it1 -> setLookUps(it1) }
                    binding.signupTitle.text =
                        getLocalizedString(Constants.registerTitle, pageDataFields)
                    binding.signupDescription.text =
                        getLocalizedString(Constants.registerSubTitle, pageDataFields)
                    binding.nameLy.titleTv.text =
                        getLocalizedString(Constants.registerNameTitle, pageDataFields)
                    binding.surnameLy.titleTv.text =
                        getLocalizedString(Constants.registerSurnameTitle, pageDataFields)
                    binding.birthTitleTv.text =
                        getLocalizedSpan(Constants.registerDateOfBirthTitle, pageDataFields)
                    binding.aboutTitleTv.text =
                        getLocalizedSpan(Constants.registerAboutMe, pageDataFields)
                    binding.aboutPlaceholderTv.hint =
                        getLocalizedString(Constants.registerAboutMePlaceholder, pageDataFields)
                    binding.birthPlaceholderTv.hint =
                        getLocalizedString(Constants.registerDateOfBirthPlaceholder, pageDataFields)
                    binding.genderLy.titleTv.text =
                        getLocalizedSpan(Constants.registerGenderTitle, pageDataFields)
                    binding.genderLy.spinner.prompt =
                        getLocalizedString(Constants.registerGenderTitle, pageDataFields)
                    binding.saveBtn.text =
                        getLocalizedString(
                            Constants.saveButtonTitle,
                            pageDataFields
                        )

                    val gender = getLookUps(Constants.lookUpGender, lookUps)
                    val genderAdapter =
                        ArrayAdapter(
                            requireContext(),
                            R.layout.spinner_item_default_selected,
                            gender
                        )
                    genderAdapter.setDropDownViewResource(R.layout.spinner_item_default)


                    with(binding.genderLy.spinner)
                    {
                        adapter = genderAdapter
                        onItemSelectedListener = this@UserEditFragment
                        gravity = Gravity.CENTER

                    }

                    viewModel.getUserDetail().observe(this, {

                        when (it.status) {

                            Status.LOADING -> {

                            }
                            Status.ERROR -> {

                            }

                            Status.SUCCESS -> {
                                val response = it.data

//                                Glide.with(requireContext())
//                                    .load(response?.avatar)
//                                    .into(binding.imageProfile)

                                response?.avatar?.let { it1 -> setImage(it1, binding.imageProfile) }

                                binding.nameLy.placeholderTv.setText(response?.firstName)
                                binding.surnameLy.placeholderTv.setText(response?.lastName)
                                binding.aboutPlaceholderTv.setText(response?.aboutMe)
                                binding.birthPlaceholderTv.text = response?.dateOfBirth


                                var convertedDate = Date()
                                try {
                                    convertedDate =
                                        Constants.datePickerFormatTv.parse(response?.dateOfBirth)
                                    formattedString =
                                        Constants.datePickerFormat.format(convertedDate)

                                } catch (e: Exception) {

                                }

                                val genderIndex = response?.gender?.let { it1 ->
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

                            }
                        }
                    })


                    binding.saveBtn.setOnClickListener {

                        val validSpinner = binding.genderLy.spinner.selectedItem != null

                        var validName = getViewError(
                            binding.nameLy.placeholderTv,
                            getLocalizedString(Constants.nameEmptyEror, pageDataFields)
                        )
                        if (!validName)
                            return@setOnClickListener

                        var validLastName = getViewError(
                            binding.surnameLy.placeholderTv,
                            getLocalizedString(Constants.surnameEmtpyError, pageDataFields)
                        )
                        if (!validLastName)
                            return@setOnClickListener


                        if (TextUtils.isEmpty(binding.birthPlaceholderTv.text.trim())) {
                            binding.birthPlaceholderTv.error =
                                getLocalizedString(Constants.dateOfBirthEmtpyError, pageDataFields)
                            return@setOnClickListener
                        } else
                            binding.birthPlaceholderTv.error = null

                        val valid = getViewError(
                            binding.aboutPlaceholderTv,
                            getLocalizedString(Constants.aboutMeEmtpyError, pageDataFields)
                        )

                        if (valid && !validSpinner) {
                            CommonMethods.onSNACK(
                                binding.root,
                                getLocalizedString(Constants.genderEmtpyError, pageDataFields)
                            )
                            return@setOnClickListener

                        }
                        if (TextUtils.isEmpty(profileUri)) {
                            CommonMethods.onSNACK(
                                binding.root,
                                getLocalizedString(Constants.imageEmtpyError, pageDataFields)
                            )
                            return@setOnClickListener

                        }

                        if (validSpinner && valid) {

                            viewModel.postUserEdit(
                                UserEditRequest(
                                    aboutMe = binding.aboutPlaceholderTv.text.trim().toString(),
                                    dateOfBirth = formattedString,
                                    gender = getLookUpKey(
                                        Constants.lookUpGender,
                                        binding.genderLy.spinner.selectedItem.toString()
                                    ),
                                    firstName = binding.nameLy.placeholderTv.text.trim().toString(),
                                    lastName = binding.surnameLy.placeholderTv.text.trim()
                                        .toString(),
                                )
                            )
                        }

                viewModel.getUserEditResponse().observe(viewLifecycleOwner, {
                    when (it.status) {
                        Status.SUCCESS -> {
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

                                    viewModel.postUserAvatar(filePart)
                                    viewModel.getUserAvatarResponse().observe(
                                        viewLifecycleOwner,
                                        { it1 ->
                                            when (it1.status) {
                                                Status.SUCCESS -> {
                                                    activity?.runOnUiThread {

                                                        // TODO: 31.07.2021 ansayfaya dönke
                                                    }

                                                }
                                                Status.ERROR -> {
                                                    CommonMethods.onSNACK(
                                                        binding.root,
                                                        it1.message.toString()
                                                    )


                                                }
                                                Status.LOADING -> {
                                                }
                                            }
                                        })

                                } else {
                                    CommonMethods.onSNACK(
                                        binding.root,
                                        getLocalizedString(Constants.imageEmtpyError,pageDataFields)
                                    )


                                }

                            }

                        }
                        Status.ERROR -> {
                            CommonMethods.onSNACK(binding.root, it.message.toString())

                        }
                        Status.LOADING -> {
                        }
                    }
                })

            }

                    }

                }



        })






        binding.birthPlaceholderTv.setOnClickListener {

            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)

            val startDate = Calendar.getInstance()
            startDate.set(1900, 0, 1)
            val endDate = Calendar.getInstance()
            endDate.add(Calendar.YEAR, -18)
            //TimePicker
            val pvTime = TimePickerBuilder(requireContext()) { date, _ ->
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
                .setCancelColor(ContextCompat.getColor(requireContext(), R.color.orangeButton))
                .setSubmitColor(ContextCompat.getColor(requireContext(), R.color.orangeButton))
                .build()

            pvTime.show()
        }

    }

    fun postRegisterDetail() {

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserEditBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.imagelY.setOnClickListener {

            // for fragment (DO NOT use `getActivity()`)
            CropImage.activity()
                .setAspectRatio(1, 1)
                .start(requireContext(), this)
        }

        setUpViewmodel()

        return view

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == -1) {
                val resultUri: Uri = result.uri

                profileUri = resultUri.path.toString()

                Glide.with(requireContext())
                    .load(resultUri)
                    .into(binding.imageProfile)


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}

    override fun onNothingSelected(parent: AdapterView<*>?) {}


}