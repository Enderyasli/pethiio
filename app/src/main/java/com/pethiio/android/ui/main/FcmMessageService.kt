package com.pethiio.android.ui.main


import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pethiio.android.PethiioApplication
import com.pethiio.android.ui.main.util.NotificationUtils
import com.pethiio.android.utils.PreferenceHelper

class FcmMessageService : FirebaseMessagingService() {




    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // If required send token to your app server.
//        sendRegistrationToServer(token)
    }


    override fun handleIntent(intent: Intent) {
        super.handleIntent(intent)
        intent.extras
    }
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
    }
//    /**
//     * Called when message is received.
//     *
//     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
//     */
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        super.onMessageReceived(remoteMessage)
//        Log.d(TAG, "From: ${remoteMessage.from}")
//
//        //Use this condition to validation login
//        if (PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn == false) {
//            return
//        } else if (remoteMessage.data.isNotEmpty()) {
//            val extras = Bundle()
//            for ((key, value) in remoteMessage.data) {
//                extras.putString(key, value)
//            }
//            if (extras.containsKey("message") && !extras.getString("message").isNullOrBlank()) {
//
//                val notificationUtils = NotificationUtils(PethiioApplication.context)
//                notificationUtils.showNotificationMessage(
//                    extras.getString("message"),
//                    extras.getString("message"),
//                    null,
//                    null
//                )
//
//            }
//        }
//    }


    companion object {
        private const val TAG = "FcmMessageService"
    }
}