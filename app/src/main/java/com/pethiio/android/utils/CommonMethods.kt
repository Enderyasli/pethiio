package com.pethiio.android.utils

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import com.pethiio.android.PethiioApplication
import com.pethiio.android.R
import com.pethiio.android.data.api.ServiceBuilder
import com.pethiio.android.data.model.LookUpsResponse
import com.pethiio.android.data.model.PethiioResponse
import okhttp3.OkHttpClient
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


class CommonMethods {


    companion object {

        @JvmStatic

        fun addRadioButton(string: String, radioGroup: RadioGroup, context: Context) {

            val radioButton = RadioButton(context)

            radioButton.text = string
            val typeface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.resources.getFont(R.font.typo_round_regular)
            } else {
                ResourcesCompat.getFont(context, R.font.typo_round_regular)
            }


            val colorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_enabled)
                ), intArrayOf(
                    ContextCompat.getColor(context, R.color.grey),  //disabled
                    ContextCompat.getColor(context, R.color.orangeButton) //enabled
                )
            )

            radioButton.buttonTintList = colorStateList

            radioButton.textSize = 14F
            radioButton.typeface = typeface
            radioButton.setTextColor(ContextCompat.getColor(context, R.color.grey))
            radioGroup.addView(radioButton)
        }

        @JvmStatic

        fun addRadioButtonwithId(
            id: Int,
            string: String,
            radioGroup: RadioGroup,
            context: Context
        ) {

            val radioButton = RadioButton(context)

            radioButton.text = string
            val typeface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.resources.getFont(R.font.typo_round_regular)
            } else {
                ResourcesCompat.getFont(context, R.font.typo_round_regular)
            }


            val colorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_enabled)
                ), intArrayOf(
                    ContextCompat.getColor(context, R.color.grey),  //disabled
                    ContextCompat.getColor(context, R.color.orangeButton) //enabled
                )
            )

            radioButton.buttonTintList = colorStateList

            radioButton.textSize = 14F
            radioButton.typeface = typeface
            radioButton.setTextColor(ContextCompat.getColor(context, R.color.grey))
            radioButton.id = id
            radioGroup.addView(radioButton)
        }

        @JvmStatic
        fun getLookUps(key: String, lookUpsResponse: List<LookUpsResponse>?): List<String> {

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

        @JvmStatic
        fun getLookUpKeys(key: String, lookUpsResponse: List<LookUpsResponse>?): List<String> {

            lookUpsResponse?.forEach { it ->
                if (it.key == key) {

                    val arrayList = arrayListOf<String>()
                    it.value.forEach {
                        arrayList.add(it.key)
                    }

                    return arrayList

                }
            }
            return emptyList()
        }

        @JvmStatic
        fun getLookUpKey(
            key: String,
            value: String,
            lookUpsResponse: List<LookUpsResponse>?
        ): String {

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

        @JvmStatic
        fun getAnimalBreeds(breedList: List<PethiioResponse>): ArrayList<String> {

            val arrayList = arrayListOf<String>()
            breedList.forEach {
                arrayList.add(it.value)
            }
            return arrayList
        }

        @JvmStatic
        fun getBreedKeys(breedList: List<PethiioResponse>): List<String> {

            val arrayList = arrayListOf<String>()

            breedList.forEach {
                    arrayList.add(it.key)
            }
            return arrayList
        }

//        @JvmStatic
//        fun getBreedsKey(value: String): String {
//
//            animalDetailResponse?.breeds?.forEach { it ->
//                if (it.value == value)
//                    return it.key
//
//            }
//            return ""
//        }
//
//        @JvmStatic
//        fun getBreedIndex(key: String): Int {
//
//            animalDetailResponse?.breeds?.forEachIndexed { index, s ->
//                if (s.key == key)
//                    return index
//            }
//
//            return 0
//        }


        fun onSNACK(view: View, snackText: String) {
//             Snackbar.make(
//                 view,
//                 snackText,
//                 Snackbar.LENGTH_LONG
//             ).show()


            val snackBar = Snackbar.make(
                view, snackText,
                Snackbar.LENGTH_LONG
            )

//            snackbar.setActionTextColor(Color.BLACK)
//             val params = view.layoutParams as FrameLayout.LayoutParams
//             params.gravity = Gravity.TOP
//             params.height= 135
//             snackbar.view.layoutParams = params

            val snackBarView = snackBar.view
            snackBarView.setBackgroundColor(Color.RED)
            val textView =
                snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(Color.WHITE)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            } else {
                textView.gravity = Gravity.CENTER_HORIZONTAL
            }
            textView.textSize = 16f
            snackBar.show()
        }

        fun convertPixelsToDp(px: Float, context: Context?): Float {
            return if (context != null) {
                val resources = context.resources
                val metrics = resources.displayMetrics
                px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
            } else {
                val metrics = Resources.getSystem().displayMetrics
                px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
            }
        }


        fun getClient(): OkHttpClient? {

            var builder: OkHttpClient.Builder? = null



            try {
                val certificate: Int = R.raw.pethiio11
                val cf = CertificateFactory.getInstance("X.509")
                val cert: InputStream =
                    PethiioApplication.context.resources.openRawResource(certificate)
                var ca: Certificate? = null
                try {
                    ca = cf.generateCertificate(cert)
                } catch (e: CertificateException) {
                    e.printStackTrace()
                } finally {
                    cert.close()
                }

                // creating a KeyStore containing our trusted CAs
                val keyStoreType = KeyStore.getDefaultType()
                val keyStore = KeyStore.getInstance(keyStoreType)
                keyStore.load(null, null)
                keyStore.setCertificateEntry("ca", ca)

                // creating a TrustManager that trusts the CAs in our KeyStore
                val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
                val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
                tmf.init(keyStore)
                val trustManagers = tmf.trustManagers
                check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                    "Unexpected default trust managers:" + Arrays.toString(
                        trustManagers
                    )
                }
                val trustManager = trustManagers[0] as X509TrustManager
                val sslContext = SSLContext.getInstance("TLS")
                sslContext.init(null, tmf.trustManagers, null)
                builder = OkHttpClient.Builder().addInterceptor(ServiceBuilder.interceptor)
                    .addNetworkInterceptor(ServiceBuilder.interceptor2)
                    .connectTimeout(180, TimeUnit.SECONDS)
                    .readTimeout(180, TimeUnit.SECONDS)
                    .sslSocketFactory(sslContext.socketFactory, trustManager)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return builder?.build()

        }
    }


}