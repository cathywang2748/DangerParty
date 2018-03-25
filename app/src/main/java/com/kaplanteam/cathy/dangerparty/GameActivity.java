package com.kaplanteam.cathy.dangerparty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

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
        needle.setOnTouchListener(this);

        final ImageView xMark = findViewById(R.id.imageView_x);
        xMark.setOnTouchListener(this);

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
                    return true;
                default :
                    return false;
            }
        }
        else if (view.getId() == R.id.imageView_x){
            float x, y, currentX, currentY;
            switch(action) {
                case (MotionEvent.ACTION_DOWN):
                    return true;
                case (MotionEvent.ACTION_MOVE):
                    x = motionEvent.getX();
                    y = motionEvent.getY();
                    currentX = view.getX();
                    currentY = view.getY();
                    view.setX(x + currentX - 32);
                    view.setY(y + currentY - 32);
                    return true;
                case (MotionEvent.ACTION_UP):
                    //snap to nearest 0 (71), 260, 496, 648
                    x = motionEvent.getX();
                    y = motionEvent.getY();
                    currentX = view.getX();
                    currentY = view.getY();
                    ArrayList<Float> locations = new ArrayList<>();
                    locations.add((float)71);
                    locations.add((float)260);
                    locations.add((float)496);
                    locations.add((float)648);
                    float minval = locations.get(0);
                    int min = 0;
                    for(int i = 1;i < locations.size();i++){
                        if (Math.pow(minval-(x+currentX), 2) > Math.pow(locations.get(i)-(x+currentX), 2)){
                            minval = locations.get(i);
                            min = i;
                        }
                    }
                    view.setX(minval);
                    if(min == 0)
                        view.setY(192);
                    else if(min == 1)
                        view.setY(114);
                    else if(min == 2)
                        view.setY(220);
                    else
                        view.setY(130);
                    return true;
                default:
                    return false;
            }
        }
        else
            return false;
    }
}
