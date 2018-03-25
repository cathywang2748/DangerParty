package com.kaplanteam.cathy.dangerparty;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import java.util.ArrayList;

/**
 * Created by Cole on 3/24/18.
 */

public class FragmentLevel1A extends Fragment implements View.OnTouchListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private double angle;
    private Switch getLostSwitch, zzyxzSwitch;
    private Button rub, hug;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.level1a, container, false);

        //wire any widgets -- must use rootView.findViewById
        angle = 0;
        final ImageView needle = rootView.findViewById(R.id.imageView_compass_needle);
        final ImageView xMark = rootView.findViewById(R.id.imageView_x);
        needle.setOnTouchListener(this);
        xMark.setOnTouchListener(this);

        wireWidgetsA(rootView);
        setListenersA();

        //get any other initial set up done

        //return the view that we inflated
        return rootView;
    }

    private void wireWidgetsA(View rootView) {
        rub = rootView.findViewById(R.id.button_rub);
        hug = rootView.findViewById(R.id.button_hug);
        getLostSwitch = rootView.findViewById(R.id.switch1);
        zzyxzSwitch = rootView.findViewById(R.id.switch2);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListenersA() {
        rub.setOnClickListener(this);
        hug.setOnClickListener(this);
        getLostSwitch.setOnCheckedChangeListener(this);
        zzyxzSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button_rub:
                break;
            case R.id.button_hug:
                break;
        }
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch(compoundButton.getId()){
            case R.id.switch1:
                break;
            case R.id.switch2:
                break;
        }
    }
}