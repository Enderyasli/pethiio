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

    //    override fun onMessageReceived(p0: RemoteMessage) {
//        super.onMessageReceived(p0)
//    }
//    /**
//     * Called when message is received.
//     *
//     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
//     */
// TODO: 17.07.2021 socketRoomun içinde değilse kapat
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "From: ${remoteMessage.from}")

        // TODO: 17.07.2021   notification dan data alınacak type a göre (sadece roomu kapa) currentRoomID, ondestroyda sikişşşşşşşşşş
        //Use this condition to validation login
        if (PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn == false) {
            return
        } else if (remoteMessage.data.isNotEmpty()) {

            if (remoteMessage.data["type"].equals("message")) {


                if (PethiioApplication.getCurrentRoom() != 0 || remoteMessage.data["roomId"] == PethiioApplication.getCurrentRoom()
                        .toString()
                ) {


                } else {

                    val notificationUtils = NotificationUtils(PethiioApplication.context)
                    val notification = remoteMessage.notification

                    //region Message

                    if (remoteMessage.data["type"].equals("message")) {

                        notificationUtils.showNotificationMessage(
                            notification?.title,
                            notification?.body,
                            "message",
                            null
                        )
                    }

                }
            }


            //endregion

        }
    }


    companion object {
        private const val TAG = "FcmMessageService"
    }
}