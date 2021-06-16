package com.pawtind.android.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pawtind.android.data.api.ApiHelper
import com.pawtind.android.data.repository.MainRepository
import com.pawtind.android.ui.main.viewmodel.MainViewModel
import com.pawtind.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pawtind.android.ui.main.viewmodel.signup.WelcomeViewModel

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainRepository(apiHelper)) as T
        }
        if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
            return WelcomeViewModel(MainRepository(apiHelper)) as T
        }
        if (modelClass.isAssignableFrom(RegisterBaseViewModel::class.java)) {
            return RegisterBaseViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}