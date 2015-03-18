package com.umkc.pickmeup;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class expandActivity extends Activity {

    ArrayList<RelativeLayout> mLinearLayout = new ArrayList<RelativeLayout>();
    ArrayList<LinearLayout> mLinearLayoutHeader = new ArrayList<LinearLayout>();
    ValueAnimator mAnimator;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_home);
        container = (LinearLayout)findViewById(R.id.container);

        add(0,"satish","anumolu","12/12/12","4914 Grand Avenue");
        add(1,"Rahul","anumolu","12/12/12","4914 Grand Avenue");
        add(2,"Anvesh","anumolu","12/12/12","4914 Grand Avenue");

    }
    private void add(final int i,String fname,String lname, String date,String addr){

        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.activity_expand, null);

        mLinearLayout.add((RelativeLayout) addView.findViewById(R.id.expandable));
        //mLinearLayout.setVisibility(View.GONE);
        mLinearLayoutHeader.add((LinearLayout) addView.findViewById(R.id.header));
        TextView firstName = (TextView)addView.findViewById(R.id.fName);
        TextView lastName = (TextView)addView.findViewById(R.id.lName);
        TextView address = (TextView)addView.findViewById(R.id.address);

        firstName.setText(fname);
        lastName.setText(lname);
        address.setText(addr);
        //mAnimator;
        //Add onPreDrawListener
        //ValueAnimator mAnimator;
        mLinearLayout.get(i).getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        mLinearLayout.get(i).getViewTreeObserver().removeOnPreDrawListener(this);
                        mLinearLayout.get(i).setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        mLinearLayout.get(i).measure(widthSpec, heightSpec);

                        mAnimator = slideAnimator(mLinearLayout.get(i),0, mLinearLayout.get(i).getMeasuredHeight());
                        return true;
                    }
                });


        mLinearLayoutHeader.get(i).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mLinearLayout.get(i).getVisibility()==View.GONE){
                    expand(mLinearLayout.get(i));
                }else{
                    collapse(mLinearLayout.get(i));
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
        getMenuInflater().inflate(R.menu.menu_expand, menu);
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
