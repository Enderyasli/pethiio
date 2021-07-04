package com.pethiio.android.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pethiio.android.ui.main.viewmodel.DashBoardViewModel
import com.pethiio.android.ui.main.viewmodel.HomeViewModel
import com.pethiio.android.ui.main.viewmodel.PetDetailViewModel
import com.pethiio.android.ui.main.viewmodel.signup.RegisterBaseViewModel
import com.pethiio.android.ui.main.viewmodel.signup.WelcomeViewModel

class ViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel() as T
        }
        if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
            return WelcomeViewModel() as T
        }
        if (modelClass.isAssignableFrom(DashBoardViewModel::class.java)) {
            return DashBoardViewModel() as T
        }
        if (modelClass.isAssignableFrom(RegisterBaseViewModel::class.java)) {
            return RegisterBaseViewModel() as T
        }
        if (modelClass.isAssignableFrom(PetDetailViewModel::class.java)) {
            return PetDetailViewModel() as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}