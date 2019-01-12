package com.emansj.mpogo.model;

import android.content.Context;
import android.provider.Settings;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.emansj.mpogo.activity.LoginActivity;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.VolleyErrorHelper;
import com.emansj.mpogo.helper.VolleySingleton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserOten {

    //Model
    public int UseId;
    public String UserName;
    public String UserPass;
    public String AndroidId;
    public String Token;
    public String Topic;

    //Constructor
    public UserOten() {}
}

