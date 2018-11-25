package com.emansj.mpogo.service;

import android.content.Intent;
import android.util.Log;

import com.emansj.mpogo.activity.MainActivity;
import com.emansj.mpogo.activity.NotificationItemActivity;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.AppOten;
import com.emansj.mpogo.helper.AppSharedPref;
import com.emansj.mpogo.helper.NotificationUtils;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.model.Notif;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class FcmService extends FirebaseMessagingService {
    private static final String NOTIFICATION_ID = "notificationid";
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String IMAGE_URL = "image_url";
    private static final String LINK_URL = "link_url";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            handleData(data);
        }
    }

    private void handleData(Map<String, String> data) {
        Integer notificationId = Tools.parseInt(data.get(NOTIFICATION_ID));
        String title = data.get(TITLE);
        String message = data.get(MESSAGE);
        String imageUrl = data.get(IMAGE_URL);
        String linkUrl = data.get(LINK_URL);

        Notif notif = new Notif();
        notif.NotificationId = notificationId;
        notif.Title = title;
        notif.Message = message;
        notif.ImageUrl = imageUrl;
        notif.LinkUrl = linkUrl;
        notif.ReceivedDate = Tools.convertDateDTS(Calendar.getInstance().getTime(), "yyyy-MM-dd HH:mm");

        //Intent resultIntent = new Intent(getApplicationContext(), NotificationItemActivity.class);

        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notif);
        //notificationUtils.playNotificationSound();
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        //Toast.makeText(this, "New Token nih bro " + s.toString(), Toast.LENGTH_LONG).show();
        sendRegistrationTokenToServer(s);
    }

    private void sendRegistrationTokenToServer(final String token) {
        AppSharedPref.init(getApplicationContext());
        int userId = AppSharedPref.read(AppSharedPref.USER_ID, 0);

        if (userId != 0) {
            AppGlobal global = AppGlobal.getInstance(getApplicationContext());
            global.setUserToken(token);

            //update token to API
            AppOten appOten = new AppOten(getApplicationContext());
            appOten.UpdateToken(token);
        }
    }
}
