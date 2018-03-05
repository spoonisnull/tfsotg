package com.zpaz.tfsotg.Build;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.zpaz.tfsotg.Enums.EntityStatus;
import com.zpaz.tfsotg.R;
import com.zpaz.tfsotg.Utils.DateTimeParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ViewBuildDetails extends AppCompatActivity {

    private List<BuildTask> buildTaskList;
    private DetailedBuild detailedBuild;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        JSONObject detailedBuildJson;

        try {
            detailedBuildJson = new JSONObject(String.valueOf(this.getIntent().getExtras().getString("detailedBuild")));
            detailedBuild = new DetailedBuild( detailedBuildJson.getString("id"), detailedBuildJson);
            detailedBuild.setBuildDefinition(String.valueOf(getIntent().getExtras().getString("buildDef")));
            detailedBuild.setBuildNumber(String.valueOf(getIntent().getExtras().getString("buildNo")));
            detailedBuild.setResult(EntityStatus.valueOf(getIntent().getExtras().getString("buildResult")));
            detailedBuild.setCreatedBy(String.valueOf(getIntent().getExtras().getString("buildCreatedBy")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        buildTaskList = detailedBuild.getBuildTasks();
        buildTaskList.sort((o1, o2) -> o1.getOrder() < o2.getOrder() ? -1 : o1.getOrder() == o2.getOrder() ? 0 : 1);
        buildTaskList.removeIf(t -> !t.getType().equals("Task"));
        RecyclerView recyclerView = findViewById(R.id.buildTaskRecView);
        BuildTaskAdapter adapter = new BuildTaskAdapter(this, buildTaskList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        try {
            setTopTextViews();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTopTextViews() throws JSONException {
        setTitle(detailedBuild.getBuildNumber());

        TextView buildDef = findViewById(R.id.buildDefDisplay);
        buildDef.setText(detailedBuild.getBuildDefinition());

        TextView buildCreatedByAt = findViewById(R.id.buildCreatedByAt);
        buildCreatedByAt.setText(detailedBuild.getCreatedBy());

        DateTimeParser dateTime = new DateTimeParser(buildTaskList.get(buildTaskList.size()-1).getStartTime());
        buildCreatedByAt.setText(dateTime.getTimeWithSeconds());
    }
}
