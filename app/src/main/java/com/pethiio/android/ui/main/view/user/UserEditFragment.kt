package com.pethiio.android.ui.main.view.user

import android.content.Context
import android.graphics.Bitmap
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
import androidx.navigation.fragment.findNavController
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.pethiio.android.R
import com.pethiio.android.data.model.user.UserEditRequest
import com.pethiio.android.databinding.FragmentUserEditBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.main.viewmodel.UserDetailViewModel
import com.pethiio.android.utils.*
import kotlinx.android.synthetic.main.fragment_vet.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
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

        binding.progressAvi.hide()

        viewModel.getUserEditPageData().observe(this, { it ->

            when (it.status) {

                Status.LOADING -> {
                    binding.progressAvi.show()
                }
                Status.ERROR -> {
                    binding.progressAvi.hide()
                }

                Status.SUCCESS -> {
                    binding.progressAvi.hide()

                    val pageDataFields = it.data?.fields
                    val lookUps = it.data?.lookups
                    lookUps?.let { it1 -> setLookUps(it1) }
                    binding.signupTitle.text =
                        getLocalizedString(Constants.registerTitle, pageDataFields)
                    binding.nameTitleTv.text =
                        getLocalizedSpan(Constants.registerNameTitle, pageDataFields)
                    binding.surnameTitleTv.text =
                        getLocalizedSpan(Constants.registerSurnameTitle, pageDataFields)
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
                                binding.progressAvi.show()
                            }
                            Status.ERROR -> {
                                binding.progressAvi.hide()
                            }

                            Status.SUCCESS -> {
                                binding.progressAvi.hide()

                                val response = it.data

//                                Glide.with(requireContext())
//                                    .load(response?.avatar)
//                                    .into(binding.imageProfile)

                                response?.avatar?.let { it1 -> setImage(it1, binding.imageProfile) }

                                binding.emailTv.text = response?.email
                                formattedString = response?.dateOfBirth.toString()

                                binding.namePlaceholderTv.setText(response?.firstName)
                                binding.surnamePlaceholderTv.setText(response?.lastName)
                                binding.aboutPlaceholderTv.setText(response?.aboutMe)
                                binding.birthPlaceholderTv.text =
                                    parseDateToddMMyyyy(formattedString)


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


                    binding.backBtn.setOnClickListener {
                        findNavController().popBackStack()
                    }
                    binding.saveBtn.setOnClickListener {

                        val validSpinner = binding.genderLy.spinner.selectedItem != null

                        val validName = getViewError(
                            binding.namePlaceholderTv,
                            getLocalizedString(Constants.nameEmptyEror, pageDataFields)
                        )
                        if (!validName)
                            return@setOnClickListener

                        val validLastName = getViewError(
                            binding.surnamePlaceholderTv,
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
                                    firstName = binding.namePlaceholderTv.text.trim().toString(),
                                    lastName = binding.surnamePlaceholderTv.text.trim()
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
                                                                findNavController().navigateUp()
                                                                binding.progressAvi.hide()

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
                                            CommonMethods.onSNACK(
                                                binding.root,
                                                getLocalizedString(
                                                    Constants.imageEmtpyError,
                                                    pageDataFields
                                                )
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
                .setLabel("", "", "", "", "", "")
                .setCancelColor(ContextCompat.getColor(requireContext(), R.color.orangeButton))
                .setSubmitColor(ContextCompat.getColor(requireContext(), R.color.orangeButton))
                .build()

            pvTime.show()
        }

    }

    fun parseDateToddMMyyyy(time: String?): String? {
        val inputPattern = "yyyy-MM-dd"
        val outputPattern = "dd-MM-yyyy"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)
        var date: Date? = null
        var str: String? = null
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserEditBinding.inflate(inflater, container, false)
        val view = binding.root

        val cropImage = registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                // use the returned uri
                val uriContent = result.uriContent
                val uriFilePath = result.getUriFilePath(requireContext()) // optional usage
                profileUri = uriFilePath.toString()

                Glide.with(requireContext())
                    .load(uriContent)
                    .into(binding.imageProfile)


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
        setUpViewmodel()

        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}

    override fun onNothingSelected(parent: AdapterView<*>?) {}


}