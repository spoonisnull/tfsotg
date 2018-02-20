package com.zpaz.tfsotg;

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
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static JSONObject jsonInView;
    TextView display;
    String creds;
    String baseUrl;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("TFS on the go");
        creds = this.getIntent().getExtras().get("Creds").toString();
        baseUrl = this.getIntent().getExtras().get("BaseUrl").toString();
        username = this.getIntent().getExtras().get("UserName").toString();
        display = findViewById(R.id.display);

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
            new requestFromTfs().execute(buildUrl("build"),creds);
        } else if (id == R.id.menuReleases) {
            new requestFromTfs().execute(buildUrl("release"),creds);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String buildUrl(String action) {
        switch (action) {
            case "build":
                return baseUrl + "_apis/build/builds?$top=10&api-version=2.0";
            case "release":
                return baseUrl + "_apis/Release/releases";
            default:
                return baseUrl + "_apis/build/builds?definitions=25&statusFilter=completed&$top=1&api-version=2.0";
        }
    }
    private class requestFromTfs extends AsyncTask<String, String, String> {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(String... urlString) {
            String response = "";
            String responseAsString = "";

            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(urlString[0]);
                String credsEncoded = urlString[1];
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Authorization", "Basic " + credsEncoded);
                httpURLConnection.setDefaultUseCaches(true);

                response = httpURLConnection.getResponseMessage();

                BufferedReader reader = null;
                if (response.equals("OK")) {
                    reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                } else {
                    reader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
                }

                responseAsString = reader.lines().collect(Collectors.joining());

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }
            onPostExecute(responseAsString);
            return responseAsString;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                jsonInView = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            updateView(display, result);
        }

        private void updateView(final TextView display, final String text){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    display.setText(text);
                }
            });
        }
    }
}
