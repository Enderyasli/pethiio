package com.pethiio.android.ui.main.view.login.singUp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pethiio.android.R
import com.pethiio.android.data.model.signup.RegisterInfo
import com.pethiio.android.databinding.FragmentSaveInfoBinding
import com.pethiio.android.ui.base.RegisterBaseFragment
import com.pethiio.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_add_image.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody




class RegisterDetailFragment : RegisterBaseFragment<RegisterBaseViewModel>(),
    AdapterView.OnItemSelectedListener {

    override var bottomNavigationViewVisibility = View.GONE
    private var _binding: FragmentSaveInfoBinding? = null

    override var useSharedViewModel = true

    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setUpViews() {
        super.setUpViews()
        viewModel.getRegisterDetailFields().observe(this, {

            setPawtindResponseList(it)
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
                getLocalizedString(Constants.registerGenderTitle)
            binding.addAnimalBtn.text =
                getLocalizedString(Constants.registerAnimalInsertButtonTitle)
            binding.goWithoutAnimalTv.text =
                getLocalizedString(Constants.registerSkipButtonTitle)

        })

        viewModel.getRegisterDetailLookUps().observe(this, {

            setLookUps(it)

            val gender = getLookUps("gender")
            val genderAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, gender)
            genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            with(binding.genderLy.spinner)
            {
                adapter = genderAdapter
                setSelection(0, false)
                onItemSelectedListener = this@RegisterDetailFragment
                gravity = Gravity.CENTER

            }

        })

        binding.addAnimalBtn.setOnClickListener {

            postRegisterInfo(
                RegisterInfo(
                    aboutMe = "test",
                    dateOfBirth = "2021-06-16",
                    gender = "FEMALE"
                )
            )

            viewModel.postRegisterInfo.observe(viewLifecycleOwner, {
                when (it.status) {
                    Status.SUCCESS -> {
                        activity?.runOnUiThread {
                            fetchAddAnimal()
                            viewModel.getAddAnimalFields().observe(viewLifecycleOwner,{
                                fetchAddAnimalDetail("1")
                            })
                            if (findNavController().currentDestination?.id == R.id.navigation_register_detail)
                                findNavController().navigate(R.id.action_navigation_register_detail_to_navigation_add_animal)

                        }

                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                    }
                    Status.LOADING -> {
                    }
                }
            })


//            postRegisterInfo(
//                RegisterInfo(
//                    aboutMe = "test",
//                    dateOfBirth = "2021-06-16",
//                    gender = "FEMALE"
//                )
//            )
//
//            viewModel.postRegisterInfo.observe(viewLifecycleOwner, {
//                when (it.status) {
//                    Status.SUCCESS -> {
//                        activity?.runOnUiThread {
//                            fetchAddAnimal()
//                            if (findNavController().currentDestination?.id == R.id.navigation_register_detail)
//                                findNavController().navigate(R.id.action_navigation_register_detail_to_navigation_add_animal)
//
//                        }
//
//                    }
//                    Status.ERROR -> {
//                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
//
//                    }
//                    Status.LOADING -> {
//                    }
//                }
//            })
        }

        binding.goWithoutAnimalTv.setOnClickListener {
            postRegisterInfo(
                RegisterInfo(
                    aboutMe = "test",
                    dateOfBirth = "2021-06-16",
                    gender = "FEMALE"
                )
            )

            viewModel.postRegisterInfo.observe(viewLifecycleOwner, {
                when (it.status) {
                    Status.SUCCESS -> {
                        activity?.runOnUiThread {
                            fetchAddAnimal()
                            fetchAddAnimalDetail("1")
                            findNavController().navigate(R.id.action_navigation_register_detail_to_navigation_add_animal)

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

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveInfoBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.imagelY.setOnClickListener {

            // for fragment (DO NOT use `getActivity()`)
            CropImage.activity()
                .setAspectRatio(1, 1)
                .start(requireContext(), this)
        }


        return view

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == -1) {
                val resultUri: Uri = result.uri
                Glide.with(requireContext())
                    .load(resultUri)
                    .into(binding.imageProfile)

                binding.imagePlaceholder.visibility = View.GONE

                postRegisterAvatar(MultipartBody.Part.createFormData("user_image", result.uri.toString()))

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
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