package com.umkc.pickmeup;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StudentRegActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE2 = "com.umkc.pickmeup.MESSAGE2";
    String gender;
    private class StudentRegistration extends AsyncTask<String,Void,String> {

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
                    Toast.makeText(getBaseContext(), "Acoount Succesfully Created - Please login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StudentRegActivity.this, MainActivity.class);
                    startActivity(intent);
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
        Intent intent = getIntent();
        setContentView(R.layout.activity_student_reg);

        // Get a reference to the AutoCompleteTextView in the layout
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.airlinesAutoComplete);
        // Get the string array
        String[] airlines = getResources().getStringArray(R.array.airlines_array);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, airlines);
        textView.setAdapter(adapter);
        Button register = (Button)findViewById(R.id.stdRegisterButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(checkValidations()){
                   EditText studentID = (EditText)findViewById(R.id.stdStudentIDField);
                   EditText password = (EditText)findViewById(R.id.stdPasswordField);
                   EditText firstName = (EditText)findViewById(R.id.stdFirstNameField);
                   EditText lastName = (EditText)findViewById(R.id.stdLastNameField);
                   EditText arrivalDate = (EditText)findViewById(R.id.stdDateField);
                   EditText arrivalTime = (EditText)findViewById(R.id.stdTimeField);
                   EditText airline = (EditText)findViewById(R.id.airlinesAutoComplete);
                   EditText flightNo = (EditText)findViewById(R.id.stdFlightNumberField);
                   EditText address = (EditText)findViewById(R.id.stdAddressField);
                   EditText emailID = (EditText)findViewById(R.id.stdEmailIDField);
                   if(gender==null||gender.isEmpty())
                   {
                       gender="M";
                   }
                   StudentRegistration studentReg = new StudentRegistration();
                    //weatherService.execute(new String[]{"http://api.wunderground.com/api/36b799dc821d5836/conditions/q/MO/Kansas%20city.json"});
                    studentReg.execute(new String[]{"http://10.0.2.2:51981/AuthService.svc/register/student?studentid="+studentID.getText().toString()+"&passwd="+password.getText().toString()+"&firstname="+ firstName.getText().toString()+"&lastname="+lastName.getText().toString()+"&email="+emailID.getText().toString()+"&gender="+gender+"&arrivaldate="+arrivalDate.getText().toString()+"&arrivaltime=12:30:12&airlines="+airline.getText().toString()+"&flight="+flightNo.getText().toString()+"&address="+address.getText().toString()+""});

                }

            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.stdMaleRadioButton:
                if (checked)
                    gender ="M";
                    break;
            case R.id.stdFemaleRadioButton:
                if (checked)
                    gender ="F";
                    break;
        }
    }

    private boolean checkValidations(){

        boolean flag=true;

        EditText studentID = (EditText)findViewById(R.id.stdStudentIDField);
        EditText password = (EditText)findViewById(R.id.stdPasswordField);
        EditText firstName = (EditText)findViewById(R.id.stdFirstNameField);
        EditText lastName = (EditText)findViewById(R.id.stdLastNameField);
        EditText arrivalDate = (EditText)findViewById(R.id.stdDateField);
        EditText arrivalTime = (EditText)findViewById(R.id.stdTimeField);
        EditText airlines = (EditText)findViewById(R.id.airlinesAutoComplete);
        EditText flightNo = (EditText)findViewById(R.id.stdFlightNumberField);
        EditText address = (EditText)findViewById(R.id.stdAddressField);

        EditText emailID = (EditText)findViewById(R.id.stdEmailIDField);
        if(studentID.getText().toString()==null||studentID.getText().toString().isEmpty()){
            studentID.setError("StudentID is mandatory");
            flag = false;
        }
        if(password.getText().toString()==null||password.getText().toString().isEmpty()){
            password.setError("Password is mandatory");
            flag = false;
        }
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
