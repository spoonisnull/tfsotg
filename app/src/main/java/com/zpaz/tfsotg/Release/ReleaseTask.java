package com.zpaz.tfsotg.Release;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;

/**
 * Created by zsolt on 25/02/18.
 */

public class ReleaseTask {
    String id;
    String name;
    String rank;
    String startTime;
    String finishTime;
    String status;
    String[] issues;
    String agentName;
    String logUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getIssues() {
        return issues;
    }

    public void setIssues(String[] issues) {
        this.issues = issues;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    public static ReleaseTask GetReleaseTaskFromJson(JSONObject releaseTaskJson) throws JSONException {
        ReleaseTask releaseTask = new ReleaseTask();
        releaseTask.setId(releaseTaskJson.getString("id"));
        releaseTask.setName(releaseTaskJson.getString("name"));
        releaseTask.setRank(releaseTaskJson.getString("rank"));
        releaseTask.setStartTime(releaseTaskJson.getString("startTime"));
        releaseTask.setFinishTime(releaseTaskJson.getString("finishTime"));
        releaseTask.setStatus(releaseTaskJson.getString("status"));
        JSONArray issuesArray = releaseTaskJson.getJSONArray("issues");
        String[] issues = new String[issuesArray.length()];
        for(int i = 0; i < issuesArray.length(); i ++){
            issues[i] = issuesArray.getJSONObject(i).toString();
        }
        releaseTask.setIssues(issues);
        releaseTask.setAgentName(releaseTaskJson.getString("agentName"));
        releaseTask.setLogUrl(releaseTaskJson.getString("logUrl"));
        return releaseTask;
    }
}
