package com.example.scorpiopk.checklist.utils;

import com.example.scorpiopk.checklist.utils.Defines;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ScorpioPK on 1/20/2018.
 */

public class JSONWrapper {
    JSONObject mJsonObject;
    public JSONWrapper() {
        mJsonObject = new JSONObject();
    }
    public JSONWrapper(JSONObject jsonObject) {
        mJsonObject = jsonObject;
    }

    public void PutStringInJSON(String tag, String value) {
        if (!Defines.DEFAULT_STRING.equals(value)) {
            try {
                mJsonObject.put(tag, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void PutIntInJSON(String tag, int value) {
        if (Defines.DEFAULT_VALUE != value) {
            try {
                mJsonObject.put(tag, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String GetStringFromJSON(String tag) {
        try {
            return mJsonObject.getString(tag);
        } catch (JSONException e) {
            return Defines.DEFAULT_STRING;
        }
    }
    public int GetIntFromJSON(String tag) {
        try {
            return mJsonObject.getInt(tag);
        } catch (JSONException e) {
            return Defines.DEFAULT_VALUE;
        }
    }
    public JSONObject GetJSON() {
        return mJsonObject;
    }
}
