package com.zpaz.tfsotg.Release;

import com.zpaz.tfsotg.Enums.EntityStatus;
import com.zpaz.tfsotg.Interfaces.ListedEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by zsolt on 21/02/18.
 */

public class ListedRelease implements ListedEntity {

    private String id;
    private String name;
    private EntityStatus status;
    private String createdOn;
    private String createdBy;
    private String releaseDefinition;

    private ListedRelease(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return releaseDefinition + "\n"
        + name + " (" + createdBy + ")" + "\n"
        + "Queued:   " + createdOn;
    }

    public static LinkedList GetTfsReleases(String response) throws JSONException {
        JSONArray releases = new JSONObject(response).getJSONArray("value");
        LinkedList<ListedRelease> releaseList = new LinkedList<>();
        for(int i = 0; i < releases.length(); i ++){
            releaseList.add(parseReleaseFromResponse(releases.getJSONObject(i)));
        }
        return releaseList;
    }

    private static ListedRelease parseReleaseFromResponse(JSONObject tfsReleaseJson) throws JSONException{
        ListedRelease release = new ListedRelease(
                tfsReleaseJson.getString("id"),
                tfsReleaseJson.getString("name"));
        release.setCreatedBy(tfsReleaseJson.getJSONObject("createdBy").getString("displayName"));
        release.setCreatedOn(tfsReleaseJson.getString("createdOn"));
        release.setReleaseDefinition(tfsReleaseJson.getJSONObject("releaseDefinition").getString("name"));
        release.setStatus(EntityStatus.valueOf(tfsReleaseJson.getString("status")));
        return release;
    }
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

    public EntityStatus getStatus() {
        return status;
    }

    @Override
    public String getDefinition() {
        return getReleaseDefinition();
    }

    private void setStatus(EntityStatus status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    private void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public String getTime() {
        return getCreatedOn();
    }

    private void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getReleaseDefinition() {
        return releaseDefinition;
    }

    private void setReleaseDefinition(String releaseDefinition) {
        this.releaseDefinition = releaseDefinition;
    }

}
