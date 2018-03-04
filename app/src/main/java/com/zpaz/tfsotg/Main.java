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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zpaz.tfsotg.Build.ListedBuild;
import com.zpaz.tfsotg.Build.ViewBuildDetails;
import com.zpaz.tfsotg.Interfaces.ListedEntity;
import com.zpaz.tfsotg.Release.ListedRelease;
import com.zpaz.tfsotg.Release.ViewReleaseDetails;
import com.zpaz.tfsotg.Enums.QueryActions;
import com.zpaz.tfsotg.Utils.MainListAdapter;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.stream.Collectors;

import static com.zpaz.tfsotg.Build.ListedBuild.GetTfsBuilds;
import static com.zpaz.tfsotg.Release.ListedRelease.GetTfsReleases;
import static com.zpaz.tfsotg.Enums.QueryActions.*;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String credentials;
    String baseUrl;
    String username;
    ListView mainList;
    ListAdapter listAdapter;
    LinkedList queueList;
    Context context;
    SwipeRefreshLayout swipeRefresh;
    QueryActions lastQuery;
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

        queryBuilds();
        swipeRefresh = findViewById(R.id.swiperefresh);
        setUpSwipeRefresh();

    }

    private void setUpSwipeRefresh(){
        swipeRefresh.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if(lastQuery.equals(QueryActions.GetBuilds)){
                        queryBuilds();
                    }else if (lastQuery.equals(QueryActions.GetReleases)){
                        queryReleases();
                    }
                }
            }
        );
    }

    private void queryBuilds(){
        lastQuery = GetBuilds;
        new RequestFromTfs().execute(GetBuilds, credentials);
    }

    private void queryReleases(){
        lastQuery = GetReleases;
        new RequestFromTfs().execute(GetReleases, credentials);
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
            queryBuilds();
        } else if (id == R.id.menuReleases) {
            queryReleases();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String buildUrl(QueryActions action, String param) {
        switch (action) {
            case GetBuilds:
                return baseUrl + "_apis/build/builds?$top=" + itemsToShow + "&api-version=2.0";
            case GetReleases:
                return baseUrl + "_apis/Release/releases";
            case GetReleaseDetails:
                return baseUrl + "_apis/Release/releases" + param;
            case GetBuildDetails:
                return baseUrl + "_apis/build/builds" + param + "/timeline?api-version=2.0";
            default:
                return baseUrl + "_apis/Release/releases";
        }
    }

    private class RequestFromTfs extends AsyncTask<Object, String, String> {
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

            HttpURLConnection conn = null;
            try {
                String credsEncoded = params[1].toString();
                URL url = new URL(buildUrl(action, optionalUrlParam));
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Basic " + credsEncoded);
                conn.setDefaultUseCaches(true);

                response = conn.getResponseMessage();

                BufferedReader reader;
                if (response.equals("OK")) {
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                returnedObject = reader.lines().collect(Collectors.joining());

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return returnedObject;
        }

        @Override
        protected void onPostExecute(String response) {
            if(swipeRefresh.isRefreshing()){
                swipeRefresh.setRefreshing(false);
            }
            try {
                switch (action){
                    case GetBuilds:
                        queueList = GetTfsBuilds(response);
                        updateView(queueList);
                        break;
                    case GetBuildDetails:
                        Intent viewBuildIntent = new Intent(getApplicationContext(), ViewBuildDetails.class);
                        viewBuildIntent.putExtra("build", response);
                        startActivity(viewBuildIntent);
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
//                    listAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
                    listAdapter = new MainListAdapter(context, R.layout.main_list_layout, list);
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
                    addBuildDetailsListener();
                    break;
            }
        }

        private void addReleaseDetailsListener(){
            mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListedRelease selectedRelease = (ListedRelease) queueList.get(position);
                    new RequestFromTfs().execute(GetReleaseDetails, credentials, selectedRelease.getId());
                }
            });
        }

        private void addBuildDetailsListener(){
            mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListedBuild selectedBuild = (ListedBuild) queueList.get(position);
                    new RequestFromTfs().execute(GetBuildDetails, credentials, String.valueOf(selectedBuild.getId()));
                }
            });
        }
    }
}
