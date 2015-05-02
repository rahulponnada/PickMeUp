package com.umkc.pickmeup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StudentUpdateActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE2 = "com.umkc.pickmeup.MESSAGE2";
    String gender;
    TextView studentID;
    private class StudentUpdation extends AsyncTask<String,Void,String> {

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

                if(result.equalsIgnoreCase("1")) {
                    Toast.makeText(getBaseContext(), "Information Successfully Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StudentUpdateActivity.this,StudentHomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("StudentID",studentID.getText().toString().replaceAll(" ",""));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    //finish();
                }
            } catch (Exception e) {
                //Log.d("ReadPlacesFeedTask", e.getLocalizedMessage());
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
        setContentView(R.layout.activity_student_update);
        Bundle getData = getIntent().getExtras();
        //Intent intent = getIntent();


        studentID = (TextView)findViewById(R.id.stdStudentIDUpdateField);
        studentID.setText(getData.getString("StudentID"));

        EditText firstName = (EditText)findViewById(R.id.stdFirstNameUpdateField);
        firstName.setText(getData.getString("firstname"));

        EditText lastName = (EditText)findViewById(R.id.stdLastNameUpdateField);
        lastName.setText(getData.getString("lastname"));

        EditText email = (EditText)findViewById(R.id.stdEmailIDUpdateField);
        email.setText(getData.getString("email"));

        RadioButton male = (RadioButton)findViewById(R.id.stdMaleRadioButtonUpdate);
        RadioButton female = (RadioButton)findViewById(R.id.stdFemaleRadioButtonUpdate);
        if(getData.getString("gender")=="female"){
            female.setChecked(true);
        }
        else {
            male.setChecked(true);
        }
        EditText date = (EditText)findViewById(R.id.stdDateUpdateField);
        EditText time = (EditText)findViewById(R.id.stdTimeUpdateField);
        //String[] text = getData.getString("date").split(" ");
        date.setText("01/02/2015");
        time.setText("22:30:31");

        EditText airline = (EditText)findViewById(R.id.airlinesUpdateAutoComplete);
        airline.setText(getData.getString("airlines"));

        EditText flightNo = (EditText)findViewById(R.id.stdFlightNumberUpdateField);
        flightNo.setText(getData.getString("flight"));

        EditText address = (EditText)findViewById(R.id.stdAddressUpdateField);
        address.setText(getData.getString("address"));

        // Get a reference to the AutoCompleteTextView in the layout
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.airlinesUpdateAutoComplete);
        // Get the string array
        String[] airlines = getResources().getStringArray(R.array.airlines_array);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, airlines);
        textView.setAdapter(adapter);
        Button register = (Button)findViewById(R.id.stdUpdateButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(checkValidations()){
                   TextView studentID = (TextView)findViewById(R.id.stdStudentIDUpdateField);
                   EditText firstName = (EditText)findViewById(R.id.stdFirstNameUpdateField);
                   EditText lastName = (EditText)findViewById(R.id.stdLastNameUpdateField);
                   EditText arrivalDate = (EditText)findViewById(R.id.stdDateUpdateField);
                   EditText arrivalTime = (EditText)findViewById(R.id.stdTimeUpdateField);
                   EditText airline = (EditText)findViewById(R.id.airlinesUpdateAutoComplete);
                   EditText flightNo = (EditText)findViewById(R.id.stdFlightNumberUpdateField);
                   EditText address = (EditText)findViewById(R.id.stdAddressUpdateField);
                   EditText emailID = (EditText)findViewById(R.id.stdEmailIDUpdateField);
                   if(gender==null||gender.isEmpty())
                   {
                       gender="Male";
                   }
                   StudentUpdation studentUpdate = new StudentUpdation();
                   //weatherService.execute(new String[]{"http://api.wunderground.com/api/36b799dc821d5836/conditions/q/MO/Kansas%20city.json"});
                   studentUpdate.execute(new String[]{"http://10.0.2.2:52715/AuthService.svc/updateStudentDetails/student?studentid="+studentID.getText().toString()+"&firstname="+ firstName.getText().toString()+"&lastname="+lastName.getText().toString()+"&email="+emailID.getText().toString()+"&gender="+gender+"&arrivaltime="+arrivalDate.getText().toString()+"%20"+arrivalTime.getText().toString()+"&airlines="+airline.getText().toString()+"&flight="+flightNo.getText().toString()+"&address="+address.getText().toString().replaceAll(" ","%20")+""});
                   //http://localhost:52715/AuthService.svc/updateStudentDetails/student?studentid=1234&firstname=xxxx&lastname=ffff&email=anvesh525@gmail.com&gender=Female&arrivaltime=02/02/2015%2022:30:31&airlines=United&flight=XXXX&address=kavur
                }

            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.stdMaleRadioButtonUpdate:
                if (checked)
                    gender ="Male";
                    break;
            case R.id.stdFemaleRadioButtonUpdate:
                if (checked)
                    gender ="Female";
                    break;
        }
    }

    private boolean checkValidations(){

        boolean flag=true;

        EditText firstName = (EditText)findViewById(R.id.stdFirstNameUpdateField);
        EditText lastName = (EditText)findViewById(R.id.stdLastNameUpdateField);
        EditText arrivalDate = (EditText)findViewById(R.id.stdDateUpdateField);
        EditText arrivalTime = (EditText)findViewById(R.id.stdTimeUpdateField);
        EditText airlines = (EditText)findViewById(R.id.airlinesUpdateAutoComplete);
        EditText flightNo = (EditText)findViewById(R.id.stdFlightNumberUpdateField);
        EditText address = (EditText)findViewById(R.id.stdAddressUpdateField);

        EditText emailID = (EditText)findViewById(R.id.stdEmailIDUpdateField);
        if(firstName.getText().toString()==null||firstName.getText().toString().isEmpty()){
            firstName.setError("FirstName is mandatory");
            flag = false;
        }
        if(lastName.getText().toString()==null||lastName.getText().toString().isEmpty()){
            lastName.setError("LastName is mandatory");
            flag = false;
        }
        if(arrivalDate.getText().toString()==null||arrivalDate.getText().toString().isEmpty()){
            arrivalDate.setError("Arrival Date is mandatory");
            flag = false;
        }
        if(arrivalTime.getText().toString()==null||arrivalTime.getText().toString().isEmpty()){
            arrivalTime.setError("Arrival Time is mandatory");
            flag = false;
        }
        if(airlines.getText().toString()==null||airlines.getText().toString().isEmpty()){
            airlines.setError("Airlines is mandatory");
            flag = false;
        }
        if(flightNo.getText().toString()==null||flightNo.getText().toString().isEmpty()){
            flightNo.setError("Flight No is mandatory");
            flag = false;
        }
        if(address.getText().toString()==null||address.getText().toString().isEmpty()){
            address.setError("Address is mandatory");
            flag = false;
        }
        if(!isValidEmail(emailID.getText().toString()))
        {
            emailID.setError("Invalid Email");
            flag = false;
        }
        return flag;
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_reg, menu);
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
