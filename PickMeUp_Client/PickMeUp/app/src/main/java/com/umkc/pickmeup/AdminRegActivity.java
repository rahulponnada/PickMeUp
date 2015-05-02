package com.umkc.pickmeup;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AdminRegActivity extends ActionBarActivity {

    String gender;
    private class AdminRegistration extends AsyncTask<String,Void,String> {

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
                    Toast.makeText(getBaseContext(), "Account Succesfully Created - Please login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminRegActivity.this, MainActivity.class);

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
        setContentView(R.layout.activity_admin_reg);
        Button register = (Button)findViewById(R.id.admRegisterButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkValidations()) {

                    EditText studentID = (EditText)findViewById(R.id.admStudentIDField);
                    EditText password = (EditText)findViewById(R.id.admPasswordField);
                    EditText firstName = (EditText)findViewById(R.id.admFirstNameField);
                    EditText lastName = (EditText)findViewById(R.id.admLastNameField);
                    EditText phoneNo = (EditText)findViewById(R.id.admPhoneNumberField);
                    EditText address = (EditText)findViewById(R.id.admAddressField);
                    EditText university = (EditText)findViewById(R.id.admUniversityField);
                    EditText emailID = (EditText)findViewById(R.id.admEmailIDField);
                    if(gender==null||gender.isEmpty())
                    {
                        gender="Male";
                    }
                    AdminRegistration adminReg = new AdminRegistration();
                    //weatherService.execute(new String[]{"http://api.wunderground.com/api/36b799dc821d5836/conditions/q/MO/Kansas%20city.json"});
                    //http://localhost:52715/AuthService.svc/register/admin?studentid="+studentID.getText().toString()+"&passwd="+password.getText().toString()+"&firstname="+firstName.getText().toString()+"&lastname="+lastName.getText().toString()+"&email="+emailID.getText().toString()+"&gender="+gender+"&phone="+phoneNo.getText().toString()+"&address="+address.getText().toString().replaceAll(" ","%20")+"&university="+university.getText().toString().replaceAll(" ","%20")
                    adminReg.execute(new String[]{"http://10.0.2.2:52715/AuthService.svc/register/admin?studentid="+studentID.getText().toString()+"&passwd="+password.getText().toString()+"&firstname="+firstName.getText().toString()+"&lastname="+lastName.getText().toString()+"&email="+emailID.getText().toString()+"&gender="+gender+"&phone="+phoneNo.getText().toString()+"&address="+address.getText().toString().replaceAll(" ","%20")+"&university="+university.getText().toString().replaceAll(" ","%20")+""});

                }
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.admMaleRadioButton:
                if (checked)
                    gender ="Male";
                break;
            case R.id.admFemaleRadioButton:
                if (checked)
                    gender ="Female";
                break;
        }
    }


    private boolean checkValidations(){

        boolean flag=true;

        EditText studentID = (EditText)findViewById(R.id.admStudentIDField);
        EditText password = (EditText)findViewById(R.id.admPasswordField);
        EditText firstName = (EditText)findViewById(R.id.admFirstNameField);
        EditText lastName = (EditText)findViewById(R.id.admLastNameField);
        EditText phoneNo = (EditText)findViewById(R.id.admPhoneNumberField);
        EditText address = (EditText)findViewById(R.id.admAddressField);

        EditText university = (EditText)findViewById(R.id.admUniversityField);
        RadioGroup radioGroup = (RadioGroup)findViewById((R.id.admRadioGroup));

        EditText emailID = (EditText)findViewById(R.id.admEmailIDField);
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
        if(phoneNo.getText().toString()==null||phoneNo.getText().toString().isEmpty()){
            phoneNo.setError("Phone No is mandatory");
            flag = false;
        }
        if(address.getText().toString()==null||address.getText().toString().isEmpty()){
            address.setError("Address is mandatory");
            flag = false;
        }
        if(university.getText().toString()==null||university.getText().toString().isEmpty()){
            university.setError("University is mandatory");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_volunteer_reg, menu);
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
