package com.zpaz.tfsotg.Release;

import com.zpaz.tfsotg.Utils.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zsolt on 25/02/18.
 */

public class ReleaseStep {
    private String id;
    private String status;
    private String attempt;
    private String requestedBy;
    private String queuedOn;
    private boolean hasStarted;
    private ReleasePhase[] phases;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    public String getAttempt() {
        return attempt;
    }

    private void setAttempt(String attempt) {
        this.attempt = attempt;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    private void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getQueuedOn() {
        return queuedOn;
    }

    private void setQueuedOn(String queuedOn) {
        this.queuedOn = queuedOn;
    }

    public boolean isHasStarted() {
        return hasStarted;
    }

    private void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public ReleasePhase[] getPhases() {
        return phases;
    }

    private void setPhases(ReleasePhase[] phases) {
        this.phases = phases;
    }

    static ReleaseStep GetReleaseStepFromJson(JSONObject releaseStepJson) throws JSONException {
        JsonParser parser = new JsonParser();
        ReleaseStep step = new ReleaseStep();
        step.setId(parser.getString(releaseStepJson,"id"));
        step.setStatus(parser.getString(releaseStepJson,"status"));
        step.setAttempt(parser.getString(releaseStepJson,"attempt"));
        step.setRequestedBy(parser.getString(releaseStepJson.getJSONObject("requestedBy"),"displayName"));
        step.setQueuedOn(parser.getString(releaseStepJson,"queuedOn"));
        step.setHasStarted(releaseStepJson.getBoolean("hasStarted"));
        JSONArray phasesJson = releaseStepJson.getJSONArray("releaseDeployPhases");
        ReleasePhase[] phases = new ReleasePhase[phasesJson.length()];
        for(int i = 0; i < phasesJson.length(); i ++){
            phases[i] = ReleasePhase.GetReleasePhaseFromJson(phasesJson.getJSONObject(i));
        }
        step.setPhases(phases);
        return step;
    }
}
