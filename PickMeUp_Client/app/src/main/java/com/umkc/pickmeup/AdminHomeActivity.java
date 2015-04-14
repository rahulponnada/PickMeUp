package com.umkc.pickmeup;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class AdminHomeActivity extends ActionBarActivity {

    int count = 0;
    TableLayout tl;


    private class AdminHomeService extends AsyncTask<String,Void,String> {

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
                JSONArray volunteerList = new
                        JSONArray(jsonObject.getString("VolunteerList"));
                System.out.println("Result-->"+jsonObject.getString("VolunteerList"));
                //---print out the content of the json feed---
                for (int i = 0; i < volunteerList.length(); i++) {

                        JSONArray studentList =
                                new JSONArray(volunteerList.getJSONObject(i).getString("studentList"));

                        System.out.println("Student-->"+volunteerList.getJSONObject(i).getString("studentList"));
                        System.out.println("length"+studentList.length());
                        String volID="";
                        String volName="";
                        String stdID="";
                        String stdName="";
                        String stdDate="";
                        for(int j=0;j<studentList.length();j++)
                        {
                            if(j==0)
                            {
                            JSONObject student = studentList.getJSONObject(j);
                            volID = student.getString("userName");
                            volName = student.getString("fName");
                            System.out.println("record-->"+student.getString("fName"));
                            }
                            else if(j==1)
                            {
                                JSONObject student = studentList.getJSONObject(j);
                                stdID = student.getString("userName");
                                stdName = student.getString("fName");
                                stdDate = student.getString("arrivalTime");
                                addRow(volID,volName,stdID,stdName,stdDate);
                            }
                            else
                            {
                                JSONObject student = studentList.getJSONObject(j);
                                stdID = student.getString("userName");
                                stdName = student.getString("fName");
                                stdDate = student.getString("arrivalTime");
                                addRow(" "," ",stdID,stdName,stdDate);
                            }
                        }

                    //add(student.getString("fName"),student.getString("lName"),student.getString("address"));
                    /*Toast.makeText(getBaseContext(),
                            student.getString("fName") + " - " +
                                    student.getString("lName") + ", " +
                                    student.getString("address"),
                            Toast.LENGTH_SHORT).show();*/
                }
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
            /*System.out.println("<---result-->"+result);
            result = result.replaceAll("\"","");
            String[] data = result.split(";");
            String[] record;
            for (int i=0;i<data.length;i++){
                record = data[i].split(",");

                add(record[0],record[1],"12/12/12",record[2]);
            }*/
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //super.onProgressUpdate(values);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        tl = (TableLayout) findViewById(R.id.main_table);
        tl.setBackgroundColor(Color.BLACK);

        //View v1 = new View(this);
        //v1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));

        //View v2 = new View(this);
        //v2.setLayoutParams(new TableRow.LayoutParams(1, TableRow.LayoutParams.MATCH_PARENT));

        TableRow tr_head = new TableRow(this);
        int i=10;
        tr_head.setId(10+i);
        //tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        //tr_head.addView(v1);
        //tr_head.addView(v2);
        TextView vol_id = new TextView(this);
        vol_id.setId(20+i);
        vol_id.setText("Volunteer ID");
        vol_id.setTextColor(Color.parseColor("#804000"));
        vol_id.setPadding(5, 5, 5, 5);
        tr_head.addView(vol_id);// add the column to the table row here

        TextView vol_name = new TextView(this);
        vol_name.setId(30+i);
        vol_name.setText("Volunteer Name");
        vol_name.setTextColor(Color.parseColor("#804000"));
        vol_name.setPadding(5, 5, 5, 5);
        tr_head.addView(vol_name);// add the column to the table row here

        TextView std_id = new TextView(this);
        std_id.setId(40+i);// define id that must be unique
        std_id.setText("Student ID"); // set the text for the header
        std_id.setTextColor(Color.parseColor("#804000"));
        std_id.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(std_id); // add the column to the table row here

        TextView std_name = new TextView(this);
        std_name.setId(50+i);// define id that must be unique
        std_name.setText("Student Name"); // set the text for the header
        std_name.setTextColor(Color.parseColor("#804000"));
        std_name.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(std_name); // add the column to the table row here

        TextView std_date = new TextView(this);
        std_date.setId(60+i);// define id that must be unique
        std_date.setText("Arrival Date"); // set the text for the header
        std_date.setTextColor(Color.parseColor("#804000"));
        std_date.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(std_date); // add the column to the table row here


        tl.addView(tr_head, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE2);
        System.out.println("Volunteeetr UserName--->"+message);
        AdminHomeService admService= new AdminHomeService();
        admService.execute(new String[]{"http://10.0.2.2:52715/AuthService.svc/login/admin/"+message});

        //addRow(tl);
        //addRow(tl);
        //addRow(tl);
    }

    public void addRow(String volID,String volName,String stdID, String stdName,String stdDate){
        // Create the table row

        TableRow tr = new TableRow(this);
        //if(count%2!=0) tr.setBackgroundColor(Color.GRAY);
        tr.setId(100+count);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        //Create two columns to add as table data
        // Create a TextView to add date
        TextView volIDText = new TextView(this);
        volIDText.setId(200+count);
        volIDText.setText(volID);
        volIDText.setPadding(2, 0, 5, 0);
        if(volID!=null||!volID.isEmpty()) volIDText.setTextColor(Color.parseColor("#C0C040"));
        tr.addView(volIDText);

        TextView volNameText = new TextView(this);
        volNameText.setId(300+count);
        volNameText.setText(volName);
        volNameText.setPadding(2, 0, 5, 0);
        if(volName!=null||!volName.isEmpty()) volNameText.setTextColor(Color.parseColor("#C0C040"));
        tr.addView(volNameText);

        TextView stdIDText = new TextView(this);
        stdIDText.setId(400+count);
        stdIDText.setText(stdID);
        stdIDText.setPadding(2, 0, 5, 0);
        //if(stdID!=null||!stdID.isEmpty()) stdIDText.setTextColor(Color.parseColor("#C0C040"));
        tr.addView(stdIDText);

        TextView stdNameText = new TextView(this);
        stdNameText.setId(500+count);
        stdNameText.setText(stdName);
        stdNameText.setPadding(2, 0, 5, 0);
        //if(stdName!=null||!stdName.isEmpty()) stdNameText.setTextColor(Color.parseColor("#C0C040"));
        tr.addView(stdNameText);

        TextView stdDateText = new TextView(this);
        stdDateText.setId(600+count);
        stdDateText.setText(stdDate);
        stdDateText.setPadding(2, 0, 5, 0);
        //if(stdDate!=null||!stdDate.isEmpty()) stdDateText.setTextColor(Color.parseColor("#C0C040"));
        tr.addView(stdDateText);

        // finally add this to the table row
        tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        count++;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_home, menu);
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
