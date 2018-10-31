package com.emansj.mpogo.helper;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import org.json.JSONObject;

import java.util.Map;

public class VolleyCustomStringRequest {

    private static volatile VolleyCustomStringRequest mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    private VolleyCustomStringRequest(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static VolleyCustomStringRequest getInstance(Context context) {
        if (mInstance == null) {
            synchronized (VolleyCustomStringRequest.class) {
                if (mInstance == null) mInstance = new VolleyCustomStringRequest(context);
            }
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            // Don't forget to start the volley request queue
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

}
