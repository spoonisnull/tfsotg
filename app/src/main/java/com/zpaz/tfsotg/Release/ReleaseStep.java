package com.zpaz.tfsotg.Release;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zsolt on 25/02/18.
 */

public class ReleaseStep {
    String id;
    String status;
    String attempt;
    String requestedBy;
    String queuedOn;
    boolean hasStarted;
    ReleasePhase[] phases;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAttempt() {
        return attempt;
    }

    public void setAttempt(String attempt) {
        this.attempt = attempt;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getQueuedOn() {
        return queuedOn;
    }

    public void setQueuedOn(String queuedOn) {
        this.queuedOn = queuedOn;
    }

    public boolean isHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public ReleasePhase[] getPhases() {
        return phases;
    }

    public void setPhases(ReleasePhase[] phases) {
        this.phases = phases;
    }

    public static ReleaseStep GetReleaseStepFromJson(JSONObject releaseStepJson) throws JSONException {
        ReleaseStep step = new ReleaseStep();
        step.setId(releaseStepJson.getString("id"));
        step.setStatus(releaseStepJson.getString("status"));
        step.setAttempt(releaseStepJson.getString("attempt"));
        step.setRequestedBy(releaseStepJson.getJSONObject("requestedBy").getString("displayName"));
        step.setQueuedOn(releaseStepJson.getString("queuedOn"));
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
