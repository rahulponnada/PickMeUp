package com.umkc.pickmeup;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class StudentHomeActivity extends ActionBarActivity {

    private class StudentHomeService extends AsyncTask<String,Void,String> {

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
                //JSONObject jsonObject = new JSONObject(result);
                System.out.println(result);
                String[] output = result.split("]");
                String student = output[0].replaceAll("\"","").replaceAll("\\]","").replaceAll("\\[", "");
                System.out.println(student);

                String[] details = student.split(",");
                TextView firstName = (TextView)findViewById(R.id.fName);
                firstName.setText(details[0]);
                TextView lastName = (TextView)findViewById(R.id.lName);
                lastName.setText(details[1]);
                TextView email = (TextView)findViewById(R.id.email);
                email.setText(details[2]);
                TextView airlines = (TextView)findViewById(R.id.airlines);
                airlines.setText(details[3]);
                TextView flight = (TextView)findViewById(R.id.flight);
                flight.setText(details[4]);
                TextView volunteer = (TextView)findViewById(R.id.volunteer);
                volunteer.setText(details[5]);

                System.out.println(details[0]+""+details[1]+""+details[2]+""+details[3]+""+details[4]+""+details[5]);

                JSONObject jsonObject = new JSONObject(output[1]);
                JSONObject temp = new
                        JSONObject(jsonObject.getString("current_observation"));
                TextView weather = (TextView)findViewById(R.id.weather);
                weather.setText(temp.getString("temp_f")+" F and "+temp.getString("weather"));
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
        setContentView(R.layout.activity_student_home);
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE2);

        StudentHomeService stdService = new StudentHomeService();
        stdService.execute(new String[]{"http://10.0.2.2:52715/AuthService.svc/login/student/"+message,"http://api.wunderground.com/api/36b799dc821d5836/conditions/q/MO/Kansas%20City.json"});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_home, menu);
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
