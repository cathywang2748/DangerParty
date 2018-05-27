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
import android.widget.Toast;

import com.kaplanteam.cathy.dangerparty.EndGameActivity;
import com.kaplanteam.cathy.dangerparty.Level3.FragmentLevel3B;
import com.kaplanteam.cathy.dangerparty.R;

/**
 * Created by Cole on 3/24/18.
 */

public class FragmentLevel2B extends Fragment implements View.OnClickListener {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.level2b, container, false);

        full = false;
        wireWidgets(rootView);
        setListeners();

        counter = this.getActivity().getSharedPreferences("HELLO", Context.MODE_PRIVATE);
        editor = counter.edit();

        img = new ImageView[5];
        img[0] = liveFive;
        img[1] = liveFour;
        img[2] = liveThree;
        img[3] = liveTwo;
        img[4] = liveOne;


        strings = new String[NUMBER_OF_STRINGS];
        strings[0] = "Climb the tree";
        strings[1] = "Pick the dancing girl flowers";
        strings[2] = "Caress the dancing girl flowers";
        strings[3] = "Burn the dancing girl flowers";
        strings[4] = "Flick the dancing girl flowers";
        strings[5] = "Count 1 monkey face orchid";
        strings[6] = "Count 2 monkey face orchid";
        strings[7] = "Count 3 monkey face orchid";
        strings[8] = "Count 4 monkey face orchid";
        strings[9] = "Fill the water bottle with unicorn juice";
        strings[10] = "Kiss the hooker's lips flower";

        currentStrings = new String[1];
        currentStrings[0] = "Empty the water bottle of unicorn juice";

        text.setText("Level 2: Mystic Party Forest");// could make ready set go or other animation type thing

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
                        Intent i = new Intent(getActivity(), EndGameActivity.class);
                        startActivity(i);//End Game
                        Toast.makeText(getContext(), "Game Over", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        img[END_GAME_FAILURES - failScore].setVisibility(View.INVISIBLE);
                        text.setText(strings[(int)(Math.random()*NUMBER_OF_STRINGS)]);
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
                if(text.getText().equals(strings[0])){
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button2A:
                if(text.getText().equals(strings[1])){
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button2B:
                if(text.getText().equals(strings[2])){
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button2C:
                if(text.getText().equals(strings[3])){
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button2D:
                if(text.getText().equals(strings[4])){
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button3A:
                if(text.getText().equals(strings[5])){
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button3B:
                if(text.getText().equals(strings[6])){
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button3C:
                if(text.getText().equals(strings[7])){
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button3D:
                if(text.getText().equals(strings[8])){
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.imageView_bottle:
                if(full){
                    if(text.getText().equals("Empty the water bottle of unicorn juice")){
                        success(9,0);
                    }
                    else{
                        successScore--;
                        swap(9, 0);
                    }
                    view.setBackgroundResource(R.drawable.bottle_empty);
                }
                else{
                    if(text.getText().equals("Fill the water bottle with unicorn juice")){
                        success(9,0);
                    }
                    else{
                        successScore--;
                        swap(9, 0);
                    }
                    view.setBackgroundResource(R.drawable.bottle_full);
                }
                full = !full;
                break;
            case R.id.imageView_lips:
                if(text.getText().equals(strings[10])){
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
            Toast.makeText(getContext(), "Move to Next Level", Toast.LENGTH_SHORT).show();
            currentFragment = new FragmentLevel3B(); //randomize?
            switchToNewScreen();
            //editor.putInt("score", successScore*100);
            //editor.commit();
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
            currentFragment = new FragmentLevel3B(); //randomize?
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
