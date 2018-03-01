package com.zpaz.tfsotg.Build;

import com.zpaz.tfsotg.Enums.BuildTaskStates;

/**
 * Created by zsolt on 01/03/18.
 */

public class BuildTask {
    private String guid;
    private String name;
    private String startTime;
    private String finishTime;
    private int percentComplete;
    private BuildTaskStates state;
    private String result;
    private String workerName;
    private String taskType;
    private String[] issues;

    public BuildTask(String guid){
        this.guid = guid;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public int getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(int percentComplete) {
        this.percentComplete = percentComplete;
    }

    public BuildTaskStates getState() {
        return state;
    }

    public void setState(BuildTaskStates state) {
        this.state = state;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String[] getIssues() {
        return issues;
    }

    public void setIssues(String[] issues) {
        this.issues = issues;
    }
}
