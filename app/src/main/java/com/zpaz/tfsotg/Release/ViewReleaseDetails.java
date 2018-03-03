package com.zpaz.tfsotg.Release;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zpaz.tfsotg.Enums.EnvironmentStatus;
import com.zpaz.tfsotg.R;
import com.zpaz.tfsotg.Utils.DateTimeParser;

import org.json.JSONException;

import static com.zpaz.tfsotg.Release.DetailedRelease.GetDetailedReleaseFromJson;

public class ViewReleaseDetails extends AppCompatActivity {
    Context context;
    DetailedRelease release = null;
    TextView releaseDefinitionDisplay;
    TextView releaseCreatedDisplay;
    LinearLayout layout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        releaseDefinitionDisplay = findViewById(R.id.relDefDisplay);
        releaseCreatedDisplay = findViewById(R.id.relCreatedDisplay);
        setSupportActionBar(toolbar);
        context = getApplicationContext();

        String releaseAsString = String.valueOf(this.getIntent().getExtras().get("release"));
        try {
            release = GetDetailedReleaseFromJson(releaseAsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setTopTextViews();
        layout = findViewById(R.id.environmentsLayout);
        setUpEnvironmentViews();
    }

    private void setTopTextViews() {
        setTitle(release.getName());
        releaseDefinitionDisplay.setText(release.getReleaseDefinition());
        DateTimeParser dateTime = new DateTimeParser(release.getCreatedOn());
        releaseCreatedDisplay.setText(String.format(dateTime.getTimeWithSeconds() + " - " + dateTime.getDateWithYear() + "\n" + release.getCreatedBy()));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setUpEnvironmentViews(){
        ReleaseEnvironment[] environments = release.getEnvironments();
        for (ReleaseEnvironment environment : environments) {
            TextView envTitleView = new TextView(context);
            envTitleView.setText(environment.getName());
            envTitleView.setTypeface(null, Typeface.BOLD);
            setTitleBarBgColourBasedOnStatus(envTitleView, environment.getStatus());
            envTitleView.setTextColor(Color.WHITE);
            layout.addView(envTitleView);


            TextView blob = new TextView(context);
            blob.setText(environment.toString() + "\n\n");
            layout.addView(blob);
        }
    }

    private void setTitleBarBgColourBasedOnStatus(TextView textView, EnvironmentStatus status){
        switch (status){
            case failed:
            case rejected:
            case canceled:
                textView.setBackgroundColor(getResources().getColor(R.color.tfsRed));
                break;
            case inProgress:
            case notStarted:
                textView.setBackgroundColor(getResources().getColor(R.color.tfsGray));
                break;
            case partiallySucceeded:
                textView.setBackgroundColor(getResources().getColor(R.color.tfsOrange));
                break;
            case succeeded:
                textView.setBackgroundColor(getResources().getColor(R.color.tfsGreen));
                break;
        }
    }
}
