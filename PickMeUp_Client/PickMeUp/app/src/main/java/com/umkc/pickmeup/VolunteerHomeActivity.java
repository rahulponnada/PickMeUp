package com.umkc.pickmeup;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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


public class VolunteerHomeActivity extends ActionBarActivity {

    ValueAnimator mAnimator;
    LinearLayout container;

    private class VolunteerHomeService extends AsyncTask<String,Void,String> {

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
                JSONArray studentList = new
                        JSONArray(jsonObject.getString("studentList"));

                //---print out the content of the json feed---
                for (int i = 0; i < studentList.length(); i++) {
                    JSONObject student =
                            studentList.getJSONObject(i);

                    add(student.getString("fName"),student.getString("lName"),student.getString("address"));
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
        Intent intent = getIntent();
        setContentView(R.layout.activity_volunteer_home);
        container = (LinearLayout)findViewById(R.id.container);

        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE2);
        System.out.println("Volunteeetr UserName--->"+message);
        Button navigate = (Button) findViewById(R.id.navigate);

        VolunteerHomeService volService= new VolunteerHomeService();
        volService.execute(new String[]{"http://10.0.2.2:52715/AuthService.svc/login/volunteer/"+message});

    }
    private void add(String fname,String lname,final String addr){

        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.activity_expand, null);

        final RelativeLayout mLinearLayout = (RelativeLayout) addView.findViewById(R.id.expandable);
        //mLinearLayout.setVisibility(View.GONE);
        final LinearLayout mLinearLayoutHeader = (LinearLayout) addView.findViewById(R.id.header);
        TextView clickme = (TextView)addView.findViewById(R.id.clickme);
        TextView firstName = (TextView)addView.findViewById(R.id.fName);
        TextView lastName = (TextView)addView.findViewById(R.id.lName);
        TextView address = (TextView)addView.findViewById(R.id.address);
        Button navigate = (Button) addView.findViewById(R.id.navigate);

        clickme.setText(fname);
        firstName.setText(fname);
        lastName.setText(lname);
        address.setText(addr);
        navigate.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+addr+",+KansasCity,+MO+64112&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        //mAnimator;
        //Add onPreDrawListener
        //ValueAnimator mAnimator;
        mLinearLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        mLinearLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        mLinearLayout.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        mLinearLayout.measure(widthSpec, heightSpec);

                        mAnimator = slideAnimator(mLinearLayout,0, mLinearLayout.getMeasuredHeight());
                        return true;
                    }
                });


        mLinearLayoutHeader.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mLinearLayout.getVisibility()==View.GONE){
                    expand(mLinearLayout);
                }else{
                    collapse(mLinearLayout);
                }
            }
        });

        container.addView(addView);
    }

    private void expand(RelativeLayout mLinearLayout) {
        //set Visible
        mLinearLayout.setVisibility(View.VISIBLE);

		/* Remove and used in preDrawListener
		final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		mLinearLayout.measure(widthSpec, heightSpec);
		mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
		*/

        mAnimator.start();
    }

    private void collapse(final RelativeLayout mLinearLayout) {
        int finalHeight = mLinearLayout.getHeight();

        ValueAnimator mAnimator = slideAnimator(mLinearLayout,finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                mLinearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }


    private ValueAnimator slideAnimator(final RelativeLayout mLinearLayout,int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = mLinearLayout.getLayoutParams();
                layoutParams.height = value;
                mLinearLayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_volunteer_home, menu);
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
