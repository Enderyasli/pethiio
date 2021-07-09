package com.pethiio.android.ui.main.view.login.singUp

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.pethiio.android.R
import com.pethiio.android.data.model.petDetail.PetImageResponse
import com.pethiio.android.databinding.FragmentPetAddImageBinding
import com.pethiio.android.ui.base.RegisterBaseFragment
import com.pethiio.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pethiio.android.utils.CommonFunctions
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.PreferenceHelper
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

    private var petId: String? = ""


    private val binding get() = _binding!!


    override fun setUpViews() {
        super.setUpViews()
        viewModel.getAddImageFields().observe(this, {

            setPethiioResponseList(it)
            binding.animalAddPhotoTitle.text = getLocalizedString(Constants.registerTitle)
            binding.completeBtn.text = getLocalizedString(Constants.registerCompleteButtonTitle)

        })


        petId?.toIntOrNull()?.let {
            viewModel.fetchPetPhotos(it)
            PreferenceHelper.SharedPreferencesManager.getInstance().petId = it
        }

        viewModel.getPetPhotos().observe(viewLifecycleOwner, {

            it.data?.let { it1 -> setImages(it1) }
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

    fun clearImage(placeholder: ImageView, xImage: ImageView, imageView: ImageView) {
        placeholder.visibility = View.VISIBLE
        xImage.visibility = View.GONE
        imageView.setImageResource(0)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetAddImageBinding.inflate(inflater, container, false)
        val view = binding.root

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(false) {
                override fun handleOnBackPressed() {

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.photoAddAnim.setAnimation("foto_ekle.json")

        petId = arguments?.getString("petId", "")


//        binding.imageView.setOnClickListener { openCropImage() }
        binding.img1Ly.setOnClickListener { openCropImage() }
        binding.img2Ly.setOnClickListener { openCropImage() }
        binding.img3Ly.setOnClickListener { openCropImage() }
        binding.image1X.setOnClickListener {


            if (!TextUtils.isEmpty(uri3)) {
                uri2 = uri3
                Glide.with(requireContext())
                    .load(uri3)
                    .into(binding.image2)
                clearImage(
                    binding.image3Placeholder,
                    binding.image3X,
                    binding.image3
                )
                uri3 = ""
            }
            if (!TextUtils.isEmpty(uri2)) {
                uri1 = uri2
                Glide.with(requireContext())
                    .load(uri2)
                    .into(binding.image1)
                uri2 = ""
                binding.image2.setImageResource(0)
                clearImage(
                    binding.image2Placeholder,
                    binding.image2X,
                    binding.image2
                )
            } else {
                clearImage(
                    binding.image1Placeholder,
                    binding.image1X,
                    binding.image1
                )
                uri1 = ""

                binding.completeBtn.isEnabled = false
                binding.completeBtn.setBackgroundResource(R.drawable.disabled_button_bg)
                binding.completeBtn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.disabled_button_color
                    )
                )

                binding.imageBg.visibility = View.GONE
                binding.photoAddAnim.visibility = View.VISIBLE
            }


        }




        binding.completeBtn.setOnClickListener {

            val filePart1 = getFilePart(uri1)
            val filePart2 = getFilePart(uri2)
            val filePart3 = getFilePart(uri3)

            val listOfFiles = listOf(filePart1, filePart2, filePart3)


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
                                                            if (petId == null) {


                                                                if (findNavController().currentDestination?.id == R.id.navigation_pet_add_photo)
                                                                    findNavController().navigate(R.id.action_navigation_pet_add_image_to_navigation_animal_list)
                                                            } else
                                                                if (findNavController().currentDestination?.id == R.id.navigation_pet_add_photo)
                                                                    findNavController().navigate(R.id.action_navigation_pet_add_photo_to_navigation_main)
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

    private fun setImages(images: List<PetImageResponse>) {

        if (images.isNotEmpty()) {
            if (images[0].indexOrder == 0) {

                binding.imageBg.visibility = View.VISIBLE
                binding.photoAddAnim.visibility = View.GONE

                binding.completeBtn.isEnabled = true
                binding.completeBtn.setBackgroundResource(R.drawable.orange_button_bg)
                binding.completeBtn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )

                uri1 = images[0].path
                Glide.with(requireContext())
                    .load(uri1)
                    .into(binding.imageView)

                setImageHolder(
                    uri1,
                    binding.image1,
                    binding.image1Placeholder,
                    binding.image1X,
                    "uri1.png"
                )
            }
            if (images.size >= 2 && images[1].indexOrder == 1) {
                uri2 = images[1].path
                setImageHolder(
                    uri2,
                    binding.image2,
                    binding.image2Placeholder,
                    binding.image2X,
                    "uri2.png"
                )
            }
            if (images.size >= 3 && images[2].indexOrder == 2) {
                uri3 = images[2].path
                setImageHolder(
                    uri3,
                    binding.image3,
                    binding.image3Placeholder,
                    binding.image3X,
                    "uri3.png"
                )
            }

        }


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
                binding.imageBg.visibility = View.VISIBLE
                binding.photoAddAnim.visibility = View.GONE
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

    private fun setImageHolder(
        uri: String,
        imageView: ImageView,
        imagePlaceholder: ImageView,
        imageX: ImageView, fileName: String
    ) {

        Glide.with(requireContext())
            .load(uri)
            .into(imageView)

        imagePlaceholder.visibility = View.GONE
        imageX.visibility = View.VISIBLE


        Glide.with(imageView)
            .asBitmap().load(uri).into(object : SimpleTarget<Bitmap?>() {

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {

                    val filesDir = requireActivity().filesDir.toString() + "/petImages"

                    if (fileName == "uri1.png")
                        uri1 = CommonFunctions.saveImage(filesDir, fileName, resource)
                    if (fileName == "uri2.png")
                        uri2 = CommonFunctions.saveImage(filesDir, fileName, resource)
                    if (fileName == "uri3.png")
                        uri3 = CommonFunctions.saveImage(filesDir, fileName, resource)

                }
            })


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getViewModelClass() = RegisterBaseViewModel::class.java

}