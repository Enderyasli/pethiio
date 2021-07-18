package com.pethiio.android.data.api

import com.pethiio.android.PethiioApplication
import com.pethiio.android.R
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.PreferenceHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


object ServiceBuilder {
    var interceptor = TokenInterceptor()

    val interceptor2 = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }


    fun getClient(): OkHttpClient? {

        var builder: OkHttpClient.Builder? = null



        try {
            val certificate: Int = R.raw.pethiio
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
            builder = OkHttpClient.Builder().addInterceptor(interceptor)
                .addNetworkInterceptor(interceptor2)
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .sslSocketFactory(sslContext.socketFactory, trustManager)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return builder?.build()


//         val sClient = OkHttpClient
//            .Builder()
//            .addInterceptor(interceptor)
//            .addNetworkInterceptor(interceptor2)
//            .connectTimeout(60, TimeUnit.SECONDS)
//            .readTimeout(60, TimeUnit.SECONDS)
//
//
//        var sc: SSLContext? = null
//        try {
//            sc = SSLContext.getInstance("SSL")
//            sc.init(null, arrayOf<TrustManager>(object : X509TrustManager {
//                @Throws(CertificateException::class)
//                override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
//
//                }
//
//                @Throws(CertificateException::class)
//                override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
//                }
//
//                override fun getAcceptedIssuers(): Array<X509Certificate> {
//                    return arrayOf()
//                }
//
//            }), SecureRandom())
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        val hv1: HostnameVerifier = HostnameVerifier { hostname, session -> true }
//
//        val workerClassName = "okhttp3.OkHttpClient"
//        try {
//            val workerClass = Class.forName(workerClassName)
//            val hostnameVerifier: Field = workerClass.getDeclaredField("hostnameVerifier")
//            hostnameVerifier.isAccessible = true
//            hostnameVerifier.set(sClient, hv1)
//            val sslSocketFactory: Field = workerClass.getDeclaredField("sslSocketFactory")
//            sslSocketFactory.isAccessible = true
//            sslSocketFactory.set(sClient, sc!!.socketFactory)
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//
//        return sClient.build()
//

    }


    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(getClient())
        .build()
        .create(PethiioServices::class.java)

    fun buildService(): PethiioServices {
        interceptor.accessToken =
            PreferenceHelper.SharedPreferencesManager.getInstance().accessToken
        return retrofit
    }


}