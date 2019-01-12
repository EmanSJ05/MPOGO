package com.emansj.mpogo.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import com.emansj.mpogo.R;
import com.emansj.mpogo.activity.NotificationItemActivity;
import com.emansj.mpogo.activity.NotificationUpdateActivity;
import com.emansj.mpogo.model.Notif;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.NOTIFICATION_SERVICE;


public class NotificationUtils {
    private static final int NOTIFICATION_ID = 200;
    private static final String CHANNEL_ID = "myChannel";
    private Context m_Ctx;
    private String m_Title;
    private String m_Message;
    private String m_ImageUrl;
    private String m_LinkUrl;
    private Bitmap m_BitmapImage;
    private PendingIntent m_PendingIntent;
    private Uri m_AlarmSound1;
    private Uri m_AlarmSound2;
    private Uri m_AlarmSound3;

    public NotificationUtils(Context ctx) {
        this.m_Ctx = ctx;
        this.m_AlarmSound1 = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + m_Ctx.getPackageName() + "/raw/notification");
        this.m_AlarmSound2 = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + m_Ctx.getPackageName() + "/raw/filling_you_inbox");
        this.m_AlarmSound3 = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + m_Ctx.getPackageName() + "/raw/quite_impressed");
    }

    public void displayNotification(Notif notif) {
        {
            Integer notifid = notif.NotificationId;
            String message = notif.Message;
            String title = notif.Title;
            String imageUrl = Tools.parseString(notif.ImageUrl);
            String linkUrl = Tools.parseString(notif.LinkUrl);
            Bitmap bitmapImage = null;
            Intent resultIntent;

            if (Tools.parseString(imageUrl) != null) {
                bitmapImage = getBitmapFromURL(imageUrl);
            }
            final int icon = R.mipmap.ic_launcher;

            PendingIntent resultPendingIntent;

            if (Tools.parseString(linkUrl) != null) { //this is a link notification
                //result (to run setHasBeenRead only)
                resultIntent = new Intent(m_Ctx, NotificationUpdateActivity.class);
                resultIntent.putExtra("notificationid", notifid);
                resultIntent.putExtra("link_url", linkUrl);

                //notification
                Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl));
                //resultPendingIntent = PendingIntent.getActivity(m_Ctx, 0, notificationIntent, 0);

                Intent[] intens = {resultIntent, notificationIntent};
                resultPendingIntent = PendingIntent.getActivities(m_Ctx, 0, intens, PendingIntent.FLAG_UPDATE_CURRENT);

            } else {
                //notification
                Intent notificationIntent = new Intent(m_Ctx, NotificationItemActivity.class);
                notificationIntent.putExtra("notificationid", notifid);

                Intent[] intens = {notificationIntent};
                resultPendingIntent = PendingIntent.getActivities(m_Ctx, 0, intens, PendingIntent.FLAG_CANCEL_CURRENT);

            }
            notif.addNotif(m_Ctx, notif);


            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(m_Ctx, CHANNEL_ID);
            Notification notification;

            if (bitmapImage == null) {
                NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
                bigTextStyle.bigText(message);
                notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentIntent(resultPendingIntent)
                        .setStyle(bigTextStyle)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(m_Ctx.getResources(), icon))
                        .setSound(m_AlarmSound2)
                        .setVibrate(new long[] {0, 300, 50, 300})
                        .setDefaults(Notification.DEFAULT_LIGHTS)
                        .build();

            } else {
                NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                bigPictureStyle.setBigContentTitle(title);
                bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
                bigPictureStyle.bigPicture(bitmapImage);
                notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentIntent(resultPendingIntent)
                        .setStyle(bigPictureStyle)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(m_Ctx.getResources(), icon))
                        .setContentText(message)
                        .setSound(m_AlarmSound2)
                        .setVibrate(new long[] {0, 300, 50, 300})
                        .setDefaults(Notification.DEFAULT_LIGHTS)
                        .build();
            }

            NotificationManager notificationManager = (NotificationManager) m_Ctx.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }

    private Bitmap getBitmapFromURL(String strURL) {
        try {
            java.net.URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + m_Ctx.getPackageName() + "/raw/quite_impressed");
            Ringtone r = RingtoneManager.getRingtone(m_Ctx, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public void displayNotificationx(Notif notif, Intent resultIntent) {
//        {
//            Integer notifid = notif.NotificationId;
//            m_Title = notif.Title;
//            m_Message = notif.Message;
//            m_ImageUrl = notif.ImageUrl;
//            m_LinkUrl = notif.LinkUrl;
//            m_BitmapImage = null;
//
//            if (!m_ImageUrl.equals("")) {
//                m_BitmapImage = getBitmapFromURL(m_ImageUrl);
//            }
//            final int icon = R.mipmap.ic_launcher;
//
//            PendingIntent resultPendingIntent;
//
//            if (!m_LinkUrl.equals("")) { //this is a link notification
//                //result (to run setHasBeenRead only)
//                resultIntent = new Intent(m_Ctx, NotificationUpdateActivity.class);
//                resultIntent.putExtra("notificationid", notifid);
//                resultIntent.putExtra("link_url", m_LinkUrl);
//
//                //notification
//                Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(m_LinkUrl));
//                //resultPendingIntent = PendingIntent.getActivity(m_Ctx, 0, notificationIntent, 0);
//
//                Intent[] intens = {resultIntent, notificationIntent};
//                //resultPendingIntent = PendingIntent.getActivities(m_Ctx, 0, intens, PendingIntent.FLAG_UPDATE_CURRENT);
//                m_PendingIntent = PendingIntent.getActivities(m_Ctx, 0, intens, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            } else {
//                //notification
//                Intent notificationIntent = new Intent(m_Ctx, NotificationItemActivity.class);
//                notificationIntent.putExtra("notificationid", notifid);
//
//                Intent[] intens = {notificationIntent};
//                //resultPendingIntent = PendingIntent.getActivities(m_Ctx, 0, intens, PendingIntent.FLAG_CANCEL_CURRENT);
//                m_PendingIntent = PendingIntent.getActivities(m_Ctx, 0, intens, PendingIntent.FLAG_CANCEL_CURRENT);
//
//            }
//            notif.addNotif(m_Ctx, notif);
//
//            if (m_BitmapImage == null) {
//                //largeTextNotification();
//
//            } else {
//                //imageNotification();
//            }
//        }
//    }
//
//    public void largeTextNotification() {
//        final int icon = R.mipmap.ic_launcher;
//
//        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
//        bigTextStyle.bigText(m_Message);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(m_Ctx, CHANNEL_ID)
//                .setSmallIcon(icon).setTicker(m_Title).setWhen(0)
//                .setAutoCancel(true)
//                .setContentTitle(m_Title)
//                .setContentIntent(m_PendingIntent)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(BitmapFactory.decodeResource(m_Ctx.getResources(), R.mipmap.ic_launcher))
//                .setStyle(bigTextStyle)
//                .setSound(m_AlarmSound3)
//                .setVibrate(new long[] {0, 300, 50, 300, 50, 300, 50, 800})
//                .setDefaults(Notification.DEFAULT_LIGHTS);
//
//
//        NotificationManager notificationManager = (NotificationManager) m_Ctx.getSystemService(NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String channelId2 = "2";
//            String channelName2 = "channel2";
//
//            NotificationChannel channel = new NotificationChannel(channelId2, channelName2, NotificationManager.IMPORTANCE_DEFAULT);
//
//            channel.enableLights(true);
//            channel.setLightColor(Color.RED);
//            channel.setShowBadge(true);
//            channel.enableVibration(true);
//
//            mBuilder.setChannelId(channelId2);
//
//            if (notificationManager != null) {
//                notificationManager.createNotificationChannel(channel);
//            }
//
//        }
//
//        if (notificationManager != null) {
//            notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//        }
//    }
//
//    public void imageNotification() {
//        int notifyId = 004;
//
//        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
//        bigPictureStyle.bigPicture(m_BitmapImage);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(m_Ctx, CHANNEL_ID)
//                .setAutoCancel(true)
//                .setContentTitle(m_Title)
//                .setContentIntent(m_PendingIntent)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(BitmapFactory.decodeResource(m_Ctx.getResources(), R.mipmap.ic_launcher))
//                .addAction(android.R.drawable.ic_menu_share, "Share", m_PendingIntent)
//                .setContentText(m_Message)
//                .setStyle(bigPictureStyle)
//                .setSound(m_AlarmSound2)
//                .setVibrate(new long[] {0, 300, 50, 300, 50, 300, 50, 800})
//                .setDefaults(Notification.DEFAULT_LIGHTS);
//
//
//        NotificationManager notificationManager = (NotificationManager) m_Ctx.getSystemService(NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String channelId2 = "2";
//            String channelName2 = "channel2";
//
//            NotificationChannel channel = new NotificationChannel(channelId2, channelName2, NotificationManager.IMPORTANCE_DEFAULT);
//
//            channel.enableLights(true);
//            channel.setLightColor(Color.RED);
//            channel.setShowBadge(true);
//            channel.enableVibration(true);
//
//            mBuilder.setChannelId(channelId2);
//
//            if (notificationManager != null) {
//                notificationManager.createNotificationChannel(channel);
//            }
//        }
//
//        if (notificationManager != null) {
//            notificationManager.notify(notifyId, mBuilder.build());
//        }
//    }

}
