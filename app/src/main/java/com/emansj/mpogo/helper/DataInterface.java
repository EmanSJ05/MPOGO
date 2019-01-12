package com.emansj.mpogo.helper;

import org.json.JSONException;
import org.json.JSONObject;

public class DataInterface {
    public interface DataCallback {
        void onSuccess(JSONObject result) throws JSONException;
    }

}
