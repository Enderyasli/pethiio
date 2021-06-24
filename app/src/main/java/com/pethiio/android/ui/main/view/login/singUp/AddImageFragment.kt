package com.pethiio.android.ui.main.view.login.singUp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pethiio.android.R
import com.pethiio.android.databinding.FragmentAddImageBinding
import com.pethiio.android.ui.base.RegisterBaseFragment
import com.pethiio.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pethiio.android.utils.Constants
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_add_image.*
import kotlinx.android.synthetic.main.item_layout.view.*


class AddImageFragment : RegisterBaseFragment<RegisterBaseViewModel>() {

    override var bottomNavigationViewVisibility = View.GONE
    private var _binding: FragmentAddImageBinding? = null

    override var useSharedViewModel = true


    private val binding get() = _binding!!

    override fun setUpViews() {
        super.setUpViews()
        viewModel.getAddImageFields().observe(this, Observer {

            setPawtindResponseList(it)
            binding.animalAddPhotoTitle.text = getLocalizedString(Constants.registerTitle)
            binding.completeBtn.text = getLocalizedString(Constants.registerCompleteButtonTitle)

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddImageBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.imageView.setOnClickListener {

            CropImage.activity()
                .setAspectRatio(1, 1)
                .start(requireContext(), this)
        }
        binding.completeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_photo_to_navigation_animal_list)
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
                    .into(binding.imageView)
                when {
                    image1.drawable == null -> {
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
                    }
                    image2.drawable == null -> {
                        Glide.with(requireContext())
                            .load(resultUri)
                            .into(binding.image2)
                        binding.image2Placeholder.visibility = View.GONE
                        binding.image2X.visibility = View.VISIBLE
                    }
                    image3.drawable == null -> {
                        Glide.with(requireContext())
                            .load(resultUri)
                            .into(binding.image3)
                        binding.image3Placeholder.visibility = View.GONE
                        binding.image3X.visibility = View.VISIBLE
                    }
                }

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

}