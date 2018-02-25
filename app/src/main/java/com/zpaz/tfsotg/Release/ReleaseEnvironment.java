package com.zpaz.tfsotg.Release;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zsolt on 25/02/18.
 */

public class ReleaseEnvironment {
    String id;
    String releaseId;
    String name;
    String status;
    ReleaseStep[] steps;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ReleaseStep[] getSteps() {
        return steps;
    }

    public void setSteps(ReleaseStep[] steps) {
        this.steps = steps;
    }

    public static ReleaseEnvironment GetReleaseEnvironmentFromJson(JSONObject environmentJson) throws JSONException {
        ReleaseEnvironment environment = new ReleaseEnvironment();
        environment.setId(environmentJson.getString("id"));
        environment.setReleaseId(environmentJson.getString("releaseId"));
        environment.setName(environmentJson.getString("name"));
        environment.setStatus(environmentJson.getString("status"));
        JSONArray releaseStepsJson = environmentJson.getJSONArray("deploySteps");
        ReleaseStep[] steps = new ReleaseStep[releaseStepsJson.length()];
        for(int i = 0; i < releaseStepsJson.length(); i ++){
            steps[i] = ReleaseStep.GetReleaseStepFromJson(releaseStepsJson.getJSONObject(i));
        }
        environment.setSteps(steps);
        return environment;
    }

}
