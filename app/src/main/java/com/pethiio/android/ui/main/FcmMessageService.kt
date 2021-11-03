package com.pethiio.android.ui.main


import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pethiio.android.PethiioApplication
import com.pethiio.android.data.EventBus.LoginEvent
import com.pethiio.android.data.EventBus.MatchEvent
import com.pethiio.android.ui.main.util.NotificationUtils
import com.pethiio.android.utils.PreferenceHelper
import org.greenrobot.eventbus.EventBus

class FcmMessageService : FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // If required send token to your app server.
//        sendRegistrationToServer(token)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "From: ${remoteMessage.from}")

        var userIdIndex = remoteMessage.from.toString().indexOf("user-")
        userIdIndex += 5

        val userId = remoteMessage.from.toString().substring(userIdIndex)

        if (PreferenceHelper.SharedPreferencesManager.getInstance().topicUserId.toString() != userId)
            return

        //Use this condition to validation login
        if (PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn == false) {
            return
        } else if (remoteMessage.data.isNotEmpty()) {

            if (remoteMessage.data["type"].equals("like")) {


                if (PethiioApplication.getCurrentRoom() == 0 && remoteMessage.data["roomId"] != PethiioApplication.getCurrentRoom()
                        .toString()
                ) {
                    val notificationUtils = NotificationUtils(PethiioApplication.context)

                        remoteMessage.data["memberId"]?.let { it1 ->
                            notificationUtils.showNotificationLike(
                                remoteMessage.data["title"],
                                remoteMessage.data["body"]
                            )
                        }


                }
            } else
                if (remoteMessage.data["type"].equals("message")) {


                    if (PethiioApplication.getCurrentRoom() == 0 && remoteMessage.data["roomId"] != PethiioApplication.getCurrentRoom()
                            .toString()
                    ) {
//                    EventBus.getDefault().post(
//                         MatchEvent(
//                                "DATING",
//                                73,
//                                2,
//                                "boncuk",
//                                "https://api.pethiio.com/api/files/b2eccd25-c8c1-4d69-9199-d58c89ba31e8-uri1.png",
//                                "charlie",
//                                "https://api.pethiio.com/api/files/b50770aa-5ed5-45df-8b1e-b4d5cc311634-uri1.png"))


                        val notificationUtils = NotificationUtils(PethiioApplication.context)
                        remoteMessage.data["roomId"]?.let {
                            remoteMessage.data["memberId"]?.let { it1 ->
                                notificationUtils.showNotificationMessage(
                                    remoteMessage.data["title"],
                                    remoteMessage.data["body"],
                                    it.toInt(),
                                    it1.toInt(),
                                    "message",
                                    null
                                )
                            }
                        }

                    }
                } else if (remoteMessage.data["type"].equals("match")) {

                    val notificationUtils = NotificationUtils(PethiioApplication.context)

                    EventBus.getDefault().post(remoteMessage.data["roomId"]?.let {
                        remoteMessage.data["memberId"]?.let { it1 ->
                            MatchEvent(
                                remoteMessage.data["purpose"],
                                it1.toInt(),
                                it.toInt(),
                                remoteMessage.data["sourceName"],
                                remoteMessage.data["sourceAvatar"],
                                remoteMessage.data["targetName"],
                                remoteMessage.data["targetAvatar"]
                            )
                        }
                    })
                    notificationUtils.showNotificationMatch(
                        remoteMessage.data["title"],
                        remoteMessage.data["body"],
                        remoteMessage.data["purpose"],
                        remoteMessage.data["memberId"]?.toInt(),
                        remoteMessage.data["roomId"]?.toInt(),
                        remoteMessage.data["sourceName"],
                        remoteMessage.data["sourceAvatar"],
                        remoteMessage.data["targetName"],
                        remoteMessage.data["targetAvatar"]
                    )


                }


            //endregion

        }
    }


    companion object {
        private const val TAG = "FcmMessageService"
    }
}