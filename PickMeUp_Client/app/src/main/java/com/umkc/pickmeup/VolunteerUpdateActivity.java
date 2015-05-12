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


public class VolunteerUpdateActivity extends ActionBarActivity {

    TextView studentID;
    String gender;
    String days="";
    int monValue=0;
    int tueValue=0;
    int wedValue=0;
    int thuValue=0;
    int friValue=0;
    int satValue=0;
    int sunValue=0;
    private class VolunteerRegistration extends AsyncTask<String,Void,String> {

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
                    Intent intent = new Intent(VolunteerUpdateActivity.this, VolunteerHomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("StudentID",studentID.getText().toString().replaceAll(" ",""));
                    intent.putExtras(bundle);
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
        Bundle getData = getIntent().getExtras();
        setContentView(R.layout.activity_volunteer_update);

        studentID = (TextView)findViewById(R.id.volStudentIDUpdateField);
        studentID.setText(getData.getString("StudentID"));

        EditText firstName = (EditText)findViewById(R.id.volFirstNameUpdateField);
        firstName.setText(getData.getString("firstname"));

        EditText lastName = (EditText)findViewById(R.id.volLastNameUpdateField);
        lastName.setText(getData.getString("lastname"));

        EditText email = (EditText)findViewById(R.id.volEmailIDUpdateField);
        email.setText(getData.getString("email"));

        RadioButton male = (RadioButton)findViewById(R.id.volMaleRadioUpdateButton);
        RadioButton female = (RadioButton)findViewById(R.id.volFemaleRadioUpdateButton);
        if(getData.getString("gender")=="female"){
            female.setChecked(true);
        }
        else {
            male.setChecked(true);
        }

        EditText phone = (EditText)findViewById(R.id.volPhoneNumberUpdateField);
        phone.setText(getData.getString("phone"));

        EditText address = (EditText)findViewById(R.id.VolAddressUpdateField);
        address.setText(getData.getString("address"));

        Button update = (Button)findViewById(R.id.volUpdateButton);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkValidations()) {

                    EditText firstName = (EditText)findViewById(R.id.volFirstNameUpdateField);
                    EditText lastName = (EditText)findViewById(R.id.volLastNameUpdateField);
                    EditText phoneNo = (EditText)findViewById(R.id.volPhoneNumberUpdateField);
                    EditText address = (EditText)findViewById(R.id.VolAddressUpdateField);
                    EditText emailID = (EditText)findViewById(R.id.volEmailIDUpdateField);
                    if(gender==null||gender.isEmpty())
                    {
                        gender="Male";
                    }
                    System.out.println(monValue+"  "+tueValue+"  "+wedValue+"  "+thuValue+"  "+friValue+"  "+satValue+"  "+sunValue);
                    VolunteerRegistration volunteerReg = new VolunteerRegistration();
                    //weatherService.execute(new String[]{"http://api.wunderground.com/api/36b799dc821d5836/conditions/q/MO/Kansas%20city.json"});
                    volunteerReg.execute(new String[]{"http://10.0.2.2:52715/AuthService.svc/updateVolunteerDetails/volunteer?studentid="+studentID.getText().toString()+"&firstname="+firstName.getText().toString()+"&lastname="+lastName.getText().toString()+"&email="+emailID.getText().toString()+"&gender="+gender+"&phone="+phoneNo.getText().toString()+"&address="+address.getText().toString().replaceAll(" ","%20")+"&Mon="+monValue+"&Tue="+tueValue+"&Wed="+wedValue+"&Thu="+thuValue+"&Fri="+friValue+"&Sat="+satValue+"&Sun="+sunValue+""});
                    //http://localhost:52715/AuthService.svc/register/volunteer?studentid=90900&passwd=puski&firstname=jaffa&lastname=lalla&email=atmc9@mail.umkc.edu&gender=Male&phone=998998899&address=UMKC%20library&Mon=12&Tue=255&Wed=122&Thu=133&Fri=78&Sat=123&Sun=8
                }
            }
        });
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
       boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.check1:
                if (checked)
                    monValue=monValue + (int)Math.pow(2,0);
                break;
            case R.id.check2:
                if (checked)
                    monValue=monValue + (int)Math.pow(2,1);
                break;
            case R.id.check3:
                if (checked)
                    monValue=monValue + (int)Math.pow(2,2);
                break;
            case R.id.check4:
                if (checked)
                    monValue=monValue + (int)Math.pow(2,3);
                break;
            case R.id.check5:
                if (checked)
                    monValue=monValue + (int)Math.pow(2,4);
                break;
            case R.id.check6:
                if (checked)
                    monValue=monValue + (int)Math.pow(2,5);
                break;
            case R.id.check7:
                if (checked)
                    monValue=monValue + (int)Math.pow(2,6);
                break;
            case R.id.check8:
                if (checked)
                    monValue=monValue + (int)Math.pow(2,7);
                break;
            case R.id.check9:
                if (checked)
                    tueValue=tueValue + (int)Math.pow(2,0);
                break;
            case R.id.check10:
                if (checked)
                    tueValue=tueValue + (int)Math.pow(2,1);
                break;
            case R.id.check11:
                if (checked)
                    tueValue=tueValue + (int)Math.pow(2,2);
                break;
            case R.id.check12:
                if (checked)
                    tueValue=tueValue + (int)Math.pow(2,3);
                break;
            case R.id.check13:
                if (checked)
                    tueValue=tueValue + (int)Math.pow(2,4);
                break;
            case R.id.check14:
                if (checked)
                    tueValue=tueValue + (int)Math.pow(2,5);
                break;
            case R.id.check15:
                if (checked)
                    tueValue=tueValue + (int)Math.pow(2,6);
                break;
            case R.id.check16:
                if (checked)
                    tueValue=tueValue + (int)Math.pow(2,7);
                break;
            case R.id.check17:
                if (checked)
                    wedValue=wedValue + (int)Math.pow(2,0);
                break;
            case R.id.check18:
                if (checked)
                    wedValue=wedValue + (int)Math.pow(2,1);
                break;
            case R.id.check19:
                if (checked)
                    wedValue=wedValue + (int)Math.pow(2,2);
                break;
            case R.id.check20:
                if (checked)
                    wedValue=wedValue + (int)Math.pow(2,3);
                break;
            case R.id.check21:
                if (checked)
                    wedValue=wedValue + (int)Math.pow(2,4);
                break;
            case R.id.check22:
                if (checked)
                    wedValue=wedValue + (int)Math.pow(2,5);
                break;
            case R.id.check23:
                if (checked)
                    wedValue=wedValue + (int)Math.pow(2,6);
                break;
            case R.id.check24:
                if (checked)
                    wedValue=wedValue + (int)Math.pow(2,7);
                break;
            case R.id.check25:
                if (checked)
                    thuValue=thuValue + (int)Math.pow(2,0);
                break;
            case R.id.check26:
                if (checked)
                    thuValue=thuValue + (int)Math.pow(2,1);
                break;
            case R.id.check27:
                if (checked)
                    thuValue=thuValue + (int)Math.pow(2,2);
                break;
            case R.id.check28:
                if (checked)
                    thuValue=thuValue + (int)Math.pow(2,3);
                break;
            case R.id.check29:
                if (checked)
                    thuValue=thuValue + (int)Math.pow(2,4);
                break;
            case R.id.check30:
                if (checked)
                    thuValue=thuValue + (int)Math.pow(2,5);
                break;
            case R.id.check31:
                if (checked)
                    thuValue=thuValue + (int)Math.pow(2,6);
                break;
            case R.id.check32:
                if (checked)
                    thuValue=thuValue + (int)Math.pow(2,7);
                break;
            case R.id.check33:
                if (checked)
                    friValue=friValue + (int)Math.pow(2,0);
                break;
            case R.id.check34:
                if (checked)
                    friValue=friValue + (int)Math.pow(2,1);
                break;
            case R.id.check35:
                if (checked)
                    friValue=friValue + (int)Math.pow(2,2);
                break;
            case R.id.check36:
                if (checked)
                    friValue=friValue + (int)Math.pow(2,3);
                break;
            case R.id.check37:
                if (checked)
                    friValue=friValue + (int)Math.pow(2,4);
                break;
            case R.id.check38:
                if (checked)
                    friValue=friValue + (int)Math.pow(2,5);
                break;
            case R.id.check39:
                if (checked)
                    friValue=friValue + (int)Math.pow(2,6);
                break;
            case R.id.check40:
                if (checked)
                    friValue=friValue + (int)Math.pow(2,7);
                break;
            case R.id.check41:
                if (checked)
                    satValue=satValue + (int)Math.pow(2,0);
                break;
            case R.id.check42:
                if (checked)
                    satValue=satValue + (int)Math.pow(2,1);
                break;
            case R.id.check43:
                if (checked)
                    satValue=satValue + (int)Math.pow(2,2);
                break;
            case R.id.check44:
                if (checked)
                    satValue=satValue + (int)Math.pow(2,3);
                break;
            case R.id.check45:
                if (checked)
                    satValue=satValue + (int)Math.pow(2,4);
                break;
            case R.id.check46:
                if (checked)
                    satValue=satValue + (int)Math.pow(2,5);
                break;
            case R.id.check47:
                if (checked)
                    satValue=satValue + (int)Math.pow(2,6);
                break;
            case R.id.check48:
                if (checked)
                    satValue=satValue + (int)Math.pow(2,7);
                break;
            case R.id.check49:
                if (checked)
                    sunValue=sunValue + (int)Math.pow(2,0);
                break;
            case R.id.check50:
                if (checked)
                    sunValue=sunValue + (int)Math.pow(2,1);
                break;
            case R.id.check51:
                if (checked)
                    sunValue=sunValue + (int)Math.pow(2,2);
                break;
            case R.id.check52:
                if (checked)
                    sunValue=sunValue + (int)Math.pow(2,3);
                break;
            case R.id.check53:
                if (checked)
                    sunValue=sunValue + (int)Math.pow(2,4);
                break;
            case R.id.check54:
                if (checked)
                    sunValue=sunValue + (int)Math.pow(2,5);
                break;
            case R.id.check55:
                if (checked)
                    sunValue=sunValue + (int)Math.pow(2,6);
                break;
            case R.id.check56:
                if (checked)
                    sunValue=sunValue + (int)Math.pow(2,7);
                break;
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.volMaleRadioButton:
                if (checked)
                    gender ="Male";
                break;
            case R.id.volFemaleRadioButton:
                if (checked)
                    gender ="Female";
                break;
        }
    }


    private boolean checkValidations(){

        boolean flag=true;

        EditText firstName = (EditText)findViewById(R.id.volFirstNameUpdateField);
        EditText lastName = (EditText)findViewById(R.id.volLastNameUpdateField);
        EditText phoneNo = (EditText)findViewById(R.id.volPhoneNumberUpdateField);
        EditText address = (EditText)findViewById(R.id.VolAddressUpdateField);

        EditText emailID = (EditText)findViewById(R.id.volEmailIDUpdateField);
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
