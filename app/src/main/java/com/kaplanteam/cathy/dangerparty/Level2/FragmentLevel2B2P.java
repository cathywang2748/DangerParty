package com.kaplanteam.cathy.dangerparty.Level2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaplanteam.cathy.dangerparty.BluetoothActivity;
import com.kaplanteam.cathy.dangerparty.EndGameActivity;
import com.kaplanteam.cathy.dangerparty.R;

/**
 * Created by Cole on 5/23/18.
 */

public class FragmentLevel2B2P extends Fragment implements View.OnClickListener {
    private Button climb, pick, caress, burn, flick, mf1, mf2, mf3, mf4;
    private ImageView bottle, lips;
    private boolean full;

    private View timerView;
    private final int MILLIS_IN_FUTURE = 7000;
    private final int COUNT_DOWN_INTERVAL = 100;
    private final float SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
    private CountDownTimer t;

    private final int NUMBER_OF_STRINGS = 11;
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

    private BluetoothActivity a;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.level2b, container, false);

        full = false;
        wireWidgets(rootView);
        setListeners();

        a = (BluetoothActivity) getActivity();

        counter = this.getActivity().getSharedPreferences("HELLO", Context.MODE_PRIVATE);
        editor = counter.edit();

        img = new ImageView[5];
        img[0] = liveFive;
        img[1] = liveFour;
        img[2] = liveThree;
        img[3] = liveTwo;
        img[4] = liveOne;


        strings = new String[NUMBER_OF_STRINGS];
        //layoutA
        strings[0] = "Squishishi";
        strings[1] = "Voop";
        strings[2] = "Bo";
        strings[3] = "Get lost";
        strings[4] = "Zzyzx";
        strings[5] = "Rub the Frustumsphere";
        strings[6] = "Hug the Frustumsphere";
        strings[7] = "Northeast";
        strings[8] = "East";
        strings[9] = "Southeast";
        strings[10] = "South";
        strings[11] = "Southwest";
        strings[12] = "West";
        strings[13] = "Northwest";
        //layoutB
        strings[14] = "Climb the tree";
        strings[15] = "Pick the dancing girl flowers";
        strings[16] = "Caress the dancing girl flowers";
        strings[17] = "Burn the dancing girl flowers";
        strings[18] = "Flick the dancing girl flowers";
        strings[19] = "Count 1 monkey face orchid";
        strings[20] = "Count 2 monkey face orchid";
        strings[21] = "Count 3 monkey face orchid";
        strings[22] = "Count 4 monkey face orchid";
        strings[23] = "Fill the water bottle with unicorn juice";
        strings[24] = "Kiss the hooker's lips flower";

        currentStrings = new String[5];
        //layoutA
        currentStrings[0] = "Nuuvut";
        currentStrings[1] = "North";
        currentStrings[2] = "Don't get lost";
        currentStrings[3] = "Unzzyzx";
        //layoutB
        currentStrings[4] = "Empty the water bottle of unicorn juice";

        text.setText("Level 2: Mystic Party Forest");// could make ready set go or other animation type thing

        //get any other initial set up done
        t = new CountDownTimer(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long l) {
                timerView.setX(l / (float) MILLIS_IN_FUTURE * SCREEN_WIDTH - SCREEN_WIDTH);
                if(a.failures > failScore){ //failure
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
                        text.setText(strings[(int)(Math.random()*NUMBER_OF_STRINGS)]);
                        a.sendReceive.write(text.getText().toString().getBytes());
                        t.start();

                    }
                }
                if(a.successes > successScore){ //success
                    if(!a.domesticSuccess){
                        successDomestic();
                    }
                    else{
                        successSilent();
                    }
                }
                if(a.swapReady){//swap
                    swapFromForeign(a.swapString, a.swapCurrent);
                    a.swapNotReady();
                }
            }

            @Override
            public void onFinish() {
                if(firstTime){
                    text.setText(strings[(int)(Math.random()*NUMBER_OF_STRINGS)]);
                    a.sendReceive.write(text.getText().toString().getBytes());
                    t.start();
                    firstTime = false;
                }
                else{
                    timerView.setX(0 - SCREEN_WIDTH);
                    //closer to death for both screens --------------------------------------------------------------------
                    failScore++;
                    a.sendReceive.write("fail".toString().getBytes());
                    if(failScore >= END_GAME_FAILURES){
                        //End Game
                        editor.putInt("score", successScore*100);
                        editor.commit();
                        Intent i = new Intent(getActivity(), EndGameActivity.class);
                        startActivity(i);
                    }
                    else{
                        img[END_GAME_FAILURES - failScore].setVisibility(View.INVISIBLE);
                        text.setText(strings[(int)(Math.random()*NUMBER_OF_STRINGS)]);
                        a.sendReceive.write(text.getText().toString().getBytes());
                        t.start();

                    }
                }
            }
        }.start();

        return rootView;
    }

    private void wireWidgets(View rootView) {
        climb = rootView.findViewById(R.id.button1);
        pick = rootView.findViewById(R.id.button2A);
        caress = rootView.findViewById(R.id.button2B);
        burn = rootView.findViewById(R.id.button2C);
        flick = rootView.findViewById(R.id.button2D);
        mf1 = rootView.findViewById(R.id.button3A);
        mf2 = rootView.findViewById(R.id.button3B);
        mf3 = rootView.findViewById(R.id.button3C);
        mf4 = rootView.findViewById(R.id.button3D);
        bottle = rootView.findViewById(R.id.imageView_bottle);
        lips = rootView.findViewById(R.id.imageView_lips);
        timerView = rootView.findViewById(R.id.timer);
        text = rootView.findViewById(R.id.textView);
        liveOne = rootView.findViewById(R.id.imageView_live_one);
        liveTwo = rootView.findViewById(R.id.imageView_live_two);
        liveThree = rootView.findViewById(R.id.imageView_live_three);
        liveFour = rootView.findViewById(R.id.imageView_live_four);
        liveFive = rootView.findViewById(R.id.imageView_live_five);
    }

    private void setListeners() {
        climb.setOnClickListener(this);
        pick.setOnClickListener(this);
        caress.setOnClickListener(this);
        burn.setOnClickListener(this);
        flick.setOnClickListener(this);
        mf1.setOnClickListener(this);
        mf2.setOnClickListener(this);
        mf3.setOnClickListener(this);
        mf4.setOnClickListener(this);
        bottle.setOnClickListener(this);
        lips.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button1:
                if(text.getText().equals(strings[14])){
                    success();
                }
                else if(a.commandForeign.equals(strings[14])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button2A:
                if(text.getText().equals(strings[15])){
                    success();
                }
                else if(a.commandForeign.equals(strings[15])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button2B:
                if(text.getText().equals(strings[16])){
                    success();
                }
                else if(a.commandForeign.equals(strings[16])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button2C:
                if(text.getText().equals(strings[17])){
                    success();
                }
                else if(a.commandForeign.equals(strings[17])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button2D:
                if(text.getText().equals(strings[18])){
                    success();
                }
                else if(a.commandForeign.equals(strings[18])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button3A:
                if(text.getText().equals(strings[19])){
                    success();
                }
                else if(a.commandForeign.equals(strings[19])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button3B:
                if(text.getText().equals(strings[20])){
                    success();
                }
                else if(a.commandForeign.equals(strings[20])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button3C:
                if(text.getText().equals(strings[21])){
                    success();
                }
                else if(a.commandForeign.equals(strings[21])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button3D:
                if(text.getText().equals(strings[22])){
                    success();
                }
                else if(a.commandForeign.equals(strings[22])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.imageView_bottle:
                if(full){
                    if(text.getText().equals("Empty the water bottle of unicorn juice")){
                        success(23,4);
                    }
                    else if(a.commandForeign.equals("Empty the water bottle of unicorn juice")){
                        successForeign();
                        swap(23, 4);
                    }
                    else{
                        successScore--;
                        swap(23, 4);
                    }
                    view.setBackgroundResource(R.drawable.bottle_empty);
                }
                else{
                    if(text.getText().equals("Fill the water bottle with unicorn juice")){
                        success(23, 4);
                    }
                    else if(a.commandForeign.equals("Fill the water bottle with unicorn juice")){
                        successForeign();
                        swap(23, 4);
                    }
                    else{
                        successScore--;
                        swap(23, 4);
                    }
                    view.setBackgroundResource(R.drawable.bottle_full);
                }
                full = !full;
                break;
            case R.id.imageView_lips:
                if(text.getText().equals(strings[24])){
                    success();
                }
                else if(a.commandForeign.equals(strings[24])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
        }
    }

    private void success(){
        t.cancel();
        successScore++;
        a.sendReceive.write("domestic success".getBytes());
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
            text.setText(strings[(int)(Math.random()*NUMBER_OF_STRINGS)]);
            a.sendReceive.write(text.getText().toString().getBytes());
            t.start();
        }
    }

    private void success(int string, int current){
        t.cancel();
        successScore++;
        a.sendReceive.write("domestic success".getBytes());
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
            text.setText(strings[(int)(Math.random()*NUMBER_OF_STRINGS)]);
            a.sendReceive.write(text.getText().toString().getBytes());
            t.start();
        }
    }

    private void swap(int string, int current){
        a.sendReceive.write(("swap" + string + current).getBytes());
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
        successScore++;
        a.sendReceive.write("foreign success".getBytes());
        if(successScore >= MOVE_ON_SUCCESSES){
            //move to next level
            a.resetSandF();
            editor.putInt("score", successScore*100);
            editor.commit();
            currentFragment = new FragmentLevel2A2P();//randomize?
            switchToNewScreen();
        }
    }

    private void successDomestic(){
        successScore++;
        if(successScore >= MOVE_ON_SUCCESSES){
            //move to next level
            a.resetSandF();
            editor.putInt("score", successScore*100);
            editor.commit();
            currentFragment = new FragmentLevel2A2P();//randomize?
            switchToNewScreen();
        }
    }

    private void successSilent(){
        successScore++;
        if(successScore >= MOVE_ON_SUCCESSES){
            //move to next level
            a.resetSandF();
            editor.putInt("score", successScore*100);
            editor.commit();
            currentFragment = new FragmentLevel2A2P();//randomize?
            switchToNewScreen();
        }
    }
}