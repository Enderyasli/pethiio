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

//        createLocationRequest()



        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // TODO: 6.07.2021 burda location izin ekranına gönder, gelişte location actır, sonra lat, lon al
                navController!!.navigate(R.id.navigation_location)
//                ActivityCompat.requestPermissions(requireActivity(),
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {

                createLocationRequest()


                // GET CURRENT LOCATION
                // GET CURRENT LOCATION



//                navController.navigate(R.id.navigation_location)
//                ActivityCompat.requestPermissions(requireActivity(),
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }


    }


//    private fun startLocationUpdates() {
//        fusedLocationClient.requestLocationUpdates(locationRequest,
//            locationCallback,
//            Looper.getMainLooper())
//    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        createLocationRequest()

                        navController!!.navigateUp()


//                        Toast.makeText(this, "location ok", Toast.LENGTH_LONG).show()

                                            }

                } else {

//                    Toast.makeText(this, "location no", Toast.LENGTH_LONG).show()

                }
            }
        }
    }


    fun createLocationRequest() {
        val locationRequest = LocationRequest.create().apply {
            interval = 3000
            fastestInterval = 1500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)



        val client: SettingsClient = LocationServices.getSettingsClient(this)
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
