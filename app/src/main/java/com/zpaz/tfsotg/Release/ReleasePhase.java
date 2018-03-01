package com.zpaz.tfsotg.Release;

import com.zpaz.tfsotg.Utils.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zsolt on 25/02/18.
 */

public class ReleasePhase {
    private String id;
    private String rank;
    private String phaseType;
    private String status;
    private ReleaseTask[] releaseTasks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRank() {
        return rank;
    }

    private void setRank(String rank) {
        this.rank = rank;
    }

    public String getPhaseType() {
        return phaseType;
    }

    private void setPhaseType(String phaseType) {
        this.phaseType = phaseType;
    }

    public String getStatus() {
        return status;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    public ReleaseTask[] getReleaseTasks() {
        return releaseTasks;
    }

    private void setReleaseTasks(ReleaseTask[] releaseTasks) {
        this.releaseTasks = releaseTasks;
    }

    static ReleasePhase GetReleasePhaseFromJson(JSONObject releasePhaseJson) throws JSONException {
        JsonParser parser = new JsonParser();
        ReleasePhase phase = new ReleasePhase();
        phase.setId(parser.getString(releasePhaseJson,"id"));
        phase.setRank(parser.getString(releasePhaseJson,"rank"));
        phase.setPhaseType(parser.getString(releasePhaseJson,"phaseType"));
        phase.setStatus(parser.getString(releasePhaseJson,"status"));
        JSONArray releaseTasksJson = releasePhaseJson.getJSONArray("deploymentJobs").getJSONObject(0).getJSONArray("tasks");
        ReleaseTask[] tasks = new ReleaseTask[releaseTasksJson.length()];
        for(int i = 0; i < releaseTasksJson.length(); i ++){
            tasks[i] = ReleaseTask.GetReleaseTaskFromJson(releaseTasksJson.getJSONObject(i));
        }
        phase.setReleaseTasks(tasks);
        return phase;
    }
}
