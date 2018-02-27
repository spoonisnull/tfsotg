package com.zpaz.tfsotg.Release;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zpaz.tfsotg.R;
import com.zpaz.tfsotg.Utils.DateTimeParser;
import com.zpaz.tfsotg.Utils.EnvironmentStatuses;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.ArrayList;

import static com.zpaz.tfsotg.Release.DetailedRelease.GetDetailedReleaseFromJson;

public class ViewReleaseDetails extends AppCompatActivity {
    Context context;
    DetailedRelease release = null;
    TextView releaseDefinitionDisplay;
    TextView releaseCreatedDisplay;
    LinearLayout layout;
    int gap = 15;

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
        setTextsInViews();
        layout = findViewById(R.id.environmentsLayout);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        createCardsForEnvironments();
    }

    private void createCardsForEnvironments() {
        for(int i = 0; i < release.getEnvironments().length; i ++){
            CardView card = new CardView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            card.setLayoutParams(params);
            card.setRadius(9);
            card.setContentPadding(gap, gap, gap, gap);
            card.setPadding(0,0,gap, gap);
            setCardBackgroundBasedOnStatus(card, release.getEnvironments()[i].getStatus());
            card.setMaxCardElevation(gap);
            card.setElevation(9);

            TextView name = new TextView(context);
            name.setLayoutParams(params);
            name.setText(release.getEnvironments()[i].getName()+"\n\n\n\n");
            card.addView(name);
            layout.addView(card);
        }
    }

    private void setTextsInViews() {
        setTitle(release.getName());
        releaseDefinitionDisplay.setText(release.getReleaseDefinition());
        DateTimeParser parser = new DateTimeParser();
        String createdOn = parser.parseDateFromDateTimeString(release.getCreatedOn());
        String createdAt = parser.parseTimeFromDateTimeString(release.getCreatedOn());
        releaseCreatedDisplay.setText(String.format("%s @ %s\nby %s", createdOn, createdAt, release.getCreatedBy()));
    }

    private void setCardBackgroundBasedOnStatus(CardView card, String status){
        switch (EnvironmentStatuses.valueOf(status)){
            case failed:
            case rejected:
            case cancelled:
                card.setBackgroundColor(getResources().getColor(R.color.tfsRed));
                break;
            case notStarted:
                card.setBackgroundColor(getResources().getColor(R.color.tfsGray));
                break;
            case partiallySucceeded:
                card.setBackgroundColor(getResources().getColor(R.color.tfsOrange));
                break;
            case succeeded:
                card.setBackgroundColor(getResources().getColor(R.color.tfsGreen));
                break;
        }
    }
}
