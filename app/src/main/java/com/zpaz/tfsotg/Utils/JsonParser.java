package com.zpaz.tfsotg.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zsolt on 01/03/18.
 */

public class JsonParser {

    public String getString(JSONObject object, String key) throws JSONException {
        if(object.has(key)){
            return object.getString(key);
        } else {
            return "in progress";
        }
    }

    public int getInt(JSONObject object, String key) throws JSONException {
        if(object.has(key)){
            return object.getInt(key);
        } else {
            return -1;
        }
    }
}
