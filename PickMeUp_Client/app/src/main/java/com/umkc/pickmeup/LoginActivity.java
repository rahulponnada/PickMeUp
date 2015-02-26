package com.umkc.pickmeup;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class LoginActivity extends ActionBarActivity {

    private class AuthenticationService extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder response = new StringBuilder();
            for (String url : urls) {

                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response.append(s);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println(response.toString());
            return response.toString();
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            try {

                JSONObject jsonObject = new JSONObject(result);
                Intent intent = getIntent();
                final String tableName = intent.getStringExtra(MainActivity.EXTRA_MESSAGE1);

                if(jsonObject.getString("status").equalsIgnoreCase("true")){
                    Toast.makeText(getBaseContext(),"Login Successfull",Toast.LENGTH_SHORT).show();
                    if(tableName.equalsIgnoreCase("Student"))
                    {
                        Intent intent1= new Intent(LoginActivity.this,StudentHomeActivity.class);
                        startActivity(intent1);
                    }
                    else {
                        Intent intent1 = new Intent(LoginActivity.this, VolunteerHomeActivity.class);
                        startActivity(intent1);
                    }

                }
                else {
                    Toast.makeText(getBaseContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //super.onProgressUpdate(values);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_login);
        final String tableName = intent.getStringExtra(MainActivity.EXTRA_MESSAGE1);
        String table="";

        System.out.println("Hello stupid idioit rascall--->"+tableName);
        Button login = (Button)findViewById(R.id.login);
        Button signUp = (Button)findViewById(R.id.signup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText studentID = (EditText)findViewById(R.id.studentID);
                EditText password =(EditText)findViewById(R.id.password);
                System.out.println("TableName-->"+tableName);
                AuthenticationService authService = new AuthenticationService();
                authService.execute(new String[]{"http://10.0.2.2:51981/AuthService.svc/login/"+tableName+"/"+studentID.getText().toString()+"/"+password.getText().toString()+""});

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tableName.equalsIgnoreCase("student"))
                {
                    Intent intent1= new Intent(LoginActivity.this,StudentRegActivity.class);
                    startActivity(intent1);
                }
                else {
                    Intent intent1 = new Intent(LoginActivity.this, VolunteerRegActivity.class);
                    startActivity(intent1);
                }

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
}
