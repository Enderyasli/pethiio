package com.pethiio.android.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pethiio.android.data.repository.MainRepository
import com.pethiio.android.ui.main.viewmodel.MainViewModel
import com.pethiio.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pethiio.android.ui.main.viewmodel.signup.WelcomeViewModel

class ViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel() as T
        }
        if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
            return WelcomeViewModel() as T
        }
        if (modelClass.isAssignableFrom(RegisterBaseViewModel::class.java)) {
            return RegisterBaseViewModel() as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}