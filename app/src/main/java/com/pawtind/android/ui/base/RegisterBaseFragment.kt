package com.pawtind.android.ui.base

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.pawtind.android.data.api.ApiHelper
import com.pawtind.android.data.api.ApiServiceImpl
import com.pawtind.android.data.model.AccessToken
import com.pawtind.android.data.model.LookUpsResponse
import com.pawtind.android.data.model.PawtindResponse
import com.pawtind.android.data.model.signup.Register
import com.pawtind.android.data.model.signup.RegisterInfo
import com.pawtind.android.ui.main.view.MainActivity
import com.pawtind.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pawtind.android.utils.Resource
import com.pawtind.android.utils.Status
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class RegisterBaseFragment<VModel : RegisterBaseViewModel> : Fragment() {

    protected open var bottomNavigationViewVisibility = View.VISIBLE

    protected open var useSharedViewModel: Boolean = false

    protected lateinit var viewModel: VModel
    protected abstract fun getViewModelClass(): Class<VModel>

    private var pawtindResponse: List<PawtindResponse>? = null
    private var lookUpsResponse: List<LookUpsResponse>? = null

    private val disposableContainer = CompositeDisposable()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // get the reference of the parent activity and call the setBottomNavigationVisibility method.
        if (activity is MainActivity) {
            var mainActivity = activity as MainActivity
            mainActivity.setBottomNavigationVisibility(bottomNavigationViewVisibility)

            init()
            setUpViews()

        }
    }

    fun getLocalizedString(key: String): String {

        pawtindResponse?.forEach {
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

    fun getLocalizedSpan(key: String): SpannableString? {

        pawtindResponse?.forEach {
            if (it.key == key)
                return getSpannableString(it.value)
        }
        return null

    }


    fun getSpannableString(key: String): SpannableString {

        val spannable = SpannableString(key + " *")
        spannable.setSpan(
            ForegroundColorSpan(Color.RED),
            key.length,// start
            key.length + 2, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        return spannable
    }

    fun setPawtindResponseList(pawtindResponseList: List<PawtindResponse>) {
        pawtindResponse = pawtindResponseList
    }

    fun setLookUps(lookUpsResponseList: List<LookUpsResponse>) {
        lookUpsResponse = lookUpsResponseList
    }

    override fun onResume() {
        super.onResume()
        if (activity is MainActivity) {
            var mainActivity = activity as MainActivity
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

    open fun fetchAddAnimal() {
        viewModel.fetchAddAnimal()
    }

    open fun postRegister(register: Register) {
        viewModel.postRegister(register)
    }


    open fun postRegisterInfo(registerInfo: RegisterInfo) {
        viewModel.postRegisterInfo(registerInfo)
    }

    private fun init() {
        viewModel = if (useSharedViewModel) {
            ViewModelProviders.of(requireActivity(), ViewModelFactory(ApiHelper(ApiServiceImpl())))
                .get(
                    getViewModelClass()
                )
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