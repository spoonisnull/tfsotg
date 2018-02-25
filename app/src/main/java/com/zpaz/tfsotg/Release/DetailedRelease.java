package com.zpaz.tfsotg.Release;

import com.zpaz.tfsotg.Interfaces.DetailedEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zsolt on 24/02/18.
 */

public class DetailedRelease implements DetailedEntity{
    String id;
    String name;
    String createdOn;
    String createdBy;
    ReleaseEnvironment[] environments;
    String releaseDefinition;

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

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ReleaseEnvironment[] getEnvironments() {
        return environments;
    }

    public void setEnvironments(ReleaseEnvironment[] environments) {
        this.environments = environments;
    }

    public String getReleaseDefinition() {
        return releaseDefinition;
    }

    public void setReleaseDefinition(String releaseDefinition) {
        this.releaseDefinition = releaseDefinition;
    }

    public static DetailedRelease GetDetailedReleaseFromJson(String response) throws JSONException {
        JSONObject releaseJson = new JSONObject(response);
        DetailedRelease detailedRelease = new DetailedRelease();

        detailedRelease.setId(releaseJson.getString("id"));
        detailedRelease.setName(releaseJson.getString("name"));
        detailedRelease.setCreatedOn(releaseJson.getString("createdOn"));
        detailedRelease.setCreatedBy(releaseJson.getJSONObject("createdBy").getString("displayName"));
        detailedRelease.setReleaseDefinition(releaseJson.getJSONObject("releaseDefinition").getString("name"));
        JSONArray environmentsJson = releaseJson.getJSONArray("environments");
        ReleaseEnvironment[] environments = new ReleaseEnvironment[environmentsJson.length()];
        for(int i = 0; i < environmentsJson.length(); i ++){
            environments[i] = ReleaseEnvironment.GetReleaseEnvironmentFromJson(environmentsJson.getJSONObject(i));
        }
        detailedRelease.setEnvironments(environments);
        return detailedRelease;
    }
}
