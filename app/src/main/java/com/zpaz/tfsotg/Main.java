package com.zpaz.tfsotg;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static JSONObject jsonInView;
    String creds;
    String baseUrl;
    String username;
    ListView mainList;
    ListAdapter listAdapter;
    Context context;

    enum QueryActions {GetBuilds,GetReleases}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("TFS on the go");
        creds = this.getIntent().getExtras().get("Creds").toString();
        baseUrl = this.getIntent().getExtras().get("BaseUrl").toString();
        username = this.getIntent().getExtras().get("UserName").toString();
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView userLabel = navigationView.getHeaderView(0).findViewById(R.id.mainUserLabel);
        userLabel.setText(username);

        mainList = findViewById(R.id.mainList);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menuBuilds) {
            new requestFromTfs().execute(QueryActions.GetBuilds, creds);
        } else if (id == R.id.menuReleases) {
            new requestFromTfs().execute(QueryActions.GetReleases, creds);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String buildUrl(QueryActions action) {
        switch (action) {
            case GetBuilds:
                return baseUrl + "_apis/build/builds?$top=100&api-version=2.0";
            case GetReleases:
                return baseUrl + "_apis/Release/releases";
            default:
                return baseUrl + "_apis/build/builds?definitions=25&statusFilter=completed&$top=1&api-version=2.0";
        }
    }
    private class requestFromTfs extends AsyncTask<Object, String, String> {

        QueryActions action;

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(Object... params) {

            action = (QueryActions) params[0];

            String response;
            String responseAsString = "";

            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(buildUrl(action));
                String credsEncoded = params[1].toString();
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

                responseAsString = reader.lines().collect(Collectors.joining());

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            if(!responseAsString.equals("")){
                onPostExecute(responseAsString);
            }
            return responseAsString;
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                LinkedList queuedEntities = ParseResponse(response);
                updateView(queuedEntities);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private LinkedList ParseResponse(String response) throws JSONException {
            switch (action){
                case GetBuilds:
                    return GetTfsBuilds(response);
                case GetReleases:
                    return GetTfsReleases(response);
                default:
                    return new LinkedList<>();
            }
        }

        private LinkedList GetTfsBuilds(String response) throws JSONException {
            JSONArray builds = new JSONObject(response).getJSONArray("value");

            LinkedList<TfsBuild> buildList = new LinkedList<>();
            for(int i = 0; i < builds.length(); i ++){

                JSONObject currentJson = builds.getJSONObject(i);
                TfsBuild current = new TfsBuild(
                        currentJson.getString("id"),
                        currentJson.getString("buildNumber"),
                        currentJson.getJSONObject("definition").getString("name"));

                current.setUrl(currentJson.getString("url"));
                current.setSourceVersion(currentJson.getString("sourceVersion"));
                current.setSourceBranch(currentJson.getString("sourceBranch"));
                current.setFinishTime(currentJson.getString("finishTime"));
                current.setCreatedBy(currentJson.getJSONObject("requestedFor").getString("displayName"));
                current.setCreatedOn(currentJson.getString("queueTime"));
                current.setStartTime(currentJson.getString("startTime"));
                current.setLogsUrl(currentJson.getJSONObject("logs").getString("url"));
                current.setResult(currentJson.getString("result"));
                current.setStatus(currentJson.getString("status"));
                current.setModifiedBy(currentJson.getJSONObject("lastChangedBy").getString("displayName"));
                current.setModifiedOn(currentJson.getString("lastChangedDate"));

                buildList.add(current);
            }
            return buildList;
        }

        private LinkedList GetTfsReleases(String response){
            return new LinkedList();
        }

        private void updateView(final LinkedList list) throws JSONException {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
                    mainList.setAdapter(listAdapter);
                }
            });
        }
    }
}
