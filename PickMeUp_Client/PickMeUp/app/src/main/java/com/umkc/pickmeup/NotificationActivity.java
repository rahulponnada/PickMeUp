package com.umkc.pickmeup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class NotificationActivity extends ActionBarActivity {

    private class NotificationService extends AsyncTask<String,Void,String> {

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
            super.onPreExecute();
            ProgressDialog pDialog = new ProgressDialog(NotificationActivity.this);
            pDialog.setMessage("Sending Email, Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                //JSONObject jsonObject = new JSONObject(result);
                Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
                finish();
                //Intent intent = new Intent(NotificationActivity.this, AdminHomeActivity.class);
                //startActivity(intent);
                }catch (Exception e) {
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
        setContentView(R.layout.activity_notification);
        final EditText message = (EditText)findViewById(R.id.notifyText);
        Button share = (Button)findViewById(R.id.share);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("notificatio-->" + message.getText().toString().replaceAll("\\r|\\n", "%0A").replaceAll(" ", "%20"));
                NotificationService notify = new NotificationService();
                notify.execute(new String[]{"http://10.0.2.2:52715/AuthService.svc/notifyall/"+message.getText().toString().replaceAll("\\r|\\n", "%0A").replaceAll(" ", "%20")});

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification, menu);
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
