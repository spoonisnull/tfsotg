package com.zpaz.tfsotg.Build;

import com.zpaz.tfsotg.Enums.EntityStatus;
import com.zpaz.tfsotg.Interfaces.DetailedEntity;
import com.zpaz.tfsotg.Utils.DateTimeParser;
import com.zpaz.tfsotg.Utils.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsolt on 25/02/18.
 */

public class DetailedBuild implements DetailedEntity{
    private String id;
    private List<BuildTask> buildTasks;
    private DateTimeParser queuedDateTime;
    private DateTimeParser startedDateTime;
    private DateTimeParser finishedDateTime;
    private String buildNumber;
    private EntityStatus result;
    private String buildDefinition;
    private String finishTime;
    private String createdBy;

    public DetailedBuild(String id, JSONObject buildJson) throws JSONException {
        this.id = id;
        this.buildTasks = GetBuildTasksFromJson(buildJson);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<BuildTask> getBuildTasks() {
        return buildTasks;
    }

    public void setBuildTasks(List<BuildTask> buildTasks) {
        this.buildTasks = buildTasks;
    }

    public DateTimeParser getQueuedDateTime() {
        return queuedDateTime;
    }

    public void setQueuedDateTime(DateTimeParser queuedDateTime) {
        this.queuedDateTime = queuedDateTime;
    }

    public DateTimeParser getStartedDateTime() {
        return startedDateTime;
    }

    public void setStartedDateTime(DateTimeParser startedDateTime) {
        this.startedDateTime = startedDateTime;
    }

    public DateTimeParser getFinishedDateTime() {
        return finishedDateTime;
    }

    public void setFinishedDateTime(DateTimeParser finishedDateTime) {
        this.finishedDateTime = finishedDateTime;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public EntityStatus getResult() {
        return result;
    }

    public void setResult(EntityStatus result) {
        this.result = result;
    }

    public String getBuildDefinition() {
        return buildDefinition;
    }

    public void setBuildDefinition(String buildDefinition) {
        this.buildDefinition = buildDefinition;
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

    private List<BuildTask> GetBuildTasksFromJson(JSONObject buildJson) throws JSONException {
        JsonParser parser = new JsonParser();
        JSONArray buildTasksArray = buildJson.getJSONArray("records");
        buildTasks = new ArrayList<>();
        for(int i = 0; i < buildTasksArray.length(); i ++){
            JSONObject taskJson = buildTasksArray.getJSONObject(i);
            BuildTask buildTask = new BuildTask(taskJson.getString("id"));

            buildTask.setOrder(parser.getInt(taskJson, "order"));
            buildTask.setType(parser.getString(taskJson, "type"));
            buildTask.setFinishTime(parser.getString(taskJson, "finishTime"));
            buildTask.setName(parser.getString(taskJson, "name"));
            buildTask.setPercentComplete("" + parser.getInt(taskJson, "percentComplete"));
            buildTask.setStartTime(parser.getString(taskJson, "startTime"));
            buildTask.setFinishTime(parser.getString(taskJson, "finishTime"));
            buildTask.setResult(parser.getString(taskJson, "result"));
            buildTask.setTaskType(parser.getString(taskJson.optJSONObject("task"), "name"));
            buildTask.setWorkerName(parser.getString(taskJson, "workerName"));
            buildTask.setStatus(EntityStatus.valueOf(taskJson.getString("result")));

            if(taskJson.has("issues")){
                JSONArray issuesJsonArray = taskJson.getJSONArray("issues");
                String[] issues = new String[issuesJsonArray.length()];
                for(int j = 0; j < issues.length; j ++){
                    issues[j] = issuesJsonArray.getJSONObject(j).getString("type") + ": " +
                            issuesJsonArray.getJSONObject(j).getString("message");
                }
                buildTask.setIssues(issues);
            }
            buildTasks.add(buildTask);
        }
        return buildTasks;
    }

}
