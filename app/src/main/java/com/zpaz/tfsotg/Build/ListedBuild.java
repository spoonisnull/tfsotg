package com.zpaz.tfsotg.Build;

import com.zpaz.tfsotg.Interfaces.ListedEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by zsolt on 20/02/18.
 */

public class ListedBuild implements ListedEntity {

    private String buildNumber;
    private String buildDefinition;
    private String finishTime;
    private String createdBy;
    private String createdOn;
    private String startTime;
    private String result;

    private ListedBuild(String id, String buildNumber, String buildDefinition) {
        this.buildNumber = buildNumber;
        this.buildDefinition = buildDefinition;
    }

    @Override
    public String toString() {
        return buildDefinition + " [" + result + "]" + "\n"
                + buildNumber + " (" + createdBy + ")" + "\n"
                + "Queued:   " + createdOn + "\n"
                + "Started:  " + startTime + "\n"
                + "Finished: " + finishTime;

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
                tfsBuildJson.getString("id"),
                tfsBuildJson.getString("buildNumber"),
                tfsBuildJson.getJSONObject("definition").getString("name"));
        build.setFinishTime(tfsBuildJson.getString("finishTime"));
        build.setCreatedBy(tfsBuildJson.getJSONObject("requestedFor").getString("displayName"));
        build.setCreatedOn(tfsBuildJson.getString("queueTime"));
        build.setStartTime(tfsBuildJson.getString("startTime"));
        build.setResult(tfsBuildJson.getString("result"));
        return build;
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

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
