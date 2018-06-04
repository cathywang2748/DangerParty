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

public class FragmentLevel3A2P extends Fragment implements View.OnClickListener {
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
        View rootView = inflater.inflate(R.layout.level3a, container, false);

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
                a.sendReceive.write("swap00".getBytes());
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
                    else if(a.commandForeign.equals("Turn off the light")){
                        successForeign();
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
                    else if(a.commandForeign.equals("Turn on the light")){
                        successForeign();
                    }
                    else{
                        successScore--;
                    }
                }
                break;
            case R.id.imageView_sapphire:
                if(text.getText().equals(strings[8])){
                    success();
                }
                else if(a.commandForeign.equals(strings[8])){
                    successForeign();
                }
                else {
                    successScore--;
                }
                break;
            case R.id.imageView_ruby:
                if(text.getText().equals(strings[9])){
                    success();
                }
                else if(a.commandForeign.equals(strings[9])){
                    successForeign();
                }
                else {
                    successScore--;
                }
                break;
            case R.id.imageView_diamond:
                if(text.getText().equals(strings[10])){
                    success();
                }
                else if(a.commandForeign.equals(strings[10])){
                    successForeign();
                }
                else {
                    successScore--;
                }
                break;
            case R.id.imageView_emerald:
                if(text.getText().equals(strings[11])){
                    success();
                }
                else if(a.commandForeign.equals(strings[11])){
                    successForeign();
                }
                else {
                    successScore--;
                }
                break;
            case R.id.imageView_amethyst:
                if(text.getText().equals(strings[12])){
                    success();
                }
                else if(a.commandForeign.equals(strings[12])){
                    successForeign();
                }
                else {
                    successScore--;
                }
                break;
            case R.id.imageView_topaz:
                if(text.getText().equals(strings[13])){
                    success();
                }
                else if(a.commandForeign.equals(strings[13])){
                    successForeign();
                }
                else {
                    successScore--;
                }
                break;
            case R.id.button_moisty_tequila:
                if(text.getText().equals(strings[14])){
                    success();
                }
                else if(a.commandForeign.equals(strings[14])){
                    successForeign();
                }
                else {
                    successScore--;
                }
                break;
            case R.id.imageView_left_cave:
                if(text.getText().equals(strings[15])){
                    success();
                }
                else if(a.commandForeign.equals(strings[15])){
                    successForeign();
                }
                else {
                    successScore--;
                }
                break;
            case R.id.imageView_right_cave:
                if(text.getText().equals(strings[16])){
                    success();
                }
                else if(a.commandForeign.equals(strings[16])){
                    successForeign();
                }
                else {
                    successScore--;
                }
                break;
            case R.id.button_backtrack:
                if(text.getText().equals(strings[17])){
                    success();
                }
                else if(a.commandForeign.equals(strings[17])){
                    successForeign();
                }
                else {
                    successScore--;
                }
                break;
            case R.id.imageView_bat1:
                if(text.getText().equals(stringsDark[19])
                        && bat2.getVisibility() == View.INVISIBLE
                        && bat3.getVisibility() == View.INVISIBLE
                        && bat4.getVisibility() == View.INVISIBLE
                        && bat5.getVisibility() == View.INVISIBLE){ // will need to change later for 2 people -----------------------------------------------
                    success();
                }
                else if (a.commandForeign.equals(stringsDark[19])
                        && bat2.getVisibility() == View.INVISIBLE
                        && bat3.getVisibility() == View.INVISIBLE
                        && bat4.getVisibility() == View.INVISIBLE
                        && bat5.getVisibility() == View.INVISIBLE){
                    successForeign();
                }
                else if (!text.getText().equals(stringsDark[19]) && !a.commandForeign.equals(stringsDark[19])){
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
                if(text.getText().equals(stringsDark[19])
                        && bat1.getVisibility() == View.INVISIBLE
                        && bat3.getVisibility() == View.INVISIBLE
                        && bat4.getVisibility() == View.INVISIBLE
                        && bat5.getVisibility() == View.INVISIBLE){ // will need to change later for 2 people -----------------------------------------------
                    success();
                }
                else if (a.commandForeign.equals(stringsDark[19])
                        && bat1.getVisibility() == View.INVISIBLE
                        && bat3.getVisibility() == View.INVISIBLE
                        && bat4.getVisibility() == View.INVISIBLE
                        && bat5.getVisibility() == View.INVISIBLE){
                    successForeign();
                }
                else if (!text.getText().equals(stringsDark[19]) && !a.commandForeign.equals(stringsDark[19])){
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
                if(text.getText().equals(stringsDark[19])
                        && bat2.getVisibility() == View.INVISIBLE
                        && bat1.getVisibility() == View.INVISIBLE
                        && bat4.getVisibility() == View.INVISIBLE
                        && bat5.getVisibility() == View.INVISIBLE){ // will need to change later for 2 people -----------------------------------------------
                    success();
                }
                else if (a.commandForeign.equals(stringsDark[19])
                        && bat2.getVisibility() == View.INVISIBLE
                        && bat1.getVisibility() == View.INVISIBLE
                        && bat4.getVisibility() == View.INVISIBLE
                        && bat5.getVisibility() == View.INVISIBLE){
                    successForeign();
                }
                else if (!text.getText().equals(stringsDark[19]) && !a.commandForeign.equals(stringsDark[19])){
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
                if(text.getText().equals(stringsDark[19])
                        && bat2.getVisibility() == View.INVISIBLE
                        && bat3.getVisibility() == View.INVISIBLE
                        && bat1.getVisibility() == View.INVISIBLE
                        && bat5.getVisibility() == View.INVISIBLE){ // will need to change later for 2 people -----------------------------------------------
                    success();
                }
                else if (a.commandForeign.equals(stringsDark[19])
                        && bat2.getVisibility() == View.INVISIBLE
                        && bat3.getVisibility() == View.INVISIBLE
                        && bat1.getVisibility() == View.INVISIBLE
                        && bat5.getVisibility() == View.INVISIBLE){
                    successForeign();
                }
                else if (!text.getText().equals(stringsDark[19]) && !a.commandForeign.equals(stringsDark[19])){
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
                if(text.getText().equals(stringsDark[19])
                        && bat2.getVisibility() == View.INVISIBLE
                        && bat3.getVisibility() == View.INVISIBLE
                        && bat4.getVisibility() == View.INVISIBLE
                        && bat1.getVisibility() == View.INVISIBLE){ // will need to change later for 2 people -----------------------------------------------
                    success();
                }
                else if (a.commandForeign.equals(stringsDark[19])
                        && bat2.getVisibility() == View.INVISIBLE
                        && bat3.getVisibility() == View.INVISIBLE
                        && bat4.getVisibility() == View.INVISIBLE
                        && bat1.getVisibility() == View.INVISIBLE){
                    successForeign();
                }
                else if (!text.getText().equals(stringsDark[19]) && !a.commandForeign.equals(stringsDark[19])){
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