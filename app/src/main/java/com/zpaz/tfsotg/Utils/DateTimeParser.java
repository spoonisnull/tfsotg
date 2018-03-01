package com.zpaz.tfsotg.Utils;

/**
 * Created by zsolt on 25/02/18.
 */

public class DateTimeParser {
    public String parseDateFromDateTimeString(String dateTimeString){
        return dateTimeString.split("T")[0];
    }

    public String parseTimeFromDateTimeString(String dateTimeString){
        return dateTimeString.split("T")[1].split("\\.")[0];
    }
}
