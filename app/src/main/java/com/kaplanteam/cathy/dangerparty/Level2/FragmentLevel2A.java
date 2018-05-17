package com.kaplanteam.cathy.dangerparty.Level2;

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
import android.widget.Toast;

import com.kaplanteam.cathy.dangerparty.EndGameActivity;
import com.kaplanteam.cathy.dangerparty.Level3.FragmentLevel3A;
import com.kaplanteam.cathy.dangerparty.R;

import java.util.ArrayList;

/**
 * Created by Cole on 3/24/18.
 */

public class FragmentLevel2A extends Fragment implements View.OnTouchListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private double angle;
    private Switch getLostSwitch, zzyxzSwitch;
    private Button rub, hug;

    private View timerView;
    private final int MILLIS_IN_FUTURE = 7000;
    private final int COUNT_DOWN_INTERVAL = 100;
    private final float SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
    private CountDownTimer t;

    private final int NUMBER_OF_STRINGS = 14;
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
        View rootView = inflater.inflate(R.layout.level2a, container, false);

        counter = this.getActivity().getSharedPreferences("HELLO", Context.MODE_PRIVATE);
        editor = counter.edit();

        //wire any widgets -- must use rootView.findViewById
        angle = 0;
        final ImageView needle = rootView.findViewById(R.id.imageView_compass_needle);
        final ImageView xMark = rootView.findViewById(R.id.imageView_x);
        needle.setOnTouchListener(this);
        xMark.setOnTouchListener(this);

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


        currentStrings = new String[4];
        currentStrings[0] = "Nuuvut";
        currentStrings[1] = "North";
        currentStrings[2] = "Don't get lost";
        currentStrings[3] = "Unzzyzx";


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
                        Intent i = new Intent(getActivity(), EndGameActivity.class);
                        startActivity(i);
                        //End Game

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
        rub = rootView.findViewById(R.id.button_rub);
        hug = rootView.findViewById(R.id.button_hug);
        getLostSwitch = rootView.findViewById(R.id.switch1);
        zzyxzSwitch = rootView.findViewById(R.id.switch2);
        timerView = rootView.findViewById(R.id.timer);
        text = rootView.findViewById(R.id.textView);
        liveOne = rootView.findViewById(R.id.imageView_live_one);
        liveTwo = rootView.findViewById(R.id.imageView_live_two);
        liveThree =rootView.findViewById(R.id.imageView_live_three);
        liveFour = rootView.findViewById(R.id.imageView_live_four);
        liveFive = rootView.findViewById(R.id.imageView_live_five);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
        rub.setOnClickListener(this);
        hug.setOnClickListener(this);
        getLostSwitch.setOnCheckedChangeListener(this);
        zzyxzSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button_rub:
                if(text.getText().equals(strings[5])){
                    success();
                }
                else{
                    successScore--;
                }
                break;
            case R.id.button_hug:
                if(text.getText().equals(strings[6])){
                    success();
                }
                else{
                    successScore--;
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = MotionEventCompat.getActionMasked(motionEvent);

        if(view.getId() == R.id.imageView_compass_needle){
            switch(action) {
                case (MotionEvent.ACTION_DOWN) :
                    return true;
                case (MotionEvent.ACTION_MOVE) :
                    float ivposx =  (view.getBottom() - view.getTop()) /2 - 75;
                    float ivposy = (view.getRight() - view.getLeft()) /2 + 80;
                    float me = motionEvent.getY();
                    float x = motionEvent.getX() - ivposx;
                    float y = ivposy - motionEvent.getY();
                    if(x!=0){
                        double theta = Math.atan(y/x);
                        theta = theta * 180/Math.PI;
                        if(x < 0){theta -= 180;}
                        theta = 90 - theta;
                        Log.d("Theta", "" + theta);
                        angle = theta + angle;
                        view.setRotation((float)angle);
                    }
                    return true;
                case (MotionEvent.ACTION_UP) :
                    angle = Math.round(angle/45)*45;
                    view.setRotation((float)angle);
                    int pointerPosition = ((int)angle)/45%8;
                    if(pointerPosition == 0){
                        if(text.getText().equals("North")){
                            int pos = 0;
                            for(int i = 0;i< strings.length; i++){
                                if(strings[i].equals("North")){
                                    pos = i;
                                }
                            }
                            success(pos, 1);
                        }
                        else{
                            successScore--;
                            int pos = 0;
                            for(int i = 0;i< strings.length; i++){
                                if(strings[i].equals("North")){
                                    pos = i;
                                }
                            }
                            swap(pos, 1);
                        }
                    }
                    else if(pointerPosition == 1){
                        if(text.getText().equals("Northeast")){
                            int pos = 0;
                            for(int i = 0;i< strings.length; i++){
                                if(strings[i].equals("Northeast")){
                                    pos = i;
                                }
                            }
                            success(pos, 1);
                        }
                        else{
                            successScore--;
                            int pos = 0;
                            for(int i = 0;i< strings.length; i++){
                                if(strings[i].equals("Northeast")){
                                    pos = i;
                                }
                            }
                            swap(pos, 1);
                        }
                    }
                    else if(pointerPosition == 2){
                        if(text.getText().equals("East")){
                            int pos = 0;
                            for(int i = 0;i< strings.length; i++){
                                if(strings[i].equals("East")){
                                    pos = i;
                                }
                            }
                            success(pos, 1);
                        }
                        else{
                            successScore--;
                            int pos = 0;
                            for(int i = 0;i< strings.length; i++){
                                if(strings[i].equals("East")){
                                    pos = i;
                                }
                            }
                            swap(pos, 1);
                        }
                    }
                    else if(pointerPosition == 3){
                        if(text.getText().equals("Southeast")){
                            int pos = 0;
                            for(int i = 0;i< strings.length; i++){
                                if(strings[i].equals("Southeast")){
                                    pos = i;
                                }
                            }
                            success(pos, 1);
                        }
                        else{
                            successScore--;
                            int pos = 0;
                            for(int i = 0;i< strings.length; i++){
                                if(strings[i].equals("Southeast")){
                                    pos = i;
                                }
                            }
                            swap(pos, 1);
                        }
                    }
                    else if(pointerPosition == 4){
                        if(text.getText().equals("South")){
                            int pos = 0;
                            for(int i = 0;i< strings.length; i++){
                                if(strings[i].equals("South")){
                                    pos = i;
                                }
                            }
                            success(pos, 1);
                        }
                        else{
                            successScore--;
                            int pos = 0;
                            for(int i = 0;i< strings.length; i++){
                                if(strings[i].equals("South")){
                                    pos = i;
                                }
                            }
                            swap(pos, 1);
                        }
                    }else if(pointerPosition == 5){
                        if(text.getText().equals("Southwest")){
                            int pos = 0;
                            for(int i = 0;i< strings.length; i++){
                                if(strings[i].equals("Southwest")){
                                    pos = i;
                                }
                            }
                            success(pos, 1);
                        }
                        else{
                            successScore--;
                            int pos = 0;
                            for(int i = 0;i< strings.length; i++){
                                if(strings[i].equals("Southwest")){
                                    pos = i;
                                }
                            }
                            swap(pos, 1);
                        }

                    }
                    else if(pointerPosition == 6){
                        if(text.getText().equals("West")){
                            int pos = 0;
                            for(int i = 0;i< strings.length; i++){
                                if(strings[i].equals("West")){
                                    pos = i;
                                }
                            }
                            success(pos, 1);
                        }
                        else{
                            successScore--;
                            int pos = 0;
                            for(int i = 0;i< strings.length; i++){
                                if(strings[i].equals("West")){
                                    pos = i;
                                }
                            }
                            swap(pos, 1);
                        }
                    }
                    else if(pointerPosition == 7){
                        if(text.getText().equals("Northwest")){
                            int pos = 0;
                            for(int i = 0;i< strings.length; i++){
                                if(strings[i].equals("Northwest")){
                                    pos = i;
                                }
                            }
                            success(pos, 1);
                        }
                        else{
                            successScore--;
                            int pos = 0;
                            for(int i = 0;i< strings.length; i++){
                                if(strings[i].equals("Northwest")){
                                    pos = i;
                                }
                            }
                            swap(pos, 1);
                        }
                    }

                    return true;
                default :
                    return false;
            }
        }
        else if (view.getId() == R.id.imageView_x){
            float x, y, currentX, currentY;
            switch(action) {
                case (MotionEvent.ACTION_DOWN):
                    return true;
                case (MotionEvent.ACTION_MOVE):
                    x = motionEvent.getX();
                    y = motionEvent.getY();
                    currentX = view.getX();
                    currentY = view.getY();
                    view.setX(x + currentX - dpToPx(9));
                    view.setY(y + currentY - dpToPx(9));
                    return true;
                case (MotionEvent.ACTION_UP):
                    //snap to nearest 0 (71), 260, 496, 648 /3.5
                    x = motionEvent.getX();
                    currentX = view.getX();
                    ArrayList<Float> locations = new ArrayList<>();
                    locations.add((float)dpToPx(19));
                    locations.add((float)dpToPx(74));
                    locations.add((float)dpToPx(142));
                    locations.add((float)dpToPx(185));
                    float minval = locations.get(0);
                    int min = 0;
                    for(int i = 1;i < locations.size();i++){
                        if (Math.pow(minval-(x+currentX), 2) > Math.pow(locations.get(i)-(x+currentX), 2)){
                            minval = locations.get(i);
                            min = i;
                        }
                    }
                    view.setX(minval);
                    if(min == 0) {
                        view.setY(dpToPx(67)); //55
                        if(text.getText().equals("Nuuvut")){
                            if(strings[0].equals("Nuuvut")){
                                success(0,0);
                            }
                            else if(strings[1].equals("Nuuvut")){
                                success(1,0);
                            }
                            else{
                                success(2,0);
                            }
                        }
                        else{
                            successScore--;
                            if(strings[0].equals("Nuuvut")){
                                swap(0,0);
                            }
                            else if(strings[1].equals("Nuuvut")){
                                swap(1,0);
                            }
                            else{
                                swap(2,0);
                            }
                        }
                    }
                    else if(min == 1) {
                        view.setY(dpToPx(43));
                        if(text.getText().equals("Squishishi")){
                            if(strings[0].equals("Squishishi")){
                                success(0,0);
                            }
                            else if(strings[1].equals("Squishishi")){
                                success(1,0);
                            }
                            else{
                                success(2,0);
                            }
                        }
                        else{
                            successScore--;
                            if(strings[0].equals("Squishishi")){
                                swap(0,0);
                            }
                            else if(strings[1].equals("Squishishi")){
                                swap(1,0);
                            }
                            else{
                                swap(2,0);
                            }
                        }
                    }
                    else if(min == 2) {
                        view.setY(dpToPx(73));
                        if(text.getText().equals("Voop")){
                            if(strings[0].equals("Voop")){
                                success(0,0);
                            }
                            else if(strings[1].equals("Voop")){
                                success(1,0);
                            }
                            else{
                                success(2,0);
                            }
                        }
                        else{
                            successScore--;
                            if(strings[0].equals("Voop")){
                                swap(0,0);
                            }
                            else if(strings[1].equals("Voop")){
                                swap(1,0);
                            }
                            else{
                                swap(2,0);
                            }
                        }
                    }
                    else {
                        view.setY(dpToPx(49));
                        if(text.getText().equals("Bo")){
                            if(strings[0].equals("Bo")){
                                success(0,0);
                            }
                            else if(strings[1].equals("Bo")){
                                success(1,0);
                            }
                            else{
                                success(2,0);
                            }
                        }
                        else{
                            successScore--;
                            if(strings[0].equals("Bo")){
                                swap(0,0);
                            }
                            else if(strings[1].equals("Bo")){
                                swap(1,0);
                            }
                            else{
                                swap(2,0);
                            }
                        }
                    }
                    return true;
                default:
                    return false;
            }
        }
        else
            return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch(compoundButton.getId()){
            case R.id.switch1:
                if(text.getText().equals(strings[3])){
                    success(3, 2);
                }
                else{
                    successScore--;
                    swap(3, 2);
                }
                break;
            case R.id.switch2:
                if(text.getText().equals(strings[4])){
                    success(4, 3);
                }
                else{
                    successScore--;
                    swap(4, 3);
                }
        }
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
            editor.commit();
            currentFragment = new FragmentLevel3A(); //randomize?
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
            currentFragment = new FragmentLevel3A(); //randomize?
            switchToNewScreen();
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