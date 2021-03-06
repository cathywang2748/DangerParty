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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.kaplanteam.cathy.dangerparty.BluetoothActivity;
import com.kaplanteam.cathy.dangerparty.EndGameActivity;
import com.kaplanteam.cathy.dangerparty.Level2.FragmentLevel2A2P;
import com.kaplanteam.cathy.dangerparty.R;

/**
 * Created by Cole on 5/23/18.
 */

public class FragmentLevel1A2P  extends Fragment implements View.OnTouchListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Button octopus, doggiePaddle, corkscrew, dolphin, elegantUndulations;
    private Switch drown, pressbutton;
    private ImageView bubbleS, bubbleM, bubbleL, vortex;
    private double angle, initial, last, theta;

    private View timerView;
    private final int MILLIS_IN_FUTURE = 7000;
    private final int COUNT_DOWN_INTERVAL = 100;
    private final float SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
    private CountDownTimer t;

    private final int NUMBER_OF_STRINGS = 18;
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

    private BluetoothActivity a;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.level1a, container, false);

        counter = this.getActivity().getSharedPreferences("HELLO", Context.MODE_PRIVATE);
        editor = counter.edit();


        a = (BluetoothActivity) getActivity();
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
        //1B layout strings
        strings[10] = "Belly flop";
        strings[11] = "Front flop";
        strings[12] = "Back flop";
        strings[13] = "Give up hope";
        strings[14] = "Turn to port";
        strings[15] = "Turn to starboard";
        strings[16] = "Open the sails";
        strings[17] = "Close the spyglass";

        currentStrings = new String[5];
        currentStrings[0] = "Still the vortex";
        currentStrings[1] = "Undrown";
        currentStrings[2] = "Don't pressbutton";
        //1B layout strings
        currentStrings[3] = "Close the sails";
        currentStrings[4] = "Open the spyglass";

        text.setText("Level 1: Danger Beach");// could make ready set go or other animation type thing

        //get any other initial set up done
        t = new CountDownTimer(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long l) {
                timerView.setX(l / (float) MILLIS_IN_FUTURE * SCREEN_WIDTH - SCREEN_WIDTH);
                if(a.failures > failScore){ //failure
                    Log.d("Fail from foreign", "Fired");
                    failScore++;
                    if(failScore >= END_GAME_FAILURES){
                        //End Game
                        editor.putInt("score", successScore*100);
                        editor.commit();
                        Intent i = new Intent(getActivity(), EndGameActivity.class);
                        startActivity(i);
                    }
                    else{
                        img[END_GAME_FAILURES - failScore].setVisibility(View.INVISIBLE);
                        int index = (int) (Math.random()*NUMBER_OF_STRINGS);
                        text.setText(strings[index]);
                        a.sendReceive.write((index + "").getBytes());
                        t.start();

                    }
                }
                if(a.successes > successScore){ //success
                    Log.d("Success from foreign", "Fired");
                    if(!a.domesticSuccess){
                        successDomestic();
                    }
                    else{
                        successSilent();
                    }
                }
                if(a.swapReady){//swap
                    Log.d("Swap from foreign", "Fired");
                    swapFromForeign(a.swapString, a.swapCurrent);
                    a.swapNotReady();
                }
            }

            @Override
            public void onFinish() {
                if(firstTime){
                    int index = (int) (Math.random()*NUMBER_OF_STRINGS);
                    text.setText(strings[index]);
                    a.sendReceive.write((index + "").getBytes());
                    t.start();
                    firstTime = false;
                }
                else{
                    Log.d("Fail", "deleted skull");
                    timerView.setX(0 - SCREEN_WIDTH);
                    //closer to death for both screens --------------------------------------------------------------------
                    failScore++;
                    a.sendReceive.write("x".getBytes());
                    if(failScore >= END_GAME_FAILURES){
                        //End Game
                        editor.putInt("score", successScore*100);
                        editor.commit();
                        Intent i = new Intent(getActivity(), EndGameActivity.class);
                        startActivity(i);
                    }
                    else{
                        img[END_GAME_FAILURES - failScore].setVisibility(View.INVISIBLE);
                        int index = (int) (Math.random()*NUMBER_OF_STRINGS);
                        text.setText(strings[index]);
                        a.sendReceive.write((index + "").getBytes());
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
                if(text.getText().equals(strings[0]) && popM && popL && strings[Integer.parseInt(a.commandForeign)].equals(strings[0])){
                    success();
                    successForeign();
                }
                else if(text.getText().equals(strings[0]) && popM && popL){
                    success();
                }
                else if (strings[Integer.parseInt(a.commandForeign)].equals(strings[0]) && popM && popL){
                    successForeign();
                }
                else if (!text.getText().equals(strings[0]) && !strings[Integer.parseInt(a.commandForeign)].equals(strings[0])){
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
                if(text.getText().equals(strings[0]) && popS && popL && strings[Integer.parseInt(a.commandForeign)].equals(strings[0])){
                    success();
                    successForeign();
                }
                else if(text.getText().equals(strings[0]) && popS && popL){
                    success();
                }
                else if (strings[Integer.parseInt(a.commandForeign)].equals(strings[0]) && popS && popL){
                    successForeign();
                }
                else if (!text.getText().equals(strings[0]) && !strings[Integer.parseInt(a.commandForeign)].equals(strings[0])){
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
                if(text.getText().equals(strings[0]) && popM && popS && strings[Integer.parseInt(a.commandForeign)].equals(strings[0])){
                    success();
                    successForeign();
                }
                else if(text.getText().equals(strings[0]) && popM && popS){
                    success();
                }
                else if (strings[Integer.parseInt(a.commandForeign)].equals(strings[0]) && popM && popS){
                    successForeign();
                }
                else if (!text.getText().equals(strings[0]) && !strings[Integer.parseInt(a.commandForeign)].equals(strings[0])){
                    successScore--;
                }
                final ImageView vl = (ImageView) view;
                vl.setImageDrawable(getResources().getDrawable(R.drawable.pop));
                final Drawable bubble = FragmentLevel1A2P.this.getResources().getDrawable(R.drawable.bubble);
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
                if(text.getText().equals(strings[3]) && strings[Integer.parseInt(a.commandForeign)].equals(strings[3])){
                    successForeign();
                    success();
                }
                else if(text.getText().equals(strings[3])){
                    success();
                }
                else if(strings[Integer.parseInt(a.commandForeign)].equals(strings[3])){
                    successForeign();
                }
                else{
                    successScore--;
                }

                break;
            case R.id.button_doggie_paddle:
                if(text.getText().equals(strings[4]) && strings[Integer.parseInt(a.commandForeign)].equals(strings[4])){
                    successForeign();
                    success();
                }
                else if(text.getText().equals(strings[4])){
                    success();
                }
                else if(strings[Integer.parseInt(a.commandForeign)].equals(strings[4])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button_corkscrew:
                if(text.getText().equals(strings[5]) && strings[Integer.parseInt(a.commandForeign)].equals(strings[5])){
                    successForeign();
                    success();
                }
                else if(text.getText().equals(strings[5])){
                    success();
                }
                else if(strings[Integer.parseInt(a.commandForeign)].equals(strings[5])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button_dolphin:
                if(text.getText().equals(strings[6]) && strings[Integer.parseInt(a.commandForeign)].equals(strings[6])){
                    successForeign();
                    success();
                }
                else if(text.getText().equals(strings[6])){
                    success();
                }
                else if(strings[Integer.parseInt(a.commandForeign)].equals(strings[6])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button_elegant_undulations:
                if(text.getText().equals(strings[7]) && strings[Integer.parseInt(a.commandForeign)].equals(strings[7])){
                    successForeign();
                    success();
                }
                else if(text.getText().equals(strings[7])){
                    success();
                }
                else if(strings[Integer.parseInt(a.commandForeign)].equals(strings[7])){
                    successForeign();
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
                if(text.getText().equals(strings[8]) && strings[Integer.parseInt(a.commandForeign)].equals(strings[8])){
                    successForeign();
                    success(8, 1);
                }
                else if(text.getText().equals(strings[8])){
                    success(8, 1);
                }
                else if(strings[Integer.parseInt(a.commandForeign)].equals(strings[8])){
                    successForeign();
                    swap(8, 1);
                }
                else{
                    successScore--;
                    swap(8, 1);
                }
                //swap foreign --------------------------------------------------------------------------
                break;
            case R.id.switch_pressbutton:
                if(text.getText().equals(strings[9]) && strings[Integer.parseInt(a.commandForeign)].equals(strings[9])){
                    successForeign();
                    success(9, 2);
                }
                else if(text.getText().equals(strings[9])){
                    success(9, 2);
                }
                else if(strings[Integer.parseInt(a.commandForeign)].equals(strings[9])){
                    successForeign();
                    swap(9, 2);
                }
                else{
                    successScore--;
                    swap(9, 2);
                }
                //swap foreign --------------------------------------------------------------------------
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
                if((text.getText().equals("Swirl a clockwise vortex") && swirl == 1
                        || text.getText().equals("Swirl a counterclockwise vortex") && swirl == -1
                        || text.getText().equals("Still the vortex") && swirl == 0)
                        && (strings[Integer.parseInt(a.commandForeign)].equals("Swirl a clockwise vortex") && swirl == 1
                        || strings[Integer.parseInt(a.commandForeign)].equals("Swirl a counterclockwise vortex") && swirl == -1
                        || strings[Integer.parseInt(a.commandForeign)].equals("Still the vortex") && swirl == 0)){
                    if(strings[1].equals(text.getText())){
                        successForeign();
                        success(1, 0); //swap foreign --------------------------------------------------------------------------
                    }
                    else{
                        successForeign();
                        success(2, 0); //swap foreign --------------------------------------------------------------------------
                    }
                }
                else if(text.getText().equals("Swirl a clockwise vortex") && swirl == 1
                        || text.getText().equals("Swirl a counterclockwise vortex") && swirl == -1
                        || text.getText().equals("Still the vortex") && swirl == 0){
                    if(strings[1].equals(text.getText())){
                        success(1, 0); //swap foreign --------------------------------------------------------------------------
                    }
                    else{
                        success(2, 0); //swap foreign --------------------------------------------------------------------------
                    }
                }
                else if(strings[Integer.parseInt(a.commandForeign)].equals("Swirl a clockwise vortex") && swirl == 1
                        || strings[Integer.parseInt(a.commandForeign)].equals("Swirl a counterclockwise vortex") && swirl == -1
                        || strings[Integer.parseInt(a.commandForeign)].equals("Still the vortex") && swirl == 0){
                    if(strings[1].equals(strings[Integer.parseInt(a.commandForeign)])){
                        successForeign(); //swap foreign --------------------------------------------------------------------------
                        swap(1, 0);
                    }
                    else{
                        successForeign(); //swap foreign --------------------------------------------------------------------------
                        swap(2, 0);
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
                            swap(1, 0); //swap foreign --------------------------------------------------------------------------
                        }
                        else {
                            swap(2, 0); //swap foreign --------------------------------------------------------------------------
                        }
                    }
                    else{
                        if(swirl == 1){
                            swap(2, 0); //swap foreign --------------------------------------------------------------------------
                        }
                        else {
                            swap(1, 0); //swap foreign --------------------------------------------------------------------------
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
        a.sendReceive.write("d".getBytes());
        if(successScore >= MOVE_ON_SUCCESSES){
            //move to next level
            a.resetSandF();
            editor.putInt("score", successScore*100);
            editor.commit();
            currentFragment = new FragmentLevel2A2P();//randomize?
            switchToNewScreen();

        }
        else{
            //send message-----------------------------------------------------------------------------------------
            int index = (int) (Math.random()*NUMBER_OF_STRINGS);
            text.setText(strings[index]);
            a.sendReceive.write((index + "").getBytes());
            t.start();
        }
    }

    private void success(int string, int current){
        t.cancel();
        successScore++;
        a.sendReceive.write("d".getBytes());
        if(successScore >= MOVE_ON_SUCCESSES){
            //move to next level
            a.resetSandF();
            currentFragment = new FragmentLevel2A2P(); //randomize?
            switchToNewScreen();
            editor.putInt("score", successScore*100);
            editor.commit();
        }
        else{
            //send message (other will need to swap)-------------------------------------------------------------------------------
            swap(string, current);
            int index = (int) (Math.random()*NUMBER_OF_STRINGS);
            text.setText(strings[index]);
            a.sendReceive.write((index + "").getBytes());
            t.start();
        }
    }

    private void swap(int string, int current){
        a.sendReceive.write(("w" + string + "*" + current).getBytes());
        String currentString = strings[string];
        strings[string] = currentStrings[current];
        currentStrings[current] = currentString;
    }

    private void swapFromForeign(int string, int current){
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

    private void successForeign(){
        Log.d("Success", "Foreign method to send message");
        successScore++;
        a.sendReceive.write("f".getBytes());
        if(successScore >= MOVE_ON_SUCCESSES){
            //move to next level
            t.cancel();
            a.resetSandF();
            editor.putInt("score", successScore*100);
            editor.commit();
            currentFragment = new FragmentLevel2A2P();//randomize?
            switchToNewScreen();
        }
    }

    private void successDomestic(){
        t.cancel();
        successScore++;
        if(successScore >= MOVE_ON_SUCCESSES){
            //move to next level
            a.resetSandF();
            editor.putInt("score", successScore*100);
            editor.commit();
            currentFragment = new FragmentLevel2A2P();//randomize?
            switchToNewScreen();
        }
        else{
            int index = (int) (Math.random()*NUMBER_OF_STRINGS);
            text.setText(strings[index]);
            a.sendReceive.write((index + "").getBytes());
            t.start();
        }
    }

    private void successSilent(){
        successScore++;
        if(successScore >= MOVE_ON_SUCCESSES){
            //move to next level
            t.cancel();
            a.resetSandF();
            editor.putInt("score", successScore*100);
            editor.commit();
            currentFragment = new FragmentLevel2A2P();//randomize?
            switchToNewScreen();
        }
    }
}