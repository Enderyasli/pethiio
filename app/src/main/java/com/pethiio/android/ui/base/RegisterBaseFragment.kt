package com.pethiio.android.ui.base

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.pethiio.android.data.model.AnimalDetailResponse
import com.pethiio.android.data.model.LookUpsResponse
import com.pethiio.android.data.model.PethiioResponse
import com.pethiio.android.data.model.PetAdd
import com.pethiio.android.data.model.login.LoginRequest
import com.pethiio.android.data.model.signup.Register
import com.pethiio.android.data.model.signup.RegisterInfo
import com.pethiio.android.ui.main.view.MainActivity
import com.pethiio.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import okhttp3.MultipartBody

abstract class RegisterBaseFragment<VModel : RegisterBaseViewModel> : Fragment() {

    protected open var bottomNavigationViewVisibility = View.VISIBLE

    protected open var useSharedViewModel: Boolean = false

    protected lateinit var viewModel: VModel
    protected abstract fun getViewModelClass(): Class<VModel>

    private var pethiioResponse: List<PethiioResponse>? = null
    private var lookUpsResponse: List<LookUpsResponse>? = null
    private var animalDetailResponse: AnimalDetailResponse? = null

    private val disposableContainer = CompositeDisposable()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // get the reference of the parent activity and call the setBottomNavigationVisibility method.
        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity
            mainActivity.setBottomNavigationVisibility(bottomNavigationViewVisibility)

            init()
            setUpViews()

        }
    }

    fun getLocalizedString(key: String): String {

        pethiioResponse?.forEach {
            if (it.key == key)
                return it.value
        }
        return ""
    }

    fun getLookUps(key: String): List<String> {

        lookUpsResponse?.forEach { it ->
            if (it.key == key) {

                val arrayList = arrayListOf<String>()
                it.value.forEach {
                    arrayList.add(it.value)
                }

                return arrayList

            }
        }
        return emptyList()
    }

    fun getAnimalPersonalities(): ArrayList<String> {

        val arrayList = arrayListOf<String>()
        animalDetailResponse?.personalities?.forEach {
            arrayList.add(it.value)
        }
        return arrayList
    }

    fun getSelectedAnimalPersonality(value: ArrayList<String>): ArrayList<Int> {

        val arrayList = arrayListOf<Int>()
        animalDetailResponse?.personalities?.forEach { pethiio ->
            value.forEach {
                if (pethiio.value == it)
                    arrayList.add(pethiio.key.toInt())
            }

        }
        return arrayList
    }

    fun getAnimalBreeds(): ArrayList<String> {

        val arrayList = arrayListOf<String>()
        animalDetailResponse?.breeds?.forEach {
            arrayList.add(it.value)
        }
        return arrayList
    }

    fun getBreedsKey(value: String): String {

        animalDetailResponse?.breeds?.forEach { it ->
            if (it.value == value)
                return it.key

        }
        return ""
    }

    fun getLookUpKey(key: String, value: String): String {

        lookUpsResponse?.forEach { it ->
            if (it.key == key) {

                it.value.forEach {
                    if (it.value == value)
                        return it.key
                }
            }
        }
        return ""
    }

    fun getLocalizedSpan(key: String): SpannableString? {

        pethiioResponse?.forEach {
            if (it.key == key)
                return getSpannableString(it.value)
        }
        return null

    }

    fun getViewError(editText: EditText, message: String): Boolean {
        var valid = true
        if (TextUtils.isEmpty(editText.text.trim())) {
            editText.error = message
            editText.requestFocus()
            valid = false
        }
        return valid
    }

    private fun getSpannableString(key: String): SpannableString {

        val spannable = SpannableString("$key *")
        spannable.setSpan(
            ForegroundColorSpan(Color.RED),
            key.length,// start
            key.length + 2, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        return spannable
    }

    fun setPethiioResponseList(pethiioResponseList: List<PethiioResponse>?) {
        pethiioResponse = pethiioResponseList
    }

    fun setLookUps(lookUpsResponseList: List<LookUpsResponse>) {
        lookUpsResponse = lookUpsResponseList
    }

    fun setAnimalDetail(animalDetailResponse1: AnimalDetailResponse) {
        animalDetailResponse = animalDetailResponse1
    }

    override fun onResume() {
        super.onResume()
        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity
            mainActivity.setBottomNavigationVisibility(bottomNavigationViewVisibility)
        }
    }

    open fun setUpViews() {}


    open fun fetchLogin() {
        viewModel.fetchLogin()
        //        viewModel.getLogin().observe(this, Observer { it ->
//            when (it.status) {
//                Status.SUCCESS -> {
//                    it.data?.let {
//
//                        pawtindResponse = it.fields
//
//                        Log.d("gelenresponse", it.toString())
//                    }
//                }
//                Status.LOADING -> {
//                }
//                Status.ERROR -> {
//                }
//            }
//        })

    }

    open fun fetchRegister() {
        viewModel.fetchRegister()
    }

    open fun fetchRegisterDetail() {
        viewModel.fetchRegisterDetail()
    }

    open fun fetchAnimalAddPhoto() {
        viewModel.fetchAnimalAddPhoto()
    }

    open fun fetchPetAddPageData() {
        viewModel.fetchPetAddPageData()
    }

    open fun fetchPetListPageData() {
        viewModel.fetchPetListPageData()
    }

    open fun fetchAddAnimalDetail(animalId: String) {
        viewModel.fetchAnimalDetail(animalId)
    }

    open fun postRegister(register: Register) {
        viewModel.postRegister(register)
    }

    open fun postLogin(loginRequest: LoginRequest ) {
        viewModel.postLogin(loginRequest)
    }


    open fun postRegisterInfo(registerInfo: RegisterInfo) {
        viewModel.postRegisterInfo(registerInfo)
    }

    open fun postRegisterAvatar(multipart: MultipartBody.Part) {
        viewModel.postRegisterAvatar(multipart)
    }

    open fun postPetPhoto(multipart: List<MultipartBody.Part?>) {
        viewModel.postPetPhoto(multipart)
    }

    open fun postPetAdd(petAdd: PetAdd) {
        viewModel.postPetAdd(petAdd)
    }

    open fun fetchPetList() {
        viewModel.fetchPetList()
    }

    private fun init() {
        viewModel = if (useSharedViewModel) {
            ViewModelProviders.of(
                requireActivity(),
                ViewModelFactory()
            ).get(getViewModelClass())
        } else {
            ViewModelProvider(this).get(getViewModelClass())
        }

    }

    fun Disposable.addToContainer() = disposableContainer.add(this)

    override fun onDestroyView() {
        disposableContainer.clear()
        super.onDestroyView()
    }


}