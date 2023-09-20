package com.aait.getak.fcm

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.aait.getak.R
import com.aait.getak.ui.activities.core.ChatActivity
import com.aait.getak.ui.activities.core.MapPreviewActivity
import com.aait.getak.ui.activities.core.TrackingActivity
import com.aait.getak.ui.activities.notifications.BidsActivity
import com.aait.getak.ui.activities.notifications.BillActivity
import com.aait.getak.ui.activities.notifications.CaptinDetailsActivity
import com.aait.getak.ui.activities.splash.SplashActivity
import com.aait.getak.utils.GlobalPreferences
import com.aait.getak.utils.Util
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import kotlin.random.Random


class MessageService: FirebaseMessagingService() {
    private var msg: String?=""
    companion object{
        var orderId_:MutableLiveData<Int> = MutableLiveData()
        var state_:MutableLiveData<String> = MutableLiveData()
        var laterOrder:MutableLiveData<String> = MutableLiveData()

    }
    override fun onMessageReceived(rMsg: RemoteMessage) {
        super.onMessageReceived(rMsg)
        Log.e("fcm",Gson().toJson(rMsg.toString()))
        Log.e("fcm", rMsg.data.toString())
        rMsg.let {
            val message_ar = it.data["message_ar"]
            val message_en = it.data["message_en"]
            val key = it.data["key"]
            val orderId=it.data["order_id"] ?: "0"



            val bidId=it.data["bid_id"] ?: "0"
            val title=it.data["title"]
            msg = if (Util.language=="ar"){
                message_ar
            } else{
                message_en
            }
            when(key!!){
                "new_message"->{
                    val conversation_id=it.data["conversation_id"]
                    val intent = Intent(this, ChatActivity::class.java)
                    intent.putExtra("conv_id", conversation_id!!.toInt())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    sendNormalNotification(msg,title,intent)
                }
                "AcceptOrder"->{
                   // goCaptain(orderId.toInt())
                    if (it.data["type"]=="later" && !TrackingActivity.isTrackingVisible){
                        sendNormalNotification(
                            title,
                            TrackingActivity::class.java,
                            msg,
                            orderId.toInt())
                    } else {
                        if (TrackingActivity.isTrackingVisible){
                            orderId_.postValue(orderId.toInt())
                            state_.postValue(key)

                        }
                        sendNormalNotification(
                            title,
                            CaptinDetailsActivity::class.java,
                            msg,
                            orderId.toInt(),
                            bidId.toInt()
                        )
                    }
                }
                "ConfirmfinishSimpleOrder"->{
                   // goCaptain(orderId.toInt())
                   // sendNormalNotification(title,CaptinDetailsActivity::class.java,msg,orderId.toInt(),bidId.toInt())

                    if (it.data["type"]=="later" && !TrackingActivity.isTrackingVisible){
                        sendNormalNotification(
                            title,
                            TrackingActivity::class.java,
                            msg,
                            orderId.toInt())
                    }
                    else {
                        if (TrackingActivity.isTrackingVisible){
                            orderId_.postValue(orderId.toInt())
                            state_.postValue(key)

                        }
                        sendNormalNotification(
                            title,
                            CaptinDetailsActivity::class.java,
                            msg,
                            orderId.toInt(),
                            bidId.toInt()
                        )
                    }

                }
                "inWayToOrder"->{
                  //  goCaptain(orderId.toInt())
                    if (it.data["type"]=="later" && !TrackingActivity.isTrackingVisible){
                        sendNormalNotification(
                            title,
                            TrackingActivity::class.java,
                            msg,
                            orderId.toInt())
                    }
                    else {
                        if (TrackingActivity.isTrackingVisible){
                            orderId_.postValue(orderId.toInt())
                            state_.postValue(key)

                        }
                        sendNormalNotification(
                            title,
                            CaptinDetailsActivity::class.java,
                            msg,
                            orderId.toInt(),
                            bidId.toInt()
                        )
                    }

                }
                "ArrivedToJoined"->{
                  //  goCaptain(orderId.toInt())
                    if (it.data["type"]=="later" && !TrackingActivity.isTrackingVisible){
                        sendNormalNotification(
                            title,
                            TrackingActivity::class.java,
                            msg,
                            orderId.toInt())
                    }
                    else {
                        if (TrackingActivity.isTrackingVisible){
                            orderId_.postValue(orderId.toInt())
                            state_.postValue(key)

                        }
                        sendNormalNotification(title,CaptinDetailsActivity::class.java,msg,orderId.toInt(),bidId.toInt())
                    }


                }
                "AcceptJoin"->{
                    //goCaptain(orderId.toInt())

                    sendNormalNotification(title,CaptinDetailsActivity::class.java,msg,orderId.toInt(),bidId.toInt())

                }
                "inWayToJoinOrder"->{
                    //goCaptain(orderId.toInt())
                    if (it.data["type"]=="later" && !TrackingActivity.isTrackingVisible){
                        sendNormalNotification(
                            title,
                            TrackingActivity::class.java,
                            msg,
                            orderId.toInt())
                    }
                    else {
                        if (TrackingActivity.isTrackingVisible){
                            orderId_.postValue(orderId.toInt())
                            state_.postValue(key)

                        }
                        sendNormalNotification(title,CaptinDetailsActivity::class.java,msg,orderId.toInt(),bidId.toInt())
                    }


                }
                "arrivedToOrder"->{
                    //goCaptain(orderId.toInt())
                    if (it.data["type"]=="later" && !TrackingActivity.isTrackingVisible){
                        sendNormalNotification(
                            title,
                            TrackingActivity::class.java,
                            msg,
                            orderId.toInt())
                    }
                    else {
                        if (TrackingActivity.isTrackingVisible){
                            orderId_.postValue(orderId.toInt())
                            state_.postValue(key)

                        }
                        sendNormalNotification(title,CaptinDetailsActivity::class.java,msg,orderId.toInt(),bidId.toInt())
                    }


                }
                "startJourney"->{
                    //goCaptain(orderId.toInt())
                    if (it.data["type"]=="later" && !TrackingActivity.isTrackingVisible){
                        sendNormalNotification(
                            title,
                            TrackingActivity::class.java,
                            msg,
                            orderId.toInt())
                    }
                    else {
                        if (TrackingActivity.isTrackingVisible){
                            orderId_.postValue(orderId.toInt())
                            state_.postValue(key)

                        }
                        sendNormalNotification(title,CaptinDetailsActivity::class.java,msg,orderId.toInt(),bidId.toInt())
                    }


                }
                "withdrawOrder"->{
                   // goHome()
                    state_.postValue(key)
                    sendNormalNotification(title,
                        MapPreviewActivity::class.java,msg,orderId.toInt(),0)

                }
                "InWayToJoined"->{
                    //goCaptain(orderId.toInt())
                    sendNormalNotification(title,CaptinDetailsActivity::class.java,msg,orderId.toInt(),bidId.toInt())
                }
                "newCaptainJourney"->{
                   // sendNormalNotification(title,PrevDetialsMapActivity::class.java,msg,orderId.toInt(),bidId.toInt())
                }

                "finishSimpleOrder"->{
                    //goBill(orderId.toInt())
                  //  sendNormalNotification(title,BillActivity::class.java,msg,orderId.toInt(),bidId.toInt())
                    if (it.data["type"]=="later" && !TrackingActivity.isTrackingVisible){
                        sendNormalNotification(
                            title,
                            TrackingActivity::class.java,
                            msg,
                            orderId.toInt())
                    }
                    else {
                        if (TrackingActivity.isTrackingVisible){
                            orderId_.postValue(orderId.toInt())
                            state_.postValue(key)

                        }
                        sendNormalNotification(
                            title,
                            CaptinDetailsActivity::class.java,
                            msg,
                            orderId.toInt(),
                            bidId.toInt()
                        )
                    }
                    //sendNormalNotification(title,BillActivity::class.java,msg,orderId.toInt(),bidId.toInt())

                }
                "finishMultiUser"->{
                    //goBill(orderId.toInt())
                    sendNormalNotification(title,BillActivity::class.java,msg,orderId.toInt(),bidId.toInt())
                    //   sendNormalNotification(title,BillActivity::class.java,msg,orderId.toInt(),bidId.toInt())
                }
                "balance_transfer"->{
                    sendNormalNotification(title,
                        MapPreviewActivity::class.java,msg,orderId.toInt())
                }
                "deleteUser"->{
                    GlobalPreferences(applicationContext).logout()
                    val intent = Intent(applicationContext, SplashActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    applicationContext.startActivity(intent)
                    (applicationContext as Activity).finish()
                }
                "blockUser"->{
                    GlobalPreferences(applicationContext).logout()
                    val intent = Intent(applicationContext, SplashActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    applicationContext.startActivity(intent)
                    (applicationContext as Activity).finish()
                }
                "applyBid"->{
                    sendNormalNotification(title,BidsActivity::class.java,msg,orderId.toInt(),bidId.toInt())
                }
                "finished"->{
                    if (TrackingActivity.isTrackingVisible){
                        orderId_.postValue(orderId.toInt())
                        state_.postValue("finished")

                    }
                    sendNormalNotification(title,TrackingActivity::class.java,msg,
                        orderId.toInt())
                }
//                "exit_user"->{
//                    GlobalPreferences(this).logout()
//                    var intent = Intent(this, SplashActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                    startActivity(intent)
//                }
                else->{
                    sendNormalNotification(title,
                        MapPreviewActivity::class.java,msg,orderId.toInt(),bidId.toInt(),notif = true)
                }
            }


            }

    }

    private fun goHome() {
        val intent = Intent(applicationContext, MapPreviewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun goCaptain(orderId: Int){
        val intent = Intent(applicationContext, CaptinDetailsActivity::class.java)
        intent.putExtra("order_id", orderId)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
    private fun goBill(orderId: Int){
        val intent = Intent(applicationContext, BillActivity::class.java)
        intent.putExtra("order_id",orderId.toInt())
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
    private fun sendNormalNotification(
        message: String?,
        title: String?,
        intent: Intent
    ) {
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val defaultUri =Uri.parse(
            "android.resource://"
                    + applicationContext!!.packageName + "/"
                    +  R.raw.notify
        )
        val builder =
            NotificationCompat.Builder(applicationContext)
        builder.setSound(defaultUri)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSmallIcon(R.mipmap.logo)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //for android 8 +26 api notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val NOTIFICATION_CHANNEL_ID = "10006"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "NOTIFICATION_CHANNEL_NAME",
                importance
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.vibrationPattern = longArrayOf(
                100,
                200,
                300,
                400,
                500,
                400,
                300,
                200,
                400
            )

            builder.setChannelId(NOTIFICATION_CHANNEL_ID)
            notificationChannel.setSound(
                Uri.parse(
                    "android.resource://"
                            + applicationContext!!.packageName + "/"
                            +  R.raw.notify
                ), AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build())
//            val audioAttributes = AudioAttributes.Builder()
//                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                .build()
//
//            notificationChannel.setSound(defaultUri,audioAttributes)
            manager.createNotificationChannel(notificationChannel)
        }
        manager.notify(Random.nextInt(), builder.build())
    }
    private fun sendNormalNotification(title:String?,activity: Class<*>, msg: String?, orderId: Int, bidId: Int,wallet:Boolean?=false,notif:Boolean?=false) {
        val intent = Intent(applicationContext, activity)
        intent.putExtra("order_id",orderId)
        intent.putExtra("bid_id",bidId)
        intent.putExtra("wallet",wallet)
        intent.putExtra("notif",notif)

        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT/*Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK*/)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val defaultUri = Uri.parse(
            "android.resource://"
                    + applicationContext!!.packageName + "/"
                    +  R.raw.notify
        )
        val builder = NotificationCompat.Builder(applicationContext)
        builder.setSound(defaultUri)
            .setContentTitle(title)
            .setContentText(msg)
            .setAutoCancel(true)
            .setSmallIcon(R.mipmap.logo)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //for android 8 +26 api notification
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val NOTIFICATION_CHANNEL_ID = (orderId + 35).toString()
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "EzzakChannel1", importance)
            notificationChannel.enableLights(true)
            notificationChannel.setShowBadge(true)
//            notificationChannel.setSound(defaultUri, AudioAttributes.Builder()
//                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                .build()
//            )
            notificationChannel.setSound(
                Uri.parse(
                    "android.resource://"
                            + applicationContext!!.packageName + "/"
                            +  R.raw.notify
                ), AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build())
            notificationChannel.lightColor = Color.RED

            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            builder.setChannelId(NOTIFICATION_CHANNEL_ID)
            if (GlobalPreferences(applicationContext).notifNum > 0) {
                builder.setNumber(GlobalPreferences(applicationContext).notifNum)
            }
            manager.createNotificationChannel(notificationChannel)
        }
        manager.notify(Random.nextInt(), builder.build())
    }
    private fun sendNormalNotification(title:String?,activity: Class<*>, msg: String?, orderId: Int) {

        val intent = Intent(applicationContext, activity)
        intent.putExtra("order_id",orderId)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val defaultUri = Uri.parse(
            "android.resource://"
                    + applicationContext!!.packageName + "/"
                    +  R.raw.notify
        )
        val builder = NotificationCompat.Builder(applicationContext)
        builder.setSound(defaultUri)
            .setContentTitle(title)
            .setContentText(msg)
            .setAutoCancel(true)
            .setSmallIcon(R.mipmap.logo)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //for android 8 +26 api notification
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val NOTIFICATION_CHANNEL_ID = (orderId + 35).toString()
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "EzzakChannel2", importance)
            notificationChannel.enableLights(true)
            notificationChannel.setShowBadge(true)
//            notificationChannel.setSound(defaultUri, AudioAttributes.Builder()
//                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                .build()
//            )
            notificationChannel.setSound(
                Uri.parse(
                    "android.resource://"
                            + applicationContext!!.packageName + "/"
                            +  R.raw.notify
                ), AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build())
            notificationChannel.lightColor = Color.RED

            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            builder.setChannelId(NOTIFICATION_CHANNEL_ID)
            if (GlobalPreferences(applicationContext).notifNum > 0) {
                builder.setNumber(GlobalPreferences(applicationContext).notifNum)
            }
            manager.createNotificationChannel(notificationChannel)
        }
        manager.notify(Random.nextInt(), builder.build())

    }


}


