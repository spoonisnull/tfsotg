package com.zpaz.tfsotg.Release;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.zpaz.tfsotg.R;
import com.zpaz.tfsotg.Utils.DateTimeParser;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.zpaz.tfsotg.Release.DetailedRelease.GetDetailedReleaseFromJson;

public class ViewReleaseDetails extends AppCompatActivity {

    private DetailedRelease release = null;
    private TextView releaseDefinitionDisplay;
    private TextView releaseCreatedDisplay;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        releaseDefinitionDisplay = findViewById(R.id.relDefDisplay);
        releaseCreatedDisplay = findViewById(R.id.relCreatedDisplay);
        setSupportActionBar(toolbar);

        String detailedRelease = String.valueOf(this.getIntent().getExtras().get("detailedRelease"));
        try {
            release = GetDetailedReleaseFromJson(detailedRelease);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setTopTextViews();
        List<ReleaseEnvironment> environmentList = new ArrayList<>();
        environmentList.addAll(Arrays.asList(release.getEnvironments()));
        RecyclerView recyclerView = findViewById(R.id.envRecView);
        EnvironmentAdapter adapter = new EnvironmentAdapter(this, environmentList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    private void setTopTextViews() {
        setTitle(release.getName());
        releaseDefinitionDisplay.setText(release.getReleaseDefinition());
        DateTimeParser dateTime = new DateTimeParser(release.getCreatedOn());
        releaseCreatedDisplay.setText(dateTime.getTimeWithSeconds() + " - " + dateTime.getDateWithYear() + "\n" + release.getCreatedBy());
    }

}
