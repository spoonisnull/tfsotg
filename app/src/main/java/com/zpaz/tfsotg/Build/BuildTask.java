package com.zpaz.tfsotg.Build;

import android.support.annotation.NonNull;

import com.zpaz.tfsotg.Enums.EntityStatus;

/**
 * Created by zsolt on 01/03/18.
 */

public class BuildTask implements Comparable{
    private int order;
    private String guid;
    private String type;
    private String name;
    private String startTime;
    private String finishTime;
    private String percentComplete;
    private EntityStatus status;
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

    public String getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(String percentComplete) {
        this.percentComplete = percentComplete;
    }

    public EntityStatus getState() {
        return status;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        BuildTask that = (BuildTask)o;
        return this.getOrder() > that.getOrder() ? 1 : 0;
    }
}
