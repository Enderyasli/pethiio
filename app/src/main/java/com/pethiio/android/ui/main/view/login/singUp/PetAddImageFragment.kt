package com.pethiio.android.ui.main.view.login.singUp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pethiio.android.R
import com.pethiio.android.databinding.FragmentPetAddImageBinding
import com.pethiio.android.ui.base.RegisterBaseFragment
import com.pethiio.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status
import com.theartofdev.edmodo.cropper.CropImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class PetAddImageFragment : RegisterBaseFragment<RegisterBaseViewModel>() {

    override var bottomNavigationViewVisibility = View.GONE
    private var _binding: FragmentPetAddImageBinding? = null

    override var useSharedViewModel = true
    var uri1: String = ""
    var uri2: String = ""
    var uri3: String = ""


    private val binding get() = _binding!!

    override fun setUpViews() {
        super.setUpViews()
        viewModel.getAddImageFields().observe(this, {

            setPethiioResponseList(it)
            binding.animalAddPhotoTitle.text = getLocalizedString(Constants.registerTitle)
            binding.completeBtn.text = getLocalizedString(Constants.registerCompleteButtonTitle)

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun openCropImage() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .start(requireContext(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetAddImageBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.imageView.setOnClickListener { openCropImage() }
        binding.img1Ly.setOnClickListener { openCropImage() }
        binding.img2Ly.setOnClickListener { openCropImage() }
        binding.img3Ly.setOnClickListener { openCropImage() }
        binding.image1X.setOnClickListener {

            binding.image1Placeholder.visibility = View.GONE
            binding.image1X.visibility = View.VISIBLE

            binding.completeBtn.isEnabled = true
            binding.completeBtn.setBackgroundResource(R.drawable.orange_button_bg)
            binding.completeBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
        }




        binding.completeBtn.setOnClickListener {

            val filePart1= getFilePart(uri1)
            val filePart2= getFilePart(uri2)
            val filePart3= getFilePart(uri3)

            val listOfFiles = listOf(filePart1,filePart2,filePart3)


                postPetPhoto(listOfFiles)




            viewModel.postPetPhoto.observe(viewLifecycleOwner, { it1 ->
                when (it1.status) {
                    Status.SUCCESS -> {
                        activity?.runOnUiThread {

                            fetchPetList()
                            fetchPetListPageData()
                            viewModel.petListPageData.observe(viewLifecycleOwner, { it1 ->

                                when (it1.status) {
                                    Status.SUCCESS -> {
                                        activity?.runOnUiThread {
                                            viewModel.petList.observe(viewLifecycleOwner, {
                                                when (it.status) {
                                                    Status.SUCCESS -> {
                                                        activity?.runOnUiThread {
                                                            if (findNavController().currentDestination?.id == R.id.navigation_pet_add_photo)
                                                                findNavController().navigate(R.id.action_navigation_pet_add_image_to_navigation_animal_list)
                                                        }
                                                    }
                                                    Status.ERROR -> {
                                                        Toast.makeText(
                                                            requireContext(),
                                                            it.message,
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                    Status.LOADING -> {
                                                    }
                                                }
                                            })
                                        }
                                    }
                                    Status.ERROR -> {
                                    }
                                    Status.LOADING -> {
                                    }
                                }

                            })


                        }

                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it1.message, Toast.LENGTH_LONG).show()

                    }
                    Status.LOADING -> {
                    }
                }
            })
        }

        return view

    }

    fun getFilePart(uri: String): MultipartBody.Part? {
        if (!TextUtils.isEmpty(uri)) {
            val file = File(uri)
            val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData("files", file.name, requestBody)
        }

        return null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == -1) {
                val resultUri: Uri = result.uri
                Glide.with(requireContext())
                    .load(resultUri)
                    .into(binding.imageView)
                when {
                    binding.image1.drawable == null -> {
                        Glide.with(requireContext())
                            .load(resultUri)
                            .into(binding.image1)
                        binding.image1Placeholder.visibility = View.GONE
                        binding.image1X.visibility = View.VISIBLE

                        binding.completeBtn.isEnabled = true
                        binding.completeBtn.setBackgroundResource(R.drawable.orange_button_bg)
                        binding.completeBtn.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )

                        uri1 = resultUri.path.toString()
                    }
                    binding.image2.drawable == null -> {
                        Glide.with(requireContext())
                            .load(resultUri)
                            .into(binding.image2)
                        binding.image2Placeholder.visibility = View.GONE
                        binding.image2X.visibility = View.VISIBLE
                        uri2 = resultUri.path.toString()

                    }
                    binding.image3.drawable == null -> {
                        Glide.with(requireContext())
                            .load(resultUri)
                            .into(binding.image3)
                        binding.image3Placeholder.visibility = View.GONE
                        binding.image3X.visibility = View.VISIBLE

                        uri3 = resultUri.path.toString()

                    }
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                result.error
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getViewModelClass() = RegisterBaseViewModel::class.java

}