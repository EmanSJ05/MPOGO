package com.emansj.mpogo.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.emansj.mpogo.R;
import com.emansj.mpogo.activity.NotificationItemActivity;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.AppOten;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


public class FcmMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFMService";
    String type = "";
    private AppGlobal m_Global;

//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // Handle data payload of FCM messages.
//        Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
//        Log.d(TAG, "FCM Notification Message: " + remoteMessage.getNotification());
//        Log.d(TAG, "FCM Data Message: " + remoteMessage.getData());
//
//        if (remoteMessage.getData().size() > 0){
//            type = "json";
//            sendNotification(remoteMessage.getData().toString());
//        }
//        if (remoteMessage.getData() != null){
//            type = "message";
//            sendNotification(remoteMessage.getNotification().getBody());
//        }
//    }
//
//    @Override
//    public void onNewToken(String s) {
//        super.onNewToken(s);
//        //Toast.makeText(this, "New Token nih bro " + s.toString(), Toast.LENGTH_LONG).show();
//        sendRegistrationTokenToServer(s);
//    }
//
//    private void sendRegistrationTokenToServer(final String token) {
//        if (m_Global.getUserLoginId() != 0) {
//            m_Global.setUserToken(token);
//
//            //update token to API
//            AppOten appOten = new AppOten(getApplicationContext());
//            appOten.UpdateToken(token, null);
//        }
//    }
//
//    private void sendNotification(String messageBody){
//        String id="", message="", title="";
//
//        if(type.equals("json")){
//            try {
//                JSONObject jsonObject = new JSONObject(messageBody);
//                JSONObject dataObject = jsonObject.getJSONObject("data");
//                id = dataObject.getString("id");
//                title = dataObject.getString("title");
//                message = dataObject.getString("message");
//            } catch (JSONException e) {
//
//            }
//        } else if(type.equals("message")) {
//            message = messageBody;
//        }
//
//
//        Intent intent = new Intent(this, NotificationItemActivity.class);
//        intent.putExtra("title", title);
//        intent.putExtra("subtitle", message);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//
//        //NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createChannel();
//        }
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
//        //notificationBuilder.setContentTitle(getString(R.string.app_name));
//        notificationBuilder.setContentTitle(title);
//        notificationBuilder.setContentText(message);
//        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        notificationBuilder.setSound(soundUri);
//        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
//        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher));
//        notificationBuilder.setAutoCancel(true);
//        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);;
//        v.vibrate(1000);
//        notificationBuilder.setContentIntent(pendingIntent);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0, notificationBuilder.build());
//
//    }
//
//    private static final String CHANNEL_ID = "media_playback_channel";
//    @RequiresApi(Build.VERSION_CODES.O)
//    private void createChannel() {
//        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//        // The id of the channel.
//        String id = CHANNEL_ID;
//        // The user-visible name of the channel.
//        CharSequence name = "Media playback";
//        // The user-visible description of the channel.
//        String description = "Media playback controls";
//        int importance = NotificationManager.IMPORTANCE_LOW;
//        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
//        // Configure the notification channel.
//        mChannel.setDescription(description);
//        mChannel.setShowBadge(true);
//        mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//        mNotificationManager.createNotificationChannel(mChannel);
//    }
}