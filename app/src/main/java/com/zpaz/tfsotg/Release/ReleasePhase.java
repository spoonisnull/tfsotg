package com.zpaz.tfsotg.Release;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zsolt on 25/02/18.
 */

public class ReleasePhase {
    String id;
    String rank;
    String phaseType;
    String status;
    ReleaseTask[] releaseTasks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPhaseType() {
        return phaseType;
    }

    public void setPhaseType(String phaseType) {
        this.phaseType = phaseType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ReleaseTask[] getReleaseTasks() {
        return releaseTasks;
    }

    public void setReleaseTasks(ReleaseTask[] releaseTasks) {
        this.releaseTasks = releaseTasks;
    }

    public static ReleasePhase GetReleasePhaseFromJson(JSONObject releasePhaseJson) throws JSONException {
        ReleasePhase phase = new ReleasePhase();
        phase.setId(releasePhaseJson.getString("id"));
        phase.setRank(releasePhaseJson.getString("rank"));
        phase.setPhaseType(releasePhaseJson.getString("phaseType"));
        phase.setStatus(releasePhaseJson.getString("status"));
        JSONArray releaseTasksJson = releasePhaseJson.getJSONArray("deploymentJobs").getJSONObject(0).getJSONArray("tasks");
        ReleaseTask[] tasks = new ReleaseTask[releaseTasksJson.length()];
        for(int i = 0; i < releaseTasksJson.length(); i ++){
            tasks[i] = ReleaseTask.GetReleaseTaskFromJson(releaseTasksJson.getJSONObject(i));
        }
        phase.setReleaseTasks(tasks);
        return phase;
    }
}
