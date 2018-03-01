package com.zpaz.tfsotg.Build;

import com.zpaz.tfsotg.Interfaces.ListedEntity;
import com.zpaz.tfsotg.Utils.DateTimeParser;
import com.zpaz.tfsotg.Utils.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by zsolt on 20/02/18.
 */

public class ListedBuild implements ListedEntity {

    private static JsonParser parser;
    private DateTimeParser dateTimeParser;
    private int id;
    private String buildNumber;
    private String buildDefinition;
    private String finishTime;
    private String createdBy;
    private String createdOn;
    private String startTime;
    private String result;

    private ListedBuild(int id, String buildNumber, String buildDefinition) {
        this.buildNumber = buildNumber;
        this.buildDefinition = buildDefinition;
        this.id = id;
        parser = new JsonParser();
        dateTimeParser = new DateTimeParser();
    }

    @Override
    public String toString() {
        return buildDefinition + " [" + result + "]" + "\n"
                + buildNumber + " (" + createdBy + ")" + "\n"
                + "Queued: " + dateTimeParser.parseDateFromDateTimeString(createdOn) + "@" + dateTimeParser.parseTimeFromDateTimeString(createdOn) + "\n"
                + "Started: " + dateTimeParser.parseDateFromDateTimeString(startTime) + "@" + dateTimeParser.parseTimeFromDateTimeString(startTime) + "\n"
                + "Finished: " + dateTimeParser.parseDateFromDateTimeString(finishTime) + "@" + dateTimeParser.parseTimeFromDateTimeString(finishTime);
    }

    public static LinkedList GetTfsBuilds(String response) throws JSONException {
        JSONArray builds = new JSONObject(response).getJSONArray("value");
        LinkedList<ListedBuild> buildList = new LinkedList<>();
        for (int i = 0; i < builds.length(); i++) {
            buildList.add(parseBuildFromResponse(builds.getJSONObject(i)));
        }
        return buildList;
    }

    private static ListedBuild parseBuildFromResponse(JSONObject tfsBuildJson) throws JSONException {
        ListedBuild build = new ListedBuild(
                tfsBuildJson.getInt("id"),
                tfsBuildJson.getString("buildNumber"),
                tfsBuildJson.getJSONObject("definition").getString("name"));
        build.setFinishTime(parser.getString(tfsBuildJson, "finishTime"));
        build.setCreatedBy(parser.getString(tfsBuildJson.getJSONObject("requestedFor"), "displayName"));
        build.setCreatedOn(parser.getString(tfsBuildJson, "queueTime"));
        build.setStartTime(parser.getString(tfsBuildJson, "startTime"));
        build.setResult(parser.getString(tfsBuildJson, "result"));
        return build;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getBuildDefinition() {
        return buildDefinition;
    }

    public void setBuildDefinition(String buildDefinition) {
        this.buildDefinition = buildDefinition;
    }

    private void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    private void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    private void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getStartTime() {
        return startTime;
    }

    private void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getResult() {
        return result;
    }

    private void setResult(String result) {
        this.result = result;
    }
}
