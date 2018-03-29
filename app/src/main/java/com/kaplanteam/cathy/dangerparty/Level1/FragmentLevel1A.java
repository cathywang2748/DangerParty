package com.kaplanteam.cathy.dangerparty.Level1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.kaplanteam.cathy.dangerparty.R;

/**
 * Created by Cole on 3/24/18.
 */

public class FragmentLevel1A extends Fragment implements View.OnTouchListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Button octopus, doggiePaddle, corkscrew, dolphin, elegantUndulations;
    private Switch drown, pressbutton;
    private ImageView bubbleS, bubbleM, bubbleL, vortex;
    private double angle, initial, last, theta;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.level1a, container, false);

        //wire any widgets -- must use rootView.findViewById

        angle = 0;
        initial = 0;
        last = 0;
        theta = 0;
        wireWidgets(rootView);
        setListeners();

        //get any other initial set up done

        //return the view that we inflated
        return rootView;
    }

    private void wireWidgets(View rootView) {
        octopus = rootView.findViewById(R.id.button_octopus);
        doggiePaddle = rootView.findViewById(R.id.button_doggie_paddle);
        corkscrew = rootView.findViewById(R.id.button_corkscrew);
        dolphin = rootView.findViewById(R.id.button_dolphin);
        elegantUndulations = rootView.findViewById(R.id.button_elegant_undulations);
        drown = rootView.findViewById(R.id.switch_drown);
        pressbutton = rootView.findViewById(R.id.switch_pressbutton);
        bubbleS = rootView.findViewById(R.id.imageView_bubble_small);
        bubbleM = rootView.findViewById(R.id.imageView_bubble_medium);
        bubbleL = rootView.findViewById(R.id.imageView_bubble_large);
        vortex = rootView.findViewById(R.id.imageView_vortex);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
        octopus.setOnClickListener(this);
        doggiePaddle.setOnClickListener(this);
        corkscrew.setOnClickListener(this);
        dolphin.setOnClickListener(this);
        elegantUndulations.setOnClickListener(this);
        drown.setOnCheckedChangeListener(this);
        pressbutton.setOnCheckedChangeListener(this);
        bubbleS.setOnClickListener(this);
        bubbleM.setOnClickListener(this);
        bubbleL.setOnClickListener(this);
        vortex.setOnTouchListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.imageView_bubble_small:
                final ImageView vs = (ImageView) view;
                vs.setImageDrawable(getResources().getDrawable(R.drawable.pop));
                CountDownTimer timers = new CountDownTimer(4000, 2000) {
                    @Override
                    public void onTick(long l) {
                        vs.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFinish() {
                        vs.setImageDrawable(getResources().getDrawable(R.drawable.bubble));
                        vs.setVisibility(View.VISIBLE);
                    }
                }.start();
                break;
            case R.id.imageView_bubble_medium:
                final ImageView vm = (ImageView) view;
                vm.setImageDrawable(getResources().getDrawable(R.drawable.pop));
                CountDownTimer timerm = new CountDownTimer(4000, 2000) {
                    @Override
                    public void onTick(long l) {
                        vm.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFinish() {
                        vm.setImageDrawable(getResources().getDrawable(R.drawable.bubble));
                        vm.setVisibility(View.VISIBLE);
                    }
                }.start();
                break;
            case R.id.imageView_bubble_large:
                final ImageView vl = (ImageView) view;
                vl.setImageDrawable(getResources().getDrawable(R.drawable.pop));
                CountDownTimer timerl = new CountDownTimer(4000, 2000) {
                    @Override
                    public void onTick(long l) {
                        vl.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFinish() {
                        vl.setImageDrawable(getResources().getDrawable(R.drawable.bubble));
                        vl.setVisibility(View.VISIBLE);
                    }
                }.start();
                break;
            case R.id.button_octopus:
                break;
            case R.id.button_doggie_paddle:
                break;
            case R.id.button_corkscrew:
                break;
            case R.id.button_dolphin:
                break;
            case R.id.button_elegant_undulations:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch(compoundButton.getId()){
            case R.id.switch_drown:
                break;
            case R.id.switch_pressbutton:
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = MotionEventCompat.getActionMasked(motionEvent);
        switch(action) {
            case (MotionEvent.ACTION_DOWN):
                initial = angle;
                return true;
            case (MotionEvent.ACTION_MOVE):
                float ivposx =  (view.getBottom() - view.getTop()) /2 - 75;
                float ivposy = (view.getRight() - view.getLeft()) /2 + 80;
                float me = motionEvent.getY();
                float x = motionEvent.getX() - ivposx;
                float y = ivposy - motionEvent.getY();
                if(x!=0){
                    theta = Math.atan(y/x);
                    theta = theta * 180/Math.PI;
                    if(x < 0){theta -= 180;}
                    theta = 90 - theta;
                    angle = theta + angle;
                    view.setRotation((float)angle);
                }
                Log.d("angle: ", "" + angle);
                if(angle < 180 + last){
                    ImageView v = (ImageView) view;
                    v.setImageDrawable(getResources().getDrawable(R.drawable.clockwise));
                    Log.d("CLOCKWISE", " Fired");
                }
                else {
                    ImageView v = (ImageView) view;
                    v.setImageDrawable(getResources().getDrawable(R.drawable.counterclockwise));
                    Log.d("COUNTERCLOCKWISE", " Fired");
                }
                last = angle;
                return true;
            case (MotionEvent.ACTION_UP):
                if(angle - initial < 30 && angle - initial > -30 ){
                    ImageView v = (ImageView) view;
                    v.setImageDrawable(getResources().getDrawable(R.drawable.vortex));
                }
                return true;
            default:
                return false;
        }
    }
}