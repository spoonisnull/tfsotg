package com.zpaz.tfsotg.Release;

import com.zpaz.tfsotg.Enums.EnvironmentStatus;
import com.zpaz.tfsotg.Utils.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zsolt on 25/02/18.
 */

public class ReleaseEnvironment {
    private String id;
    private String releaseId;
    private String name;
    private EnvironmentStatus status;
    private ReleaseStep[] steps;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReleaseId() {
        return releaseId;
    }

    private void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    EnvironmentStatus getStatus() {
        return status;
    }

    private void setStatus(EnvironmentStatus status) {
        this.status = status;
    }

    public ReleaseStep[] getSteps() {
        return steps;
    }

    private void setSteps(ReleaseStep[] steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(getId()).append(" [").append(getReleaseId()).append("] \n");
        for(ReleaseStep step : getSteps()){
            sb.append(step.toString());
        }
        return sb.toString();
    }

    static ReleaseEnvironment GetReleaseEnvironmentFromJson(JSONObject environmentJson) throws JSONException {
        JsonParser parser = new JsonParser();
        ReleaseEnvironment environment = new ReleaseEnvironment();
        environment.setId(parser.getString(environmentJson,"id"));
        environment.setReleaseId(parser.getString(environmentJson,"releaseId"));
        environment.setName(parser.getString(environmentJson,"name"));
        environment.setStatus(EnvironmentStatus.valueOf(environmentJson.getString("status")));
        JSONArray releaseStepsJson = environmentJson.getJSONArray("deploySteps");
        ReleaseStep[] steps = new ReleaseStep[releaseStepsJson.length()];
        for(int i = 0; i < releaseStepsJson.length(); i ++){
            steps[i] = ReleaseStep.GetReleaseStepFromJson(releaseStepsJson.getJSONObject(i));
        }
        environment.setSteps(steps);
        return environment;
    }

}
