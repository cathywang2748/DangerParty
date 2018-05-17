package com.kaplanteam.cathy.dangerparty.Level1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kaplanteam.cathy.dangerparty.EndGameActivity;
import com.kaplanteam.cathy.dangerparty.Level2.FragmentLevel2A;
import com.kaplanteam.cathy.dangerparty.R;

/**
 * Created by Cole on 3/24/18.
 */

public class FragmentLevel1A extends Fragment implements View.OnTouchListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Button octopus, doggiePaddle, corkscrew, dolphin, elegantUndulations;
    private Switch drown, pressbutton;
    private ImageView bubbleS, bubbleM, bubbleL, vortex;
    private double angle, initial, last, theta;

    private View timerView;
    private final int MILLIS_IN_FUTURE = 7000;
    private final int COUNT_DOWN_INTERVAL = 100;
    private final float SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
    private CountDownTimer t;

    private final int NUMBER_OF_STRINGS = 10;
    private String[] strings;
    private String[] currentStrings;
    private TextView text;

    private boolean popS, popM, popL;
    private int swirl;

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
        View rootView = inflater.inflate(R.layout.level1a, container, false);

        counter = this.getActivity().getSharedPreferences("HELLO", Context.MODE_PRIVATE);
        editor = counter.edit();


        //wire any widgets -- must use rootView.findViewById

        angle = 0;
        initial = 0;
        last = 0;
        theta = 0;
        popS = false;
        popM = false;
        popL = false;
        swirl = 0;
        successScore = 0;
        failScore = 0;
        firstTime = true;

        wireWidgets(rootView);
        setListeners();

        img = new ImageView[5];
        img[0] = liveFive;
        img[1] = liveFour;
        img[2] = liveThree;
        img[3] = liveTwo;
        img[4] = liveOne;

        strings = new String[NUMBER_OF_STRINGS];
        strings[0] = "Pop the bubbles";
        strings[1] = "Swirl a clockwise vortex";
        strings[2] = "Swirl a counterclockwise vortex";
        strings[3] = "Swim the octopus";
        strings[4] = "Swim the doggie paddle";
        strings[5] = "Swim the corkscrew";
        strings[6] = "Swim the dolphin";
        strings[7] = "Swim the elegant undulations";
        strings[8] = "Drown";
        strings[9] = "Pressbutton";

        currentStrings = new String[3];
        currentStrings[0] = "Still the vortex";
        currentStrings[1] = "Undrown";
        currentStrings[2] = "Don't pressbutton";

        text.setText("Get Ready");// could make ready set go or other animation type thing

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
                        Toast.makeText(getContext(), "Game Over", Toast.LENGTH_SHORT).show();
                        editor.putInt("score", successScore*100);
                        editor.commit();
                        Intent i = new Intent(getActivity(), EndGameActivity.class);
                        startActivity(i);
                    }
                    else{
                        img[END_GAME_FAILURES - failScore].setVisibility(View.INVISIBLE);
                        text.setText(strings[(int)(Math.random()*NUMBER_OF_STRINGS)]);
                        t.start();

                    }
                }
            }
        }.start();

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
        timerView = rootView.findViewById(R.id.timer);
        text = rootView.findViewById(R.id.textView);
        liveFive = rootView.findViewById(R.id.imageView_live_five);
        liveFour =rootView.findViewById(R.id.imageView_live_four);
        liveThree = rootView.findViewById(R.id.imageView_live_three);
        liveTwo = rootView.findViewById(R.id.imageView_live_two);
        liveOne = rootView.findViewById(R.id.imageView_live_one);
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
                popS = true;
                if(text.getText().equals(strings[0]) && popM && popL){ // will need to change later for 2 people -----------------------------------------------
                    success();
                }
                else if (!text.getText().equals(strings[0])){
                    successScore--;
                }
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
                        popS = false;
                    }
                }.start();
                break;
            case R.id.imageView_bubble_medium:
                popM = true;
                if(text.getText().equals(strings[0]) && popS && popL){ // will need to change later for 2 people -----------------------------------------------
                    success();
                }
                else if (!text.getText().equals(strings[0])){
                    successScore--;
                }
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
                        popM = false;
                    }
                }.start();
                break;
            case R.id.imageView_bubble_large:
                popL = true;
                if(text.getText().equals(strings[0]) && popM && popS){ // will need to change later for 2 people -----------------------------------------------
                    success();
                }
                else if (!text.getText().equals(strings[0])){
                    successScore--;
                }
                final ImageView vl = (ImageView) view;
                vl.setImageDrawable(getResources().getDrawable(R.drawable.pop));
                final Drawable bubble = FragmentLevel1A.this.getResources().getDrawable(R.drawable.bubble);
                CountDownTimer timerl = new CountDownTimer(4000, 2000) {
                    @Override
                    public void onTick(long l) {
                        vl.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFinish() {
                        vl.setImageDrawable(bubble);
                        vl.setVisibility(View.VISIBLE);
                        popL = false;
                    }
                }.start();
                break;
            case R.id.button_octopus:
                if(text.getText().equals(strings[3])){ // will need to change later for 2 people -----------------------------------------------
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button_doggie_paddle:
                if(text.getText().equals(strings[4])){ // will need to change later for 2 people -----------------------------------------------
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button_corkscrew:
                if(text.getText().equals(strings[5])){ // will need to change later for 2 people -----------------------------------------------
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button_dolphin:
                if(text.getText().equals(strings[6])){ // will need to change later for 2 people -----------------------------------------------
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button_elegant_undulations:
                if(text.getText().equals(strings[7])){ // will need to change later for 2 people -----------------------------------------------
                    success();
                }
                else{
                    successScore--;
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch(compoundButton.getId()){
            case R.id.switch_drown:
                if(text.getText().equals(strings[8])){ // will need to change later for 2 people -----------------------------------------------
                    success(8, 1);
                }
                else{
                    successScore--;
                    swap(8, 1);
                }
                break;
            case R.id.switch_pressbutton:
                if(text.getText().equals(strings[9])){ // will need to change later for 2 people -----------------------------------------------
                    success(9, 2);
                }
                else{
                    successScore--;
                    swap(9, 2);
                }
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
                if(angle < 180 + last){ //clockwise
                    ImageView v = (ImageView) view;
                    v.setImageDrawable(getResources().getDrawable(R.drawable.clockwise));
                    swirl = 1;
                }
                else { //counterclockwise
                    ImageView v = (ImageView) view;
                    v.setImageDrawable(getResources().getDrawable(R.drawable.counterclockwise));
                    swirl = -1;
                }
                last = angle;
                return true;
            case (MotionEvent.ACTION_UP):
                if(angle - initial < 30 && angle - initial > -30 ){
                    ImageView v = (ImageView) view;
                    v.setImageDrawable(getResources().getDrawable(R.drawable.vortex));
                    swirl = 0;
                }

                //"Swirl a clockwise vortex"
                //"Swirl a counterclockwise vortex"
                //"Still the vortex"
                if(text.getText().equals("Swirl a clockwise vortex") && swirl == 1
                        || text.getText().equals("Swirl a counterclockwise vortex") && swirl == -1
                        || text.getText().equals("Still the vortex") && swirl == 0){
                    if(strings[1].equals(text.getText())){
                        success(1, 0);
                    }
                    else{
                        success(2, 0);
                    }
                }
                else{
                    successScore--;
                    if(currentStrings[0].equals("Swirl a counterclockwise vortex") && swirl == 1
                            || currentStrings[0].equals("Swirl a counterclockwise vortex") && swirl == -1
                            || currentStrings[0].equals("Still the vortex") && swirl == 0){
                    }
                    else if (strings[1].equals("Swirl a counterclockwise vortex")){
                        if(swirl == 1){
                            swap(1, 0);
                        }
                        else {
                            swap(2, 0);
                        }
                    }
                    else{
                        if(swirl == 1){
                            swap(2, 0);
                        }
                        else {
                            swap(1, 0);
                        }
                    }
                }

                return true;
            default:
                return false;
        }
    }

    private void success(){
        t.cancel();
        successScore++;
        if(successScore >= MOVE_ON_SUCCESSES){
            //move to next level
            Toast.makeText(getContext(), "Move to Next Level", Toast.LENGTH_SHORT).show();
            editor.putInt("score", successScore*100);
            editor.commit();
            currentFragment = new FragmentLevel2A();//randomize?
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
           currentFragment = new FragmentLevel2A(); //randomize?
            switchToNewScreen();
            //Intent i = new Intent(getActivity(), EndGameActivity.class);
            //startActivity(i);
            editor.putInt("score", successScore*100);
            editor.commit();
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
}