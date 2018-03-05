package com.zpaz.tfsotg.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zsolt on 01/03/18.
 */

public class JsonParser {

    public String getString(JSONObject object, String key) throws JSONException {
        if((object != null) && object.has(key)){
            return object.optString(key);
        } else {
            return "in progress";
        }
    }

    public int getInt(JSONObject object, String key) throws JSONException {
        if((object != null) && object.has(key)){
            return object.optInt(key);
        } else {
            return -1;
        }
    }
}
