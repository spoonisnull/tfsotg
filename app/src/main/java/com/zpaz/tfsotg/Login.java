package com.zpaz.tfsotg;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {

    private EditText tfsUrlField;
    private EditText projCollField;
    private EditText projectField;
    private EditText userField;
    private EditText patField;
    private Button testBtn;
    private Button nextBtn;
    private String outPutString = "";
    private String baseUrl = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(isThereLocalData()){
            try {
                loginWithLocallyStoredData();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } else {
            setTitle("Set up connection to TFS");
            initVars();
        }
    }

    private void initVars() {
        tfsUrlField = findViewById(R.id.tfsUrlField);
        projCollField = findViewById(R.id.projCollField);
        projectField = findViewById(R.id.projField);
        userField = findViewById(R.id.usernameField);
        patField = findViewById(R.id.patField);
        testBtn = findViewById(R.id.testBtn);
        nextBtn = findViewById(R.id.nextBtn);
        Button patBtn = findViewById(R.id.patInfoBtn);
        nextBtn.setEnabled(false);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                parseBaseUrl();
                String testUrl = baseUrl + "_apis/build/builds?definitions=25&statusFilter=completed&$top=1&api-version=2.0";
                String[] args = {testUrl, parseCreds(), outPutString};
                new testRequest().execute(args);
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                try {
                    storeUserData();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                startMain(parseCreds(), baseUrl, userField.getText().toString());
            }
        });
        patBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://docs.microsoft.com/en-us/vsts/accounts/use-personal-access-tokens-to-authenticate");
                Intent infoIntent = new Intent(Intent.ACTION_VIEW, uri);
                Toast.makeText(Login.super.getApplicationContext(), "Create PAT, come back and paste here", Toast.LENGTH_LONG).show();
                startActivity(infoIntent);
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMain(String creds, String baseUrl, String userName) {
        Intent mainIntent = new Intent(this, Main.class);
        mainIntent.putExtra("Creds", creds);
        mainIntent.putExtra("BaseUrl", baseUrl);
        mainIntent.putExtra("UserName", userName);
        startActivity(mainIntent);
        finish();
    }

    private boolean allFieldsValid() {
        return projCollField.getText().length() > 0 &&
                projectField.getText().length() > 0 &&
                tfsUrlField.getText().length() > 0 &&
                userField.getText().length() > 0 &&
                patField.getText().length() > 0;
    }

    private void parseBaseUrl() {
        if (!allFieldsValid()) {
            Toast.makeText(getBaseContext(), "Invalid values in fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String tfsAddress = tfsUrlField.getText().toString();
        String projectColl = projCollField.getText().toString();
        String project = projectField.getText().toString();

        baseUrl = tfsAddress + "/" + projectColl + "/" + project + "/";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String parseCreds() {
        String unEncoded = userField.getText().toString() + ":" + patField.getText().toString();
        return java.util.Base64.getEncoder().encodeToString(unEncoded.getBytes());
    }

    private void storeUserData() throws JSONException, IOException {
        String fileName = "userdata";
        JSONObject data = new JSONObject()
                .put("ProjectCollection",projCollField.getText().toString())
                .put("Project",projectField.getText().toString())
                .put("TfsUrl",tfsUrlField.getText().toString())
                .put("UserName",userField.getText().toString())
                .put("PAT",patField.getText().toString());
        FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
        fos.write(data.toString().getBytes());
        fos.close();
    }

    private boolean isThereLocalData(){
        boolean ifExists = false;
        for (String f : fileList()) {
            if(f.equals("userdata")){
                ifExists = true;
            }
        }
        return ifExists;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loginWithLocallyStoredData() throws IOException, JSONException {
       FileInputStream fis = openFileInput("userdata");
       StringBuilder dataAsString = new StringBuilder();
       int i;
       while((i = fis.read()) != -1){
           dataAsString.append((char) i);
       }
       fis.close();

       JSONObject data = new JSONObject(dataAsString.toString());
       String credsUnencoded = data.get("UserName").toString() + ":" + data.get("PAT").toString();
       String creds = java.util.Base64.getEncoder().encodeToString(credsUnencoded.getBytes());
       String baseUrl =
               data.get("TfsUrl").toString() + "/" +
               data.get("ProjectCollection").toString() + "/" +
               data.get("Project").toString() + "/";
       startMain(creds,baseUrl,data.get("UserName").toString());
    }

    private class testRequest extends AsyncTask<String, String, Boolean> {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = false;
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(params[0]);
                String credsEncoded = params[1];
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Authorization", "Basic " + credsEncoded);
                httpURLConnection.setDefaultUseCaches(true);
                result = httpURLConnection.getResponseMessage().equals("OK");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            onPostExecute(result);
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            String word = result ? "Success" : "Failed";
            setBtnText(testBtn, word);
            if (result) {
                enableBtn(nextBtn, true);
                enableBtn(testBtn, false);
            }
        }

        private void setBtnText(final Button button, final String word) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    button.setText(word);
                }
            });
        }

        private void enableBtn(final Button button, final Boolean enabled) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    button.setEnabled(enabled);
                }
            });
        }
    }
}
