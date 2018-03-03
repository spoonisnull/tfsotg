package com.zpaz.tfsotg.Utils;

/**
 * Created by zsolt on 25/02/18.
 */

public class DateTimeParser {

    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String second;

    public DateTimeParser(String dateTimeString) {
        parseDateTime(dateTimeString);
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public void parseDateTime(String dateTimeString){
        if (dateTimeString.split("T").length >= 2) {
            setYear(dateTimeString.split("T")[0].split("-")[0].substring(2, 4));
            setMonth(dateTimeString.split("T")[0].split("-")[1]);
            setDay(dateTimeString.split("T")[0].split("-")[2]);
            setHour(dateTimeString.split("T")[1].split(":")[0]);
            setMinute(dateTimeString.split("T")[1].split(":")[1]);
            setSecond(dateTimeString.split("T")[1].split(":")[2].substring(0,2));
        }
    }

    public String getDateWithYear(){
        return String.valueOf(getDay() + "/" + getMonth() + "/" + getYear());
    }

    public String getDate(){
        return String.valueOf(getDay() + "/" + getMonth());
    }

    public String getTimeWithSeconds(){
        return String.valueOf(getHour() + ":" + getMinute() + ":" + getSecond());
    }

    public String getTime(){
        return String.valueOf(getHour() + ":" + getMinute());
    }
}
