package com.kaplanteam.cathy.dangerparty.Level1;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kaplanteam.cathy.dangerparty.R;

/**
 * Created by Cole on 3/24/18.
 */

public class FragmentLevel1B extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private Button belly, back, front, giveUp;
    private ImageView sails, wheel, spyglass2, spyglass3;
    private boolean sailsOpen, spyglassOpen, wheelClockwise;
    private double angle, last, theta;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.level1b, container, false);

        sailsOpen = false;
        spyglassOpen = true;
        wireWidgets(rootView);
        setListeners();

        return rootView;
    }

    private void wireWidgets(View rootView) {
        belly = rootView.findViewById(R.id.button_belly);
        back = rootView.findViewById(R.id.button_back);
        front = rootView.findViewById(R.id.button_front);
        giveUp = rootView.findViewById(R.id.button_give_up);
        sails = rootView.findViewById(R.id.imageView_sails);
        wheel = rootView.findViewById(R.id.imageView_wheel);
        spyglass2 = rootView.findViewById(R.id.imageView_spyglass2);
        spyglass3 = rootView.findViewById(R.id.imageView_spyglass3);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
        belly.setOnClickListener(this);
        back.setOnClickListener(this);
        front.setOnClickListener(this);
        giveUp.setOnClickListener(this);
        sails.setOnClickListener(this);
        spyglass3.setOnTouchListener(this);
        wheel.setOnTouchListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_belly:
                break;
            case R.id.button_front:
                break;
            case R.id.button_back:
                break;
            case R.id.button_give_up:
                break;
            case R.id.imageView_sails:
                if(sailsOpen){
                    ImageView v = (ImageView) view;
                    v.setImageResource(R.drawable.sail_closed);
                }
                else{
                    ImageView v = (ImageView) view;
                    v.setImageResource(R.drawable.sail_open);
                }
                sailsOpen = !sailsOpen;
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = MotionEventCompat.getActionMasked(motionEvent);
        if(view.getId() == R.id.imageView_wheel){
            switch (action) {
                case (MotionEvent.ACTION_DOWN):
                    return true;
                case (MotionEvent.ACTION_MOVE):
                    float ivposx = (view.getBottom() - view.getTop()) / 2 - 75;
                    float ivposy = (view.getRight() - view.getLeft()) / 2 + 80;
                    float me = motionEvent.getY();
                    float x = motionEvent.getX() - ivposx;
                    float y = ivposy - motionEvent.getY();
                    if (x != 0) {
                        theta = Math.atan(y / x);
                        theta = theta * 180 / Math.PI;
                        if (x < 0) {
                            theta -= 180;
                        }
                        theta = 90 - theta;
                        angle = theta + angle;
                        view.setRotation((float) angle);
                    }
                    wheelClockwise = angle < 180 + last;
                    last = angle;
                    return true;
                case (MotionEvent.ACTION_UP):
                    if (wheelClockwise) { //starboard
                        Toast.makeText(getContext(), "CLOCKWISE", Toast.LENGTH_SHORT).show();
                    }
                    else{ //port
                        Toast.makeText(getContext(), "COUNTERCLOCKWISE", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                default:
                    return false;
            }
        }
        else if (view.getId() == R.id.imageView_spyglass3){
            float y  = 0;
            switch (action) {
                case (MotionEvent.ACTION_DOWN):
                    return true;
                case (MotionEvent.ACTION_MOVE):
                    y = view.getY() + motionEvent.getY();
                    if(y < dpToPx(138) + dpToPx(50) && y > dpToPx(0) + dpToPx(50)) {
                        view.setY(y - dpToPx(50));
                        spyglass2.setY(dpToPx(88) + view.getY()/2);
                    }
                    return true;
                case (MotionEvent.ACTION_UP):
                    y = view.getY() + motionEvent.getY();
                    if(y > dpToPx(138)/2 + dpToPx(50)){
                        view.setY(dpToPx(138));
                        spyglass2.setY(dpToPx(155));
                        spyglassOpen = true;//replace
                    } else {
                        view.setY(dpToPx(0));
                        spyglass2.setY(dpToPx(88));
                        spyglassOpen = false;
                    }
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
