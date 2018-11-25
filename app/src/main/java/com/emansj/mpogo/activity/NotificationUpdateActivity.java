package com.emansj.mpogo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.model.Notif;


public class NotificationUpdateActivity extends AppCompatActivity {


    //Standard vars
    private static final String TAG = NotificationUpdateActivity.class.getSimpleName();
    private Context m_Ctx;
    private AppGlobal m_Global;

    //Custom vars
    private Notif m_Notif;
    private int m_NotificationId;
    private String m_LinkUrl;


    //---------------------------------------OVERRIDE
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_notification_item);

        m_Ctx = NotificationUpdateActivity.this;

        //Get extras
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            m_NotificationId = (int) extras.get("notificationid");
            m_LinkUrl = (String) extras.get("link_url");

            m_Notif = new Notif();
            m_Notif.setHasBeenRead(m_Ctx, m_NotificationId);

        }
        finish();
    }

}
