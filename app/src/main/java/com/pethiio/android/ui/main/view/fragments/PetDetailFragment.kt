package com.pethiio.android.ui.main.view.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pethiio.android.R
import com.pethiio.android.data.model.petDetail.PetSearchDetailResponse
import com.pethiio.android.data.model.petDetail.PetSearchOwnerDetailResponse
import com.pethiio.android.databinding.FragmentPetDetailBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.base.ViewModelFactory
import com.pethiio.android.ui.main.adapter.PetSearchDetailCharacterAdapter
import com.pethiio.android.ui.main.adapter.ViewPagerAdapter
import com.pethiio.android.ui.main.viewmodel.PetDetailViewModel
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status
import java.lang.reflect.Method


class PetDetailFragment : BaseFragment() {

    private var _binding: FragmentPetDetailBinding? = null
    private val binding get() = _binding!!
    override var bottomNavigationViewVisibility = View.GONE

    private lateinit var viewModel: PetDetailViewModel

    private var owner: String = ""
    private var report: String = ""
    private var userId: String = ""
    private var ownerAgeTitle: String = ""

    private var hasOwnerInfo: Boolean = false
    private var ownerSelected: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel =
            ViewModelProviders.of(this, ViewModelFactory()).get(PetDetailViewModel::class.java)






        return view
    }

    @SuppressLint("SetTextI18n")
    override fun setUpViews() {
        super.setUpViews()

        viewModel.fetchPetDetailPageData()
        viewModel.fetchPetDetail()

        binding.popupMenu.setOnClickListener {
            openPopUpMenu()
        }

        viewModel.getPetDetailPageData().observe(viewLifecycleOwner, {


            val fields = it.data?.fields

            binding.colorLy.title.text =
                getLocalizedString(Constants.petSearchDetailAboutColorTitle, fields)
            binding.listLy.title.text =
                getLocalizedString(Constants.petSearchDetailListTypeTitle, fields)
            binding.detailLy.title.text =
                getLocalizedString(Constants.petSearchDetailDetailTitle, fields)
            owner = getLocalizedString(Constants.petSearchDetailOwner, fields)
            report = getLocalizedString(Constants.petSearchDetailReport, fields)

            binding.ownerAboutTv.text =
                getLocalizedString(Constants.petSearchDetailAboutOwnerTitle, fields)
            ownerAgeTitle =
                " " + getLocalizedString(Constants.petSearchDetailAboutOwnerAgeTitle, fields)


        })

        viewModel.getPetDetail().observe(viewLifecycleOwner, {

            val petDetail: PetSearchDetailResponse? = it.data
            when (it.status) {

                Status.SUCCESS -> {

                    petDetail?.photos?.let { it1 ->
                        val mViewPagerAdapter = ViewPagerAdapter(requireContext(), it1)
                        binding.imagePager.adapter = mViewPagerAdapter
                    }

                    binding.characterRv.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                    val adapterCharacter = petDetail?.personalities?.let { it1 ->
                        PetSearchDetailCharacterAdapter(
                            requireContext(),
                            it1
                        )
                    }

                    binding.characterRv.adapter = adapterCharacter

                    binding.petNameTv.text = petDetail?.name
                    binding.petBreedTv.text = petDetail?.breed

                    binding.genderTv.text = petDetail?.gender

                    binding.colorLy.value.text = petDetail?.color
                    binding.listLy.value.text = petDetail?.purpose
                    binding.detailLy.value.text = petDetail?.about

                    binding.ageTv.text = petDetail?.age
                    binding.distanceTv.text = petDetail?.distance.toString() + " km"

                    userId = petDetail?.userId.toString()


                }
                Status.ERROR -> {

                }
            }


        })


    }

    @SuppressLint("SetTextI18n")
    private fun getOwnerDetail() {

        viewModel.fetchPetOwnerDetail(userId)


        viewModel.getPetOwnerDetail().observe(viewLifecycleOwner, {

            val petOwnerDetail: PetSearchOwnerDetailResponse? = it.data

            when (it.status) {

                Status.SUCCESS -> {
                    hasOwnerInfo = true

                    Glide.with(binding.ownerImage)
                        .load(petOwnerDetail?.avatar)
                        .apply(RequestOptions().override(200, 200))
                        .into(binding.ownerImage)

                    binding.ownerNameTv.text = petOwnerDetail?.fullName
                    binding.ownerAboutValueTv.text = petOwnerDetail?.about
                    binding.ownerAgeTv.text = petOwnerDetail?.age + ownerAgeTitle

                    changeUserType(true)
                }
            }
        })

    }

    private fun openPopUpMenu() {
        val popUpMenu = PopupMenu(requireContext(), binding.popupMenu)
        popUpMenu.inflate(R.menu.pop_up_menu)

        popUpMenu.menu.getItem(0).title = owner
        popUpMenu.menu.getItem(1).title = report

        popUpMenu.apply {

            popUpMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.owner ->
                        if (!ownerSelected) {
                            if (!hasOwnerInfo)
                                getOwnerDetail()
                            else
                                changeUserType(true)
                        } else {
                            changeUserType(false)
                        }

                    R.id.report ->
                        Toast.makeText(
                            requireContext(),
                            "You Clicked : " + item.title,
                            Toast.LENGTH_SHORT
                        ).show()

                }
                true
            }

            // show icons on popup menu

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popUpMenu.setForceShowIcon(true)
            } else {
                try {
                    val fields = popUpMenu.javaClass.declaredFields
                    for (field in fields) {
                        if ("mPopup" == field.name) {
                            field.isAccessible = true
                            val menuPopupHelper = field[popUpMenu]
                            val classPopupHelper =
                                Class.forName(menuPopupHelper.javaClass.name)
                            val setForceIcons: Method = classPopupHelper.getMethod(
                                "setForceShowIcon",
                                Boolean::class.javaPrimitiveType
                            )
                            setForceIcons.invoke(menuPopupHelper, true)
                            break
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            popUpMenu.show()
        }

    }

    private fun changeUserType(isUser: Boolean) {
        if (isUser) {
            ownerSelected = true
            binding.petLayout.visibility = View.GONE
            binding.ownerLayout.visibility = View.VISIBLE
        } else {
            ownerSelected = false
            binding.petLayout.visibility = View.VISIBLE
            binding.ownerLayout.visibility = View.GONE
        }
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        val item: MenuItem = menu.findItem(R.id.owner)
        item.title = "Set to 'Out of bed'"

        return super.onPrepareOptionsMenu(menu)
    }


}