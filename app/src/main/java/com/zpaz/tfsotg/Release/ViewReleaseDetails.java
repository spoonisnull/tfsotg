package com.zpaz.tfsotg.Release;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zpaz.tfsotg.R;

import org.json.JSONException;
import org.json.JSONStringer;

import static com.zpaz.tfsotg.Release.DetailedRelease.GetDetailedReleaseFromJson;

public class ViewReleaseDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String releaseAsString = String.valueOf(this.getIntent().getExtras().get("release"));
        DetailedRelease release = null;
        try {
            release = GetDetailedReleaseFromJson(releaseAsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setTitle(release.getName());
        TextView view = findViewById(R.id.releaseTextView);
        view.setText(releaseAsString);

    }
}
