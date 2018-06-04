package com.kaplanteam.cathy.dangerparty.Level3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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

public class FragmentLevel3B2P extends Fragment implements View.OnClickListener {
    private Button woo, bum, banana, boom;
    private ImageView lFork, rFork, dynamite, spark, explosion, caveWall;
    private boolean lightOn;
    private ConstraintLayout background;
    private TextView scream;

    private View timerView;
    private final int MILLIS_IN_FUTURE = 7000;
    private final int COUNT_DOWN_INTERVAL = 100;
    private final float SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
    private CountDownTimer t;

    private final int NUMBER_OF_STRINGS = 19;
    private String[] strings;
    private final int NUMBER_OF_STRINGS_DARK = 20;
    private String[] stringsDark;
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
        View rootView = inflater.inflate(R.layout.level3b, container, false);

        counter = getActivity().getSharedPreferences("HELLO", Context.MODE_PRIVATE);
        editor = counter.edit();

        a = (BluetoothActivity) getActivity();
        //wire any widgets -- must use rootView.findViewById

        lightOn = true;
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
        //layoutB strings
        strings[0] = "Holler woolloomooloo";
        strings[1] = "Humm bumbadumbdum";
        strings[2] = "Sing banamanamum";
        strings[3] = "Screetch boomsicklepop";
        strings[4] = "Interpret the cave drawings";
        strings[5] = "Light the dynamite";
        strings[6] = "Lick the left fork";
        strings[7] = "Bite the right fork";
        //layoutA strings
        strings[8] = "Snatch the sapphire";
        strings[9] = "Rub the ruby";
        strings[10] = "Abduct the diamond";
        strings[11] = "Extract the emerald";
        strings[12] = "Palpate the amethyst";
        strings[13] = "Tap the topaz";
        strings[14] = "Moisty Tequila";
        strings[15] = "Take the left fork of the cave path";
        strings[16] = "Take the right fork of the cave path";
        strings[17] = "Backtrack";
        strings[18] = "Turn off the light";

        stringsDark = new String[NUMBER_OF_STRINGS_DARK];
        //layoutB strings
        stringsDark[0] = "Holler woolloomooloo";
        stringsDark[1] = "Humm bumbadumbdum";
        stringsDark[2] = "Sing banamanamum";
        stringsDark[3] = "Screetch boomsicklepop";
        stringsDark[4] = "Eat some glow worms";
        stringsDark[5] = "Light the dynamite";
        stringsDark[6] = "Lick the left fork";
        stringsDark[7] = "Bite the right fork";
        //layoutA strings
        stringsDark[8] = "Snatch the sapphire";
        stringsDark[9] = "Rub the ruby";
        stringsDark[10] = "Abduct the diamond";
        stringsDark[11] = "Extract the emerald";
        stringsDark[12] = "Palpate the amethyst";
        stringsDark[13] = "Tap the topaz";
        stringsDark[14] = "Moisty Tequila";
        stringsDark[15] = "Take the left fork of the cave path";
        stringsDark[16] = "Take the right fork of the cave path";
        stringsDark[17] = "Backtrack";
        stringsDark[18] = "Turn on the light";
        stringsDark[19] = "Put the bats to sleep";

        text.setText("Level 3: Cave of Nightmares");// could make ready set go or other animation type thing

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
                    lightChange();
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

