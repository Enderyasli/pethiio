package com.pethiio.android.ui.main.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.ViewModelProviders
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
import com.pethiio.android.PethiioApplication
import com.pethiio.android.R
import com.pethiio.android.data.socket.SocketIOService
import com.pethiio.android.ui.main.viewmodel.ChatViewModel
import com.pethiio.android.ui.main.viewmodel.MainActivityViewModel
import com.pethiio.android.utils.CommonMethods
import com.pethiio.android.utils.PreferenceHelper
import com.pethiio.android.utils.Status


class MainActivity : AppCompatActivity() {


    val MY_PERMISSIONS_REQUEST_LOCATION = 99

    lateinit var navView: BottomNavigationView
    lateinit var viewCustom: View
    var navController: NavController? = null
    private lateinit var viewModel: MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        //change statusbar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }


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


        checkGpsPermission()
//        setUpViewModel()


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


    public fun getSocket() {

    }

    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        viewModel.getAllChatList().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {

                    if (PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn == true) {

                        var socketService: SocketIOService? = null
                        socketService = SocketIOService()
//                        socketService.setListenersMap(it.data)
                        val service = Intent(this, socketService.javaClass)
                        service.putExtra(
                            SocketIOService.EXTRA_EVENT_TYPE,
                            SocketIOService.EVENT_TYPE_MESSAGE
                        )
                        startService(service)

                    }
                }
            }


        })
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            99 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    if (ContextCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        createLocationRequest()

                        navController?.navigateUp()

                    } else {

                    }

                } else {
                }
            }
        }
    }

    public fun checkGpsPermission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn == true)
                navController?.navigate(R.id.navigation_location)
        } else {
            createLocationRequest()

        }
    }

    @SuppressLint("MissingPermission")
    fun createLocationRequest() {

        if (navController?.currentDestination?.id == R.id.navigation_dashboard) {

            val locationRequest = LocationRequest.create().apply {
                interval = 3000
                fastestInterval = 1500
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

            val client: SettingsClient = LocationServices.getSettingsClient(applicationContext)
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())



            task.addOnCompleteListener {
                try {
                    task.getResult(ApiException::class.java)


                } catch (e: ApiException) {
                    when (e.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            if (e is ResolvableApiException) {

                                try {
                                    e.startResolutionForResult(this, 6989)
                                } catch (sendEx: IntentSender.SendIntentException) {
                                    Log.e("sednex", sendEx.toString())

                                }
                            }
                        }
                    }


                }
            }

        }
    }


}
