package com.pethiio.android.ui.main.util

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.pethiio.android.R
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by sem0025 on 23.03.2017.
 */
class NotificationUtils(private val mContext: Context) {
    var CHANNEL_ID: String = "Pethiio"

    fun showNotificationMessage(
        title: String?,
        message: String?,
        intent: Intent?,
        imageUrl: String?
    ) {
        // notification icon
        var resultPendingIntent: PendingIntent? = null
        if (intent != null) {
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            resultPendingIntent = PendingIntent.getActivity(
                mContext,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(message)) {
            showSmallNotification(title!!, message!!, resultPendingIntent)
        }
    }

    private fun showSmallNotification(
        title: String,
        message: String,
        resultPendingIntent: PendingIntent?
    ) {

        val mBuilder = NotificationCompat.Builder(mContext, CHANNEL_ID)
        mBuilder
            .setChannelId(CHANNEL_ID)
            .setSmallIcon(R.drawable.app_icon)
            .setColor(ContextCompat.getColor(mContext, android.R.color.transparent))
//            .setLargeIcon(BitmapFactory.decodeResource(mContext.resources, icon))
            .setContentTitle(HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY))
            .setContentText(HtmlCompat.fromHtml(message, HtmlCompat.FROM_HTML_MODE_LEGACY))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle(
                        HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                    )
                    .bigText(
                        HtmlCompat.fromHtml(message, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                    )
            )
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
        mBuilder.setVibrate(longArrayOf(0, 100, 220, 120))
        if (resultPendingIntent != null) mBuilder.setContentIntent(resultPendingIntent)
        mBuilder.setSound(RingtoneManager.getDefaultUri(2))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationManager =
                mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val mChannel = NotificationChannel(
                CHANNEL_ID,
                "Pethiio",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(mChannel)
            notificationManager.notify(createID(), mBuilder.build())

        } else {
            val notificationManager =
                NotificationManagerCompat.from(mContext)
            notificationManager.notify(createID(), mBuilder.build())
        }
    }


    fun createID(): Int {
        val now = Date()
        return SimpleDateFormat("ddHHmmss", Locale.US).format(now).toInt()
    }


}