        //return the view that we inflated
        return rootView;
    }

    private void wireWidgets(View rootView) {
        woo = rootView.findViewById(R.id.button_woo);
        bum = rootView.findViewById(R.id.button_bum);
        banana = rootView.findViewById(R.id.button_banana);
        boom = rootView.findViewById(R.id.button_boom);
        lFork = rootView.findViewById(R.id.imageView_fork_left);
        rFork = rootView.findViewById(R.id.imageView_fork_right);
        dynamite = rootView.findViewById(R.id.imageView_dynamite);
        spark = rootView.findViewById(R.id.imageView_spark);
        explosion = rootView.findViewById(R.id.imageView_explosion);
        caveWall = rootView.findViewById(R.id.imageView_cave_drawings);
        background = rootView.findViewById(R.id.level3b_background);
        scream = rootView.findViewById(R.id.textView_scream);
        timerView = rootView.findViewById(R.id.timer);
        text = rootView.findViewById(R.id.textView);
        liveOne = rootView.findViewById(R.id.imageView_live_one);
        liveTwo = rootView.findViewById(R.id.imageView_live_two);
        liveThree = rootView.findViewById(R.id.imageView_live_three);
        liveFour = rootView.findViewById(R.id.imageView_live_four);
        liveFive = rootView.findViewById(R.id.imageView_live_five);
    }

    private void setListeners() {
        woo.setOnClickListener(this);
        bum.setOnClickListener(this);
        banana.setOnClickListener(this);
        boom.setOnClickListener(this);
        lFork.setOnClickListener(this);
        rFork.setOnClickListener(this);
        dynamite.setOnClickListener(this);
        caveWall.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_woo:
                if(text.getText().equals(strings[0])){
                    success();
                }
                else if(a.commandForeign.equals(strings[0])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button_banana:
                if(text.getText().equals(strings[2])){
                    success();
                }
                else if(a.commandForeign.equals(strings[2])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button_bum:
                if(text.getText().equals(strings[1])){
                    success();
                }
                else if(a.commandForeign.equals(strings[1])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button_boom:
                if(text.getText().equals(strings[3])){
                    success();
                }
                else if(a.commandForeign.equals(strings[3])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.imageView_dynamite:
                spark.setVisibility(View.VISIBLE);
                if(text.getText().equals(strings[5])){
                    success();
                }
                else if(a.commandForeign.equals(strings[5])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                CountDownTimer timers = new CountDownTimer(6000, 1000) {
                    @Override
                    public void onTick(long l) {
                        if(l > 3500) {
                        }
                        else if(l > 2500)
                        {
                            explosion.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            spark.setVisibility(View.INVISIBLE);
                            explosion.setVisibility(View.INVISIBLE);
                            dynamite.setVisibility(View.INVISIBLE);
                        }

                    }

                    @Override
                    public void onFinish() {
                        dynamite.setVisibility(View.VISIBLE);
                    }
                }.start();
                break;
            case R.id.imageView_cave_drawings:
                if(lightOn && text.getText().equals(strings[4]) || !lightOn && text.getText().equals(stringsDark[4])){
                    success();
                }
                else if(lightOn && a.commandForeign.equals(strings[4]) || !lightOn && a.commandForeign.equals(stringsDark[4])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.imageView_fork_left:
                if(text.getText().equals(strings[6])){
                    success();
                }
                else if(a.commandForeign.equals(strings[6])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.imageView_fork_right:
                if(text.getText().equals(strings[7])){
                    success();
                }
                else if(a.commandForeign.equals(strings[7])){
                    successForeign();
                }
                else{
                    successScore--;
                }
                break;
        }
    }

    private void lightChange(){
        if(lightOn){
            caveWall.setImageResource(R.drawable.cave_drawings);
            background.setBackgroundColor(Color.WHITE);
            scream.setTextColor(Color.BLACK);
        }
        else{
            caveWall.setImageResource(R.drawable.glow_worms);
            background.setBackgroundColor(Color.BLACK);
            scream.setTextColor(Color.WHITE);
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
            Intent i = new Intent(getActivity(), EndGameActivity.class);
            startActivity(i);

        }
        else{
            //send message-----------------------------------------------------------------------------------------
            text.setText(strings[(int)(Math.random()*NUMBER_OF_STRINGS)]);
            a.sendReceive.write(text.getText().toString().getBytes());
            t.start();
        }
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
            Intent i = new Intent(getActivity(), EndGameActivity.class);
            startActivity(i);
        }
    }

    private void successDomestic(){
        successScore++;
        if(successScore >= MOVE_ON_SUCCESSES){
            //move to next level
            a.resetSandF();
            editor.putInt("score", successScore*100);
            editor.commit();
            Intent i = new Intent(getActivity(), EndGameActivity.class);
            startActivity(i);
        }
    }

    private void successSilent(){
        successScore++;
        if(successScore >= MOVE_ON_SUCCESSES){
            //move to next level
            a.resetSandF();
            editor.putInt("score", successScore*100);
            editor.commit();
            Intent i = new Intent(getActivity(), EndGameActivity.class);
            startActivity(i);
        }
    }
}