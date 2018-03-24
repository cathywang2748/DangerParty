package com.kaplanteam.cathy.dangerparty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Cole on 3/22/18.
 */

public class GameActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private double angle;

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level1a); //later change to if statement or something
        Intent i = getIntent();


        final ImageView needle = findViewById(R.id.imageView_compass_needle);
        angle = 0;
        needle.setRotation((float)angle);
        needle.setOnTouchListener(this);

        wireWidgets();
        setOnClickListeners();

    }

    private void wireWidgets() {

    }

    private void setOnClickListeners() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = MotionEventCompat.getActionMasked(motionEvent);

        if(view.getId() == R.id.imageView_compass_needle){
            switch(action) {
                case (MotionEvent.ACTION_DOWN) :
                    Log.d("Motion Event: ","Action was DOWN");
                    return true;
                case (MotionEvent.ACTION_MOVE) :
                    float ivposx =  (view.getBottom() - view.getTop()) /2 - 75;
                    float ivposy = (view.getRight() - view.getLeft()) /2 + 80;
                    float me = motionEvent.getY();
                    float x = motionEvent.getX() - ivposx;
                    float y = ivposy - motionEvent.getY();
                    if(x!=0){
                        double theta = Math.atan(y/x);
                        theta = theta * 180/Math.PI;
                        if(x < 0){theta -= 180;}
                        theta = 90 - theta;
                        angle = theta + angle;
                        view.setRotation((float)angle);
                    }
                    return true;
                case (MotionEvent.ACTION_UP) :
                    angle = Math.round(angle/45)*45;
                    view.setRotation((float)angle);

                    Log.d("Motion Event: ","Action was UP");
                    return true;
                default :
                    return false;
            }
        }
        else if (view.getId() == R.id.imageView_x){

            return true;
        }
        else
            return false;
    }
}
