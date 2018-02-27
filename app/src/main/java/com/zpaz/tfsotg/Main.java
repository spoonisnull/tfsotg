package com.zpaz.tfsotg;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zpaz.tfsotg.Interfaces.ListedEntity;
import com.zpaz.tfsotg.Release.DetailedRelease;
import com.zpaz.tfsotg.Release.ListedRelease;
import com.zpaz.tfsotg.Release.ViewReleaseDetails;
import com.zpaz.tfsotg.Utils.QueryActions;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.stream.Collectors;

import static com.zpaz.tfsotg.Build.ListedBuild.GetTfsBuilds;
import static com.zpaz.tfsotg.Release.DetailedRelease.GetDetailedReleaseFromJson;
import static com.zpaz.tfsotg.Release.ListedRelease.GetTfsReleases;
import static com.zpaz.tfsotg.Utils.QueryActions.*;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String credentials;
    String baseUrl;
    String username;
    ListView mainList;
    ListAdapter listAdapter;
    LinkedList queueList;
    Context context;
    int itemsToShow = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("TFS on the go");
        credentials = this.getIntent().getExtras().get("Creds").toString();
        baseUrl = this.getIntent().getExtras().get("BaseUrl").toString();
        username = this.getIntent().getExtras().get("UserName").toString();
        context = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView userLabel = navigationView.getHeaderView(0).findViewById(R.id.mainUserLabel);
        userLabel.setText(username);

        queueList = new LinkedList<ListedEntity>();
        mainList = findViewById(R.id.mainList);

        new requestFromTfs().execute(GetBuilds, credentials);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menuBuilds) {
            new requestFromTfs().execute(GetBuilds, credentials);
        } else if (id == R.id.menuReleases) {
            new requestFromTfs().execute(GetReleases, credentials);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String buildUrl(QueryActions action, String param) {
        switch (action) {
            case GetBuilds:
                return baseUrl + "_apis/build/builds?$top=" + itemsToShowStringified() + "&api-version=2.0";
            case GetReleases:
                return baseUrl + "_apis/Release/releases";
            case GetReleaseDetails:
                return baseUrl + "_apis/Release/releases" + param;
            case GetBuildDetails:
                return baseUrl + "_apis/build/builds" + param;
            default:
                return baseUrl + "_apis/Release/releases";
        }
    }

    private String itemsToShowStringified(){
        return "" + itemsToShow;
    }
    private class requestFromTfs extends AsyncTask<Object, String, String> {
        QueryActions action;

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(Object... params) {

            action = (QueryActions) params[0];

            String response;
            String returnedObject = "";
            String optionalUrlParam = "";
            if(params.length > 2){
                optionalUrlParam = "/" + params[2].toString();
            }

            HttpURLConnection httpURLConnection = null;
            try {
                String credsEncoded = params[1].toString();
                URL url = new URL(buildUrl(action, optionalUrlParam));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Authorization", "Basic " + credsEncoded);
                httpURLConnection.setDefaultUseCaches(true);

                response = httpURLConnection.getResponseMessage();

                BufferedReader reader;
                if (response.equals("OK")) {
                    reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                } else {
                    reader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
                }

                returnedObject = reader.lines().collect(Collectors.joining());

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return returnedObject;
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                switch (action){
                    case GetBuilds:
                        queueList = GetTfsBuilds(response);
                        updateView(queueList);
                        break;
                    case GetReleases:
                        queueList = GetTfsReleases(response);
                        updateView(queueList);
                        break;
                    case GetReleaseDetails:
                        Intent viewReleaseIntent = new Intent(getApplicationContext(), ViewReleaseDetails.class);
                        viewReleaseIntent.putExtra("release", response);
                        startActivity(viewReleaseIntent);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void updateView(final LinkedList list) throws JSONException {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
                    addRelevantListenerToListView();
                    changeTitle();
                    mainList.setAdapter(listAdapter);
                }
            });
        }

        private void changeTitle(){
            switch (action){
                case GetBuilds:
                    setTitle("Builds");
                    break;
                case GetReleases:
                    setTitle("Releases");
                    break;
            }
        }

        private void addRelevantListenerToListView(){
            switch (action){
                case GetReleases:
                    addReleaseDetailsListener();
                    break;
                case GetBuilds:
                    mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            return;
                        }
                    });
                    break;
            }
        }

        private void addReleaseDetailsListener(){
            mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListedRelease selectedRelease = (ListedRelease) queueList.get(position);
                    new requestFromTfs().execute(GetReleaseDetails, credentials, selectedRelease.getId());
                }
            });
        }
    }
}
