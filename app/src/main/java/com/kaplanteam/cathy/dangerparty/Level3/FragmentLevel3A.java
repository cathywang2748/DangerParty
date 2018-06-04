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

public class FragmentLevel3A extends Fragment implements View.OnClickListener {
    private Button moistyTequila, backtrack;
    private ImageView sapphire, ruby, diamond, emerald, amethyst, topaz, left_cave, right_cave, flashlight,
        bat1, bat2, bat3, bat4, bat5, sglow, rglow, dglow, eglow, aglow, tglow;
    private ConstraintLayout background;
    private boolean lightOn;

    private View timerView;
    private final int MILLIS_IN_FUTURE = 7000;
    private final int COUNT_DOWN_INTERVAL = 100;
    private final float SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
    private CountDownTimer t;

    private final int NUMBER_OF_STRINGS = 11;
    private String[] strings;
    private final int NUMBER_OF_STRINGS_DARK = 12;
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.level3a, container, false);

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
        strings[0] = "Snatch the sapphire";
        strings[1] = "Rub the ruby";
        strings[2] = "Abduct the diamond";
        strings[3] = "Extract the emerald";
        strings[4] = "Palpate the amethyst";
        strings[5] = "Tap the topaz";
        strings[6] = "Moisty Tequila";
        strings[7] = "Take the left fork of the cave path";
        strings[8] = "Take the right fork of the cave path";
        strings[9] = "Backtrack";
        strings[10] = "Turn off the light";

        stringsDark = new String[NUMBER_OF_STRINGS_DARK];
        stringsDark[0] = "Snatch the sapphire";
        stringsDark[1] = "Rub the ruby";
        stringsDark[2] = "Abduct the diamond";
        stringsDark[3] = "Extract the emerald";
        stringsDark[4] = "Palpate the amethyst";
        stringsDark[5] = "Tap the topaz";
        stringsDark[6] = "Moisty Tequila";
        stringsDark[7] = "Take the left fork of the cave path";
        stringsDark[8] = "Take the right fork of the cave path";
        stringsDark[9] = "Backtrack";
        stringsDark[10] = "Turn on the light";
        stringsDark[11] = "Put the bats to sleep";

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
        moistyTequila = rootView.findViewById(R.id.button_moisty_tequila);
        backtrack = rootView.findViewById(R.id.button_backtrack);
        sapphire = rootView.findViewById(R.id.imageView_sapphire);
        ruby = rootView.findViewById(R.id.imageView_ruby);
        diamond = rootView.findViewById(R.id.imageView_diamond);
        emerald = rootView.findViewById(R.id.imageView_emerald);
        amethyst = rootView.findViewById(R.id.imageView_amethyst);
        topaz = rootView.findViewById(R.id.imageView_topaz);
        left_cave = rootView.findViewById(R.id.imageView_left_cave);
        right_cave = rootView.findViewById(R.id.imageView_right_cave);
        flashlight = rootView.findViewById(R.id.imageView_flashlight);
        background = rootView.findViewById(R.id.level3a_background);
        bat1 = rootView.findViewById(R.id.imageView_bat1);
        bat2 = rootView.findViewById(R.id.imageView_bat2);
        bat3 = rootView.findViewById(R.id.imageView_bat3);
        bat4 = rootView.findViewById(R.id.imageView_bat4);
        bat5 = rootView.findViewById(R.id.imageView_bat5);
        sglow = rootView.findViewById(R.id.imageView_sapp_glow);
        rglow = rootView.findViewById(R.id.imageView_ruby_glow);
        dglow = rootView.findViewById(R.id.imageView_dia_glow);
        eglow = rootView.findViewById(R.id.imageView_emer_glow);
        tglow = rootView.findViewById(R.id.imageView_top_glow);
        aglow = rootView.findViewById(R.id.imageView_ame_glow);
        timerView = rootView.findViewById(R.id.timer);
        text = rootView.findViewById(R.id.textView);
        liveOne = rootView.findViewById(R.id.imageView_live_one);
        liveTwo = rootView.findViewById(R.id.imageView_live_two);
        liveThree = rootView.findViewById(R.id.imageView_live_three);
        liveFour = rootView.findViewById(R.id.imageView_live_four);
        liveFive = rootView.findViewById(R.id.imageView_live_five);
    }

    private void setListeners() {
        moistyTequila.setOnClickListener(this);
        backtrack.setOnClickListener(this);
        sapphire.setOnClickListener(this);
        ruby.setOnClickListener(this);
        diamond.setOnClickListener(this);
        emerald.setOnClickListener(this);
        amethyst.setOnClickListener(this);
        topaz.setOnClickListener(this);
        left_cave.setOnClickListener(this);
        right_cave.setOnClickListener(this);
        flashlight.setOnClickListener(this);
        bat1.setOnClickListener(this);
        bat2.setOnClickListener(this);
        bat3.setOnClickListener(this);
        bat4.setOnClickListener(this);
        bat5.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.imageView_flashlight:
                if(lightOn){
                    lightOn = false;
                    flashlight.setImageResource(R.drawable.light_off);
                    background.setBackgroundColor(Color.BLACK);
                    bat1.setVisibility(View.VISIBLE);
                    bat2.setVisibility(View.VISIBLE);
                    bat3.setVisibility(View.VISIBLE);
                    bat4.setVisibility(View.VISIBLE);
                    bat5.setVisibility(View.VISIBLE);
                    ruby.setImageResource(R.drawable.ruby_dark);
                    sapphire.setImageResource(R.drawable.sapp_dark);
                    emerald.setImageResource(R.drawable.emer_dark);
                    diamond.setImageResource(R.drawable.dia_dark);
                    topaz.setImageResource(R.drawable.top_dark);
                    amethyst.setImageResource(R.drawable.ame_dark);
                    sglow.setVisibility(View.VISIBLE);
                    rglow.setVisibility(View.VISIBLE);
                    eglow.setVisibility(View.VISIBLE);
                    dglow.setVisibility(View.VISIBLE);
                    tglow.setVisibility(View.VISIBLE);
                    aglow.setVisibility(View.VISIBLE);

                    if(text.getText().equals("Turn off the light")){
                        success();
                    }
                    else{
                        successScore--;
                    }
                }
                else{
                    lightOn = true;
                    flashlight.setImageResource(R.drawable.light_on);
                    background.setBackgroundColor(Color.WHITE);
                    bat1.setVisibility(View.INVISIBLE);
                    bat2.setVisibility(View.INVISIBLE);
                    bat3.setVisibility(View.INVISIBLE);
                    bat4.setVisibility(View.INVISIBLE);
                    bat5.setVisibility(View.INVISIBLE);
                    ruby.setImageResource(R.drawable.ruby);
                    sapphire.setImageResource(R.drawable.sapphire);
                    emerald.setImageResource(R.drawable.emerald);
                    diamond.setImageResource(R.drawable.diamond);
                    topaz.setImageResource(R.drawable.topaz);
                    amethyst.setImageResource(R.drawable.amethyst);
                    sglow.setVisibility(View.INVISIBLE);
                    rglow.setVisibility(View.INVISIBLE);
                    eglow.setVisibility(View.INVISIBLE);
                    dglow.setVisibility(View.INVISIBLE);
                    tglow.setVisibility(View.INVISIBLE);
                    aglow.setVisibility(View.INVISIBLE);

                    if(text.getText().equals("Turn on the light")){
                        success();
                    }
                    else{
                        successScore--;
                    }
                }
                break;
            case R.id.imageView_sapphire:
                if(text.getText().equals(strings[0])){
                    success();
                }
                else {
                   successScore--;
                }
                break;
            case R.id.imageView_ruby:
                if(text.getText().equals(strings[1])){
                    success();
                }
                else {
                    successScore--;
                }
                break;
            case R.id.imageView_diamond:
                if(text.getText().equals(strings[2])){
                    success();
                }
                else {
                    successScore--;
                }
                break;
            case R.id.imageView_emerald:
                if(text.getText().equals(strings[3])){
                    success();
                }
                else {
                    successScore--;
                }
                break;
            case R.id.imageView_amethyst:
                if(text.getText().equals(strings[4])){
                    success();
                }
                else {
                    successScore--;
                }
                break;
            case R.id.imageView_topaz:
                if(text.getText().equals(strings[5])){
                    success();
                }
                else {
                    successScore--;
                }
                break;
            case R.id.button_moisty_tequila:
                if(text.getText().equals(strings[6])){
                    success();
                }
                else {
                    successScore--;
                }
                break;
            case R.id.imageView_left_cave:
                if(text.getText().equals(strings[7])){
                    success();
                }
                else {
                    successScore--;
                }
                break;
            case R.id.imageView_right_cave:
                if(text.getText().equals(strings[8])){
                    success();
                }
                else {
                    successScore--;
                }
                break;
            case R.id.button_backtrack:
                if(text.getText().equals(strings[9])){
                    success();
                }
                else {
                    successScore--;
                }
                break;
            case R.id.imageView_bat1:
                if(text.getText().equals(stringsDark[11])
                        && bat2.getVisibility() == View.INVISIBLE
                        && bat3.getVisibility() == View.INVISIBLE
                        && bat4.getVisibility() == View.INVISIBLE
                        && bat5.getVisibility() == View.INVISIBLE){ // will need to change later for 2 people -----------------------------------------------
                    success();
                }
                else if (!text.getText().equals(stringsDark[11])){
                    successScore--;
                }
                bat1.setVisibility(View.INVISIBLE);
                CountDownTimer timers1 = new CountDownTimer(6000, 2000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        if(!lightOn){
                            bat1.setVisibility(View.VISIBLE);
                        }
                    }
                }.start();
                break;
            case R.id.imageView_bat2:
                if(text.getText().equals(stringsDark[11])
                        && bat1.getVisibility() == View.INVISIBLE
                        && bat3.getVisibility() == View.INVISIBLE
                        && bat4.getVisibility() == View.INVISIBLE
                        && bat5.getVisibility() == View.INVISIBLE){ // will need to change later for 2 people -----------------------------------------------
                    success();
                }
                else if (!text.getText().equals(stringsDark[11])){
                    successScore--;
                }
                bat2.setVisibility(View.INVISIBLE);
                CountDownTimer timers2 = new CountDownTimer(6000, 2000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        if(!lightOn){
                            bat2.setVisibility(View.VISIBLE);
                        }
                    }
                }.start();
                break;
            case R.id.imageView_bat3:
                if(text.getText().equals(stringsDark[11])
                        && bat2.getVisibility() == View.INVISIBLE
                        && bat1.getVisibility() == View.INVISIBLE
                        && bat4.getVisibility() == View.INVISIBLE
                        && bat5.getVisibility() == View.INVISIBLE){ // will need to change later for 2 people -----------------------------------------------
                    success();
                }
                else if (!text.getText().equals(stringsDark[11])){
                    successScore--;
                }
                bat3.setVisibility(View.INVISIBLE);
                CountDownTimer timers3 = new CountDownTimer(6000, 2000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        if(!lightOn){
                            bat3.setVisibility(View.VISIBLE);
                        }
                    }
                }.start();
                break;
            case R.id.imageView_bat4:
                if(text.getText().equals(stringsDark[11])
                        && bat2.getVisibility() == View.INVISIBLE
                        && bat3.getVisibility() == View.INVISIBLE
                        && bat1.getVisibility() == View.INVISIBLE
                        && bat5.getVisibility() == View.INVISIBLE){ // will need to change later for 2 people -----------------------------------------------
                    success();
                }
                else if (!text.getText().equals(stringsDark[11])){
                    successScore--;
                }
                bat4.setVisibility(View.INVISIBLE);
                CountDownTimer timers4 = new CountDownTimer(6000, 2000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        if(!lightOn){
                            bat4.setVisibility(View.VISIBLE);
                        }
                    }
                }.start();
                break;
            case R.id.imageView_bat5:
                if(text.getText().equals(stringsDark[11])
                        && bat2.getVisibility() == View.INVISIBLE
                        && bat3.getVisibility() == View.INVISIBLE
                        && bat4.getVisibility() == View.INVISIBLE
                        && bat1.getVisibility() == View.INVISIBLE){ // will need to change later for 2 people -----------------------------------------------
                    success();
                }
                else if (!text.getText().equals(stringsDark[11])){
                    successScore--;
                }
                bat5.setVisibility(View.INVISIBLE);
                CountDownTimer timers5 = new CountDownTimer(6000, 2000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        if(!lightOn){
                            bat5.setVisibility(View.VISIBLE);
                        }
                    }
                }.start();
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
            if(lightOn){
                text.setText(strings[(int)(Math.random()*NUMBER_OF_STRINGS)]);
                t.start();
            }
            else{
                text.setText(stringsDark[(int)(Math.random()*NUMBER_OF_STRINGS_DARK)]);
                t.start();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        t.cancel();
    }
}