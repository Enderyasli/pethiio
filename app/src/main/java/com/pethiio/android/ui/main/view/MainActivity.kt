package com.pethiio.android.ui.main.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pethiio.android.R
import com.pethiio.android.utils.PreferenceHelper


class MainActivity : AppCompatActivity() {


    val MY_PERMISSIONS_REQUEST_LOCATION = 99

    lateinit var navView: BottomNavigationView
    lateinit var viewCustom: View
    var navController: NavController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        navView = findViewById<BottomNavigationView>(R.id.bottomNav_view)

        //Pass the ID's of Different destinations

        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_home,
            R.id.navigation_dashboard,
        ).build()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottomNav_view)
            .setupWithNavController(navController!!)


        //Add custom tab menu
        val bottomMenuView = navView.getChildAt(0) as BottomNavigationMenuView
        val view = bottomMenuView.getChildAt(1)

        val itemView = view as BottomNavigationItemView
        viewCustom =
            LayoutInflater.from(this).inflate(R.layout.button_custom, bottomMenuView, false)
//        viewCustom.setOnClickListener {
//            navController.navigate(R.id.navigation_dashboard)
//        }
        itemView[1].setOnClickListener {
            navController!!.navigate(R.id.navigation_dashboard)
        }
        itemView.addView(viewCustom)


//        try {
//            val db = Room.databaseBuilder(
//                this,
//                AppDatabase::class.java, "user.db"
//            ).build()
//
//            val userDao = db.userDao()
//            val users: List<User> = userDao.getAll()
//        } catch (e: Exception) {
//        }


    }






    @SuppressLint("ResourceAsColor")
    fun setDashboardClickListener(dashboardClicked: Boolean) {

        // TODO: 4.07.2021 logo gelince aç
//        if (dashboardClicked)
//            ImageViewCompat.setImageTintList(
//                viewCustom.findViewById<ImageView>(R.id.dashboard_icon_img),
//                ContextCompat.getColorStateList(this, R.color.orangeButton)
//            )
//        else
//            ImageViewCompat.setImageTintList(
//                viewCustom.findViewById<ImageView>(R.id.dashboard_icon_img),
//                ContextCompat.getColorStateList(this, R.color.grey)
//            )

    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//    }

    fun setBottomNavigationVisibility(visibility: Int) {
        // get the reference of the bottomNavigationView and set the visibility.
        navView.visibility = visibility
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}
