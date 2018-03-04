package com.zpaz.tfsotg.Build;

import com.zpaz.tfsotg.Enums.EntityStatus;
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
    private DateTimeParser queuedDateTime;
    private DateTimeParser startedDateTime;
    private DateTimeParser finishedDateTime;
    private int id;
    private String buildNumber;
    private String buildDefinition;
    private String finishTime;
    private String createdBy;
    private String createdOn;
    private String startTime;
    private EntityStatus result;

    private ListedBuild(int id, String buildNumber, String buildDefinition, String createdBy, String createdOn, String finishTime, String startTime, EntityStatus result) {
        this.buildNumber = buildNumber;
        this.buildDefinition = buildDefinition;
        this.id = id;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.finishTime = finishTime;
        this.startTime = startTime;
        this.result = result;

        queuedDateTime = new DateTimeParser(getCreatedOn());
        startedDateTime = new DateTimeParser(getStartTime());
        finishedDateTime = new DateTimeParser(getFinishTime());
    }

    @Override
    public String toString() {
        return buildDefinition + " [" + result + "]" + "\n"
                + buildNumber + " (" + createdBy + ")" + "\n"
                + "Queued: " + queuedDateTime.getDate() + "@" + queuedDateTime.getTime() + "\n"
                + "Started: " + startedDateTime.getDate() + "@" + startedDateTime.getTime() + "\n"
                + "Finished: " + finishedDateTime.getDate() + "@" + finishedDateTime.getTime();
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
        parser = new JsonParser();
        ListedBuild build = new ListedBuild(
                tfsBuildJson.getInt("id"),
                tfsBuildJson.getString("buildNumber"),
                tfsBuildJson.getJSONObject("definition").getString("name"),
                parser.getString(tfsBuildJson.getJSONObject("requestedFor"), "displayName"),
                parser.getString(tfsBuildJson, "queueTime"),
                parser.getString(tfsBuildJson, "finishTime"),
                parser.getString(tfsBuildJson, "startTime"),
                EntityStatus.valueOf(parser.getString(tfsBuildJson, "result")));
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

    public String getFinishTime() {
        return finishTime;
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

    @Override
    public String getTime() {
        return getStartTime();
    }

    @Override
    public String getName() {
        return getBuildNumber();
    }

    @Override
    public EntityStatus getStatus() {
        return getResult();
    }

    @Override
    public String getDefinition() {
        return getBuildDefinition();
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

    public EntityStatus getResult() {
        return result;
    }

    private void setResult(EntityStatus result) {
        this.result = result;
    }
}
