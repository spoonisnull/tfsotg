package com.zpaz.tfsotg;

/**
 * Created by zsolt on 20/02/18.
 */

public class TfsBuild implements TfsQueuedEntity {

    private String id;
    private String buildNumber;
    private String buildDefinition;
    private String url;
    private String sourceVersion;
    private String sourceBranch;
    private String finishTime;
    private String createdBy;
    private String createdOn;
    private String startTime;
    private String logsUrl;
    private String result;
    private String status;
    private String modifiedBy;
    private String modifiedOn;

    TfsBuild(String id, String buildNumber, String buildDefinition) {
        this.id = id;
        this.buildNumber = buildNumber;
        this.buildDefinition = buildDefinition;
    }

    @Override
    public String toString() {
        return buildDefinition + " [" + result + "]" + "\n"
        + buildNumber + " (" + createdBy + ")" + "\n"
        + "queued: " + createdOn + "\n"
        + "started: " + startTime + "\n"
        + "finished: " + finishTime + "\n";

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getBuildDefinition() {
        return buildDefinition;
    }

    public void setBuildDefinition(String buildDefinition) {
        this.buildDefinition = buildDefinition;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSourceVersion() {
        return sourceVersion;
    }

    public void setSourceVersion(String sourceVersion) {
        this.sourceVersion = sourceVersion;
    }

    public String getSourceBranch() {
        return sourceBranch;
    }

    public void setSourceBranch(String sourceBranch) {
        this.sourceBranch = sourceBranch;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getLogsUrl() {
        return logsUrl;
    }

    public void setLogsUrl(String logsUrl) {
        this.logsUrl = logsUrl;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
