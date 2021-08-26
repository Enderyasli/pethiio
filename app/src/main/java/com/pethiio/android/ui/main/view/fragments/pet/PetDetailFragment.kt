package com.pethiio.android.ui.main.view.fragments.pet

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pethiio.android.R
import com.pethiio.android.data.EventBus.LikeEvent
import com.pethiio.android.data.model.petDetail.PetSearchDetailResponse
import com.pethiio.android.data.model.petDetail.PetSearchOwnerDetailResponse
import com.pethiio.android.databinding.FragmentPetDetailBinding
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.ui.base.ViewModelFactory
import com.pethiio.android.ui.main.adapter.PetSearchDetailCharacterAdapter
import com.pethiio.android.ui.main.adapter.ViewPagerAdapter
import com.pethiio.android.ui.main.view.customViews.MaximobileDialog
import com.pethiio.android.ui.main.viewmodel.PetDetailViewModel
import com.pethiio.android.utils.CommonMethods
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.Status
import org.greenrobot.eventbus.EventBus
import java.lang.reflect.Method


class PetDetailFragment : BaseFragment() {

    private var _binding: FragmentPetDetailBinding? = null
    private val binding get() = _binding!!
    override var bottomNavigationViewVisibility = View.GONE

    private lateinit var viewModel: PetDetailViewModel

    private var owner: String = ""
    private var report: String = ""
    private var userId: Int? = 0
    private var memberId: Int = 0
    private var animalId: String = ""
    private var ownerAgeTitle: String = ""
    private var petDeleteAlert: String = ""


    private var isOwner: Boolean = false
    private var fromChat: Boolean = false

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
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return view
    }

    @SuppressLint("SetTextI18n")
    override fun setUpViews() {
        super.setUpViews()


        // TODO: 19.07.2021 animal owner textlerini ayarla
        memberId = arguments?.getInt("memberId", 0)!!
        animalId = arguments?.getString("animalId", "")!!
        isOwner = arguments?.getBoolean("isOwner", false)!!
        fromChat = arguments?.getBoolean("fromChat", false)!!

        if (isOwner) {
            if (fromChat)
                viewModel.fetchPetSearchDetailPageData()
            else
                viewModel.fetchPetDetailPageData()


            binding.scrollView.setBackgroundColor(Color.WHITE)
            binding.mainLayout.setBackgroundColor(Color.WHITE)
            binding.buttonContainer.visibility = View.GONE
        } else {
            viewModel.fetchPetSearchDetailPageData()
        }
        viewModel.fetchPetDetail(animalId)

        binding.popupMenu.setOnClickListener {
            openPopUpMenu()
        }

        viewModel.getPetDetailPageData().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {

                    val fields = it.data?.fields

                    binding.colorLy.title.text =
                        getLocalizedString(Constants.petSearchDetailAboutColorTitle, fields)
                    binding.listLy.title.text =
                        getLocalizedString(Constants.petSearchDetailListTypeTitle, fields)
                    binding.detailLy.title.text =
                        getLocalizedString(Constants.petSearchDetailDetailTitle, fields)
                    owner = if (isOwner && !fromChat)
                        getLocalizedString(Constants.petSearchDetailUpdate, fields)
                    else
                        getLocalizedString(Constants.petSearchDetailOwner, fields)

                    report = if (isOwner && !fromChat)
                        getLocalizedString(Constants.petSearchDetailDelete, fields)
                    else
                        getLocalizedString(Constants.petSearchDetailReport, fields)


                    binding.ownerAboutTv.text =
                        getLocalizedString(Constants.petSearchDetailAboutOwnerTitle, fields)
                    ownerAgeTitle =
                        " " + getLocalizedString(
                            Constants.petSearchDetailAboutOwnerAgeTitle,
                            fields
                        )

                    petDeleteAlert = getLocalizedString(Constants.petDeleteAlert, fields)
                    binding.progressAvi.hide()


                }
                Status.LOADING -> {
                    binding.progressAvi.show()
                }
            }

            binding.skipButton.setOnClickListener {

                findNavController().navigateUp()
                Handler().postDelayed({ EventBus.getDefault().post(LikeEvent(false)) }, 500)
            }
            binding.likeButton.setOnClickListener {

                findNavController().navigateUp()
                Handler().postDelayed({ EventBus.getDefault().post(LikeEvent(true)) }, 500)


            }


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

                    if (petDetail?.personalities?.size == 0) {
                        binding.characterRv.visibility = View.GONE
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

                    userId = petDetail?.userId


                    binding.progressAvi.hide()
                }
                Status.ERROR -> {
                    binding.progressAvi.hide()
                    CommonMethods.onSNACK(binding.root, it.message.toString())

                }
            }


        })


    }

    @SuppressLint("SetTextI18n")
    private fun getOwnerDetail() {

        viewModel.fetchPetOwnerDetail(userId.toString())


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


//                    if (!isOwner)
                        changeUserType(true)

                    binding.progressAvi.hide()
                }
                Status.ERROR -> {
                    binding.progressAvi.hide()

                }
            }
        })

    }

    private fun openPopUpMenu() {
        val popUpMenu = PopupMenu(requireContext(), binding.popupMenu)
        if (isOwner && !fromChat)
            popUpMenu.inflate(R.menu.pop_up_menu)
        else
            popUpMenu.inflate(R.menu.pop_up_owner)


        popUpMenu.menu.getItem(0).title = owner
        popUpMenu.menu.getItem(1).title = report

        popUpMenu.apply {

            popUpMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.owner -> {
                        if (!isOwner) {
                            if (!ownerSelected) {

                                if (!hasOwnerInfo)
                                    getOwnerDetail()
                                else
                                    changeUserType(true)
                            } else {
                                changeUserType(false)
                            }
                        } else {
                            if (!fromChat) {
                                val bundle = bundleOf("animalId" to animalId, "from" to false)
                                findNavController().navigate(R.id.navigation_pet_add, bundle)
                            } else {
                                if (!ownerSelected) {
                                    if (!hasOwnerInfo)
                                        getOwnerDetail()
                                    else
                                        changeUserType(true)
                                } else {
                                    changeUserType(false)
                                }
                            }


                            // TODO: 7.07.2021 düzenle
                        }
                    }

                    R.id.report -> {
                        if (!isOwner) {
                            findNavController().navigate(R.id.navigation_report)
                        } else {
                            if (!fromChat)
                                openDelete(report, petDeleteAlert)
                            else
                                findNavController().navigate(R.id.navigation_report)

                        }

                    }


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

    private fun openDelete(delete: String, title: String) {

        val maximobileDialog =
            MaximobileDialog(
                requireContext(),
                true,
                title,
                delete,
                getString(R.string.lcl_cancel_datepicker)
            )

        maximobileDialog.getPositiveButton().setOnClickListener {
            maximobileDialog.dissmiss()

            animalId.toIntOrNull()?.let { viewModel.fetchPetDelete(it) }
            binding.progressAvi.show()
            Handler().postDelayed({ findNavController().navigateUp() }, 500)
        }
        maximobileDialog.getNegativeButton().setOnClickListener {
            maximobileDialog.dissmiss()
        }
        maximobileDialog.getPositiveButton().visibility = View.VISIBLE

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
        return super.onPrepareOptionsMenu(menu)
    }


}