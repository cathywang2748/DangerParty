package com.kaplanteam.cathy.dangerparty.Level3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaplanteam.cathy.dangerparty.EndGameActivity;
import com.kaplanteam.cathy.dangerparty.R;

/**
 * Created by Cole on 4/1/18.
 */

public class FragmentLevel3B extends Fragment implements View.OnClickListener {
    private Button woo, bum, banana, boom;
    private ImageView lFork, rFork, dynamite, spark, explosion, caveWall;
    private boolean lightOn;
    private ConstraintLayout background;
    private TextView scream;

    //
    private View timerView;
    private final int MILLIS_IN_FUTURE = 7000;
    private final int COUNT_DOWN_INTERVAL = 100;
    private final float SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
    private CountDownTimer t;

    private final int NUMBER_OF_STRINGS = 8;
    private String[] strings;
    private final int NUMBER_OF_STRINGS_DARK = 8;
    private TextView text;

    private int successScore;
    private int failScore;
    private final int MOVE_ON_SUCCESSES = 10;
    private final int END_GAME_FAILURES = 5;
    private ImageView liveOne, liveTwo, liveThree, liveFour, liveFive;
    private ImageView[] img;

    private boolean firstTime;

    private SharedPreferences counter;
    private SharedPreferences.Editor editor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.level3b, container, false);

        counter = getActivity().getSharedPreferences("HELLO", Context.MODE_PRIVATE);
        editor = counter.edit();

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
        strings[0] = "Holler woolloomooloo";
        strings[1] = "Humm bumbadumbdum";
        strings[2] = "Sing banamanamum";
        strings[3] = "Screetch boomsicklepop";
        strings[4] = "Interpret the cave drawings";
        strings[5] = "Light the dynamite";
        strings[6] = "Lick the left fork";
        strings[7] = "Bite the right fork";

        text.setText("Level 3: Cave of Nightmares");// could make ready set go or other animation type thing

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
                else{
                    successScore--;
                }
                break;
            case R.id.button_banana:
                if(text.getText().equals(strings[2])){
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button_bum:
                if(text.getText().equals(strings[1])){
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button_boom:
                if(text.getText().equals(strings[3])){
                    success();
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
                if(text.getText().equals(strings[4])){
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.imageView_fork_left:
                if(text.getText().equals(strings[6])){
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.imageView_fork_right:
                if(text.getText().equals(strings[7])){
                    success();
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
        if(successScore >= MOVE_ON_SUCCESSES){
            //move to next level
            editor.putInt("score", successScore*100);
            editor.commit();
            Intent i = new Intent(getActivity(), EndGameActivity.class);
            startActivity(i);
        }
        else{
            text.setText(strings[(int)(Math.random()*NUMBER_OF_STRINGS)]);
            t.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        t.cancel();
    }
}