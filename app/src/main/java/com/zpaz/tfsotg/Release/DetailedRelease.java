package com.zpaz.tfsotg.Release;

import com.zpaz.tfsotg.Interfaces.DetailedEntity;
import com.zpaz.tfsotg.Utils.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zsolt on 24/02/18.
 */

public class DetailedRelease implements DetailedEntity{
    private String id;
    private String name;
    private String createdOn;
    private String createdBy;
    private ReleaseEnvironment[] environments;
    private String releaseDefinition;

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

    String getCreatedOn() {
        return createdOn;
    }

    private void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    String getCreatedBy() {
        return createdBy;
    }

    private void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    ReleaseEnvironment[] getEnvironments() {
        return environments;
    }

    private void setEnvironments(ReleaseEnvironment[] environments) {
        this.environments = environments;
    }

    String getReleaseDefinition() {
        return releaseDefinition;
    }

    private void setReleaseDefinition(String releaseDefinition) {
        this.releaseDefinition = releaseDefinition;
    }

    static DetailedRelease GetDetailedReleaseFromJson(String response) throws JSONException {
        JsonParser parser = new JsonParser();
        JSONObject releaseJson = new JSONObject(response);
        DetailedRelease detailedRelease = new DetailedRelease();

        detailedRelease.setId(parser.getString(releaseJson, "id"));
        detailedRelease.setName(parser.getString(releaseJson,"name"));
        detailedRelease.setCreatedOn(parser.getString(releaseJson,"createdOn"));
        detailedRelease.setCreatedBy(parser.getString(releaseJson.getJSONObject("createdBy"),"displayName"));
        detailedRelease.setReleaseDefinition(parser.getString(releaseJson.getJSONObject("releaseDefinition"),"name"));
        JSONArray environmentsJson = releaseJson.getJSONArray("environments");
        ReleaseEnvironment[] environments = new ReleaseEnvironment[environmentsJson.length()];
        for(int i = 0; i < environmentsJson.length(); i ++){
            environments[i] = ReleaseEnvironment.GetReleaseEnvironmentFromJson(environmentsJson.getJSONObject(i));
        }
        detailedRelease.setEnvironments(environments);
        return detailedRelease;
    }
}
