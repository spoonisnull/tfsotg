package com.zpaz.tfsotg.Build;

import com.zpaz.tfsotg.Enums.BuildTaskStates;
import com.zpaz.tfsotg.Interfaces.DetailedEntity;
import com.zpaz.tfsotg.Utils.DateTimeParser;
import com.zpaz.tfsotg.Utils.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zsolt on 25/02/18.
 */

public class DetailedBuild implements DetailedEntity{
    private String id;
    private BuildTask[] tasks;

    public DetailedBuild(String id, String build) throws JSONException {
        this.id = id;
        this.tasks = GetBuildTasksFromJson(build);
    }

    private BuildTask[] GetBuildTasksFromJson(String build) throws JSONException {
        JsonParser parser = new JsonParser();
        JSONObject buildJson = new JSONObject(build);
        JSONArray buildTasksArray = buildJson.getJSONArray("records");
        tasks = new BuildTask[buildTasksArray.length()];
        for(int i = 0; i < buildTasksArray.length(); i ++){
            JSONObject task = buildTasksArray.getJSONObject(i);
            tasks[i].setFinishTime(parser.getString(task, "finishTime"));
            tasks[i].setName(parser.getString(task, "name"));
            tasks[i].setPercentComplete(parser.getInt(task, "percentComplete"));
            tasks[i].setStartTime(parser.getString(task, "startTime"));
            tasks[i].setFinishTime(parser.getString(task, "finishTime"));
            tasks[i].setResult(parser.getString(task, "result"));
            tasks[i].setTaskType(parser.getString(task.getJSONObject("task"), "name"));
            tasks[i].setWorkerName(parser.getString(task, "workerName"));
            tasks[i].setState(BuildTaskStates.valueOf(task.getString("state")));
            if(task.has("issues")){
                JSONArray issuesJsonArray = task.getJSONArray("issues");
                String[] issues = new String[issuesJsonArray.length()];
                for(int j = 0; i < issues.length; j ++){
                    issues[j] = issuesJsonArray.getJSONObject(j).getString("type") + ": " +
                            issuesJsonArray.getJSONObject(j).getString("message");
                }
                tasks[i].setIssues(issues);
            }
        }
        return tasks;
    }

}
