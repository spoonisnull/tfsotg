package com.zpaz.tfsotg.Release;

import com.zpaz.tfsotg.Utils.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zsolt on 25/02/18.
 */

public class ReleaseTask {
    private String id;
    private String name;
    private String rank;
    private String startTime;
    private String finishTime;
    private String status;
    private String[] issues;
    private String agentName;
    private String logUrl;

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

    private void setRank(String rank) {
        this.rank = rank;
    }

    public String getStartTime() {
        return startTime;
    }

    private void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    private void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getStatus() {
        return status;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    public String[] getIssues() {
        return issues;
    }

    private void setIssues(String[] issues) {
        this.issues = issues;
    }

    public String getAgentName() {
        return agentName;
    }

    private void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getLogUrl() {
        return logUrl;
    }

    private void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    static ReleaseTask GetReleaseTaskFromJson(JSONObject releaseTaskJson) throws JSONException {
        JsonParser parser = new JsonParser();
        ReleaseTask releaseTask = new ReleaseTask();
        releaseTask.setId(parser.getString(releaseTaskJson,"id"));
        releaseTask.setName(parser.getString(releaseTaskJson,"name"));
        releaseTask.setRank(parser.getString(releaseTaskJson,"rank"));
        releaseTask.setStartTime(parser.getString(releaseTaskJson,"startTime"));
        releaseTask.setFinishTime(parser.getString(releaseTaskJson,"finishTime"));
        releaseTask.setStatus(parser.getString(releaseTaskJson,"status"));
        JSONArray issuesArray = releaseTaskJson.getJSONArray("issues");
        String[] issues = new String[issuesArray.length()];
        for(int i = 0; i < issuesArray.length(); i ++){
            issues[i] = issuesArray.getJSONObject(i).toString();
        }
        releaseTask.setIssues(issues);
        releaseTask.setAgentName(parser.getString(releaseTaskJson,"agentName"));
        releaseTask.setLogUrl(parser.getString(releaseTaskJson,"logUrl"));
        return releaseTask;
    }
}
