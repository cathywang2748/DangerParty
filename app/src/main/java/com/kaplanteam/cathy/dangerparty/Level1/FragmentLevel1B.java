package com.kaplanteam.cathy.dangerparty.Level1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MotionEventCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaplanteam.cathy.dangerparty.EndGameActivity;
import com.kaplanteam.cathy.dangerparty.Level2.FragmentLevel2B;
import com.kaplanteam.cathy.dangerparty.R;

/**
 * Created by Cole on 3/24/18.
 */

public class FragmentLevel1B extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private Button belly, back, front, giveUp;
    private ImageView sails, wheel, spyglass2, spyglass3;
    private boolean sailsOpen, spyglassOpen, wheelClockwise;
    private double angle, last, theta;

    private View timerView;
    private final int MILLIS_IN_FUTURE = 7000;
    private final int COUNT_DOWN_INTERVAL = 100;
    private final float SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
    private CountDownTimer t;

    private final int NUMBER_OF_STRINGS = 8;
    private String[] strings;
    private String[] currentStrings;
    private TextView text;

    private int successScore;
    private int failScore;
    private final int MOVE_ON_SUCCESSES = 10;
    private final int END_GAME_FAILURES = 5;
    private ImageView liveOne, liveTwo, liveThree, liveFour, liveFive;
    private ImageView[] img;

    private Fragment currentFragment;
    private boolean firstTime;

    private SharedPreferences counter;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.level1b, container, false);

        counter = this.getActivity().getSharedPreferences("HELLO", Context.MODE_PRIVATE);
        editor = counter.edit();

        sailsOpen = false;
        spyglassOpen = true;
        wireWidgets(rootView);
        setListeners();

        img = new ImageView[5];
        img[0] = liveFive;
        img[1] = liveFour;
        img[2] = liveThree;
        img[3] = liveTwo;
        img[4] = liveOne;

        strings = new String[NUMBER_OF_STRINGS];
        strings[0] = "Belly flop";
        strings[1] = "Front flop";
        strings[2] = "Back flop";
        strings[3] = "Give up hope";
        strings[4] = "Turn to port";
        strings[5] = "Turn to starboard";
        strings[6] = "Open the sails";
        strings[7] = "Close the spyglass";

        currentStrings = new String[2];
        currentStrings[0] = "Close the sails";
        currentStrings[1] = "Open the spyglass";

        text.setText("Level 1: Danger Beach");// could make ready set go or other animation type thing

        //get any other initial set up done
        t = new CountDownTimer(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long l) {
                timerView.setX(l / (float) MILLIS_IN_FUTURE * SCREEN_WIDTH - SCREEN_WIDTH);
            }

            @Override
            public void onFinish() {
                if(firstTime){
                    text.setText(strings[(int)(Math.random()*NUMBER_OF_STRINGS)]);
                    t.start();
                    firstTime = false;
                }
                else{
                    timerView.setX(0 - SCREEN_WIDTH);
                    //closer to death
                    failScore++;
                    if(failScore >= END_GAME_FAILURES){
                        //End Game
                        editor.putInt("score", successScore*100);
                        editor.commit();
                        Intent i = new Intent(getActivity(), EndGameActivity.class);
                        startActivity(i);
                    }
                    else{
                        text.setText(strings[(int)(Math.random()*NUMBER_OF_STRINGS)]);
                        img[END_GAME_FAILURES- failScore].setVisibility(View.INVISIBLE);
                        t.start();
                    }
                }
            }
        }.start();

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
        timerView = rootView.findViewById(R.id.timer);
        text = rootView.findViewById(R.id.textView);
        liveOne = rootView.findViewById(R.id.imageView_live_one);
        liveTwo = rootView.findViewById(R.id.imageView_live_two);
        liveThree = rootView.findViewById(R.id.imageView_live_three);
        liveFour = rootView.findViewById(R.id.imageView_live_four);
        liveFive = rootView.findViewById(R.id.imageView_live_five);
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
                if(text.getText().equals(strings[0])){
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button_front:
                if(text.getText().equals(strings[1])){
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button_back:
                if(text.getText().equals(strings[2])){
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button_give_up:
                if(text.getText().equals(strings[3])){
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.imageView_sails:
                if(sailsOpen){
                    if(text.getText().equals("Close the sails")){
                        success(6, 0);
                        sails.setImageResource(R.drawable.sail_closed);
                    }
                    else{
                        successScore--;
                        swap(6, 0);
                        sails.setImageResource(R.drawable.sail_closed);
                    }
                }
                else{
                    if(text.getText().equals("Open the sails")){
                        success(6, 0);
                        sails.setImageResource(R.drawable.sail_open);
                    }
                    else{
                        successScore--;
                        swap(6, 0);
                        sails.setImageResource(R.drawable.sail_open);
                    }
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
                        if(text.getText().equals("Turn to starboard")){
                            success();
                        }
                        else{
                            successScore--;
                        }
                    }
                    else{ //port
                        if(text.getText().equals("Turn to port")){
                            success();
                        }
                        else{
                            successScore--;
                        }
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
                        spyglassOpen = true;
                    } else {
                        view.setY(dpToPx(0));
                        spyglass2.setY(dpToPx(88));
                        spyglassOpen = false;
                    }

                    if(text.getText().equals("Close the spyglass") && spyglassOpen
                            || text.getText().equals("Open the spyglass") && !spyglassOpen){
                        success(7, 1);
                    }
                    else{
                        swap(7, 1);
                        successScore--;
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

    private void success(){
        t.cancel();
        successScore++;
        if(successScore == MOVE_ON_SUCCESSES){
            //move to next level
            Toast.makeText(getContext(), "Move to Next Level", Toast.LENGTH_SHORT).show();
            editor.putInt("score", successScore*100);
            currentFragment = new FragmentLevel2B(); //randomize?
            switchToNewScreen();
        }
        else{
            text.setText(strings[(int)(Math.random()*NUMBER_OF_STRINGS)]);
            t.start();
        }
    }

    private void success(int string, int current){
        t.cancel();
        successScore++;
        if(successScore >= MOVE_ON_SUCCESSES){
            //move to next level
            Toast.makeText(getContext(), "Move to Next Level", Toast.LENGTH_SHORT).show();
            editor.putInt("score", successScore*100);
            editor.commit();
            currentFragment = new FragmentLevel2B(); //randomize?
            switchToNewScreen();
            //Intent i = new Intent(getActivity(), EndGameActivity.class);
            //startActivity(i);
            //editor.putInt("score", successScore*100);
            //editor.commit();
        }
        else{
            swap(string, current);
            text.setText(strings[(int)(Math.random()*NUMBER_OF_STRINGS)]);
            t.start();
        }
    }

    private void swap(int string, int current){
        String currentString = strings[string];
        strings[string] = currentStrings[current];
        currentStrings[current] = currentString;
    }

    private void switchToNewScreen() {
        //tell the fragment manager that if our current fragment isn't null, to replace whatever is there with it
        FragmentManager fm = getFragmentManager();
        if (currentFragment != null) {
            fm.beginTransaction()
                    .replace(R.id.fragment_container, currentFragment)
                    .commit();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        t.cancel();
    }
}
