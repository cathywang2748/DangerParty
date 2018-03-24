package com.kaplanteam.cathy.dangerparty;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

/**
 * Created by Cole on 3/22/18.
 */

public class GameActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private double angle;
    private TextSwitcher textSw;
    private TextView introText, textSwTextView;
    private int ii;
    private ArrayList<String> intro;

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level1a); //later change to if statement or something
        Intent i = getIntent();
        ii=0;

        setIntro();
        textSw =  (TextSwitcher) findViewById(R.id.textSwitcher);
        textSw.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                introText = (TextView) findViewById(R.id.textView_intro);
                introText.setLayoutParams(new TextSwitcher
                        .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                return introText;
            }
        });

        CountDownTimer timer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long l) {
                textSwTextView = (TextView) textSw.getChildAt(0);

            }

            @Override
            public void onFinish() {
                textSwTextView.setText(intro.get(ii));
                ii++;
            }
        };

        final ImageView needle = findViewById(R.id.imageView_compass_needle);
        angle = 0;
        needle.setRotation((float)angle);
        needle.setOnTouchListener(this);

        wireWidgets();
        setOnClickListeners();

    }

    private void setIntro() {
        intro.add(0,"Welcome to Danger Island");
        intro.add(1, "You came here for a party, and you will get a party...");
        intro.add(2,"Enter if you dare!");
        intro.add(3,"There is one way to leave…");
        intro.add(4,"But you must follow the directions " +
                "and work as a team to get out.");
        intro.add(5,"Are you ready?");
    }

    private void wireWidgets() {

    }

    private void setOnClickListeners() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = MotionEventCompat.getActionMasked(motionEvent);

        if(view.getId() == R.id.imageView_compass_needle){
            switch(action) {
                case (MotionEvent.ACTION_DOWN) :
                    Log.d("Motion Event: ","Action was DOWN");
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
                        angle = theta + angle;
                        view.setRotation((float)angle);
                    }
                    return true;
                case (MotionEvent.ACTION_UP) :
                    angle = Math.round(angle/45)*45;
                    view.setRotation((float)angle);

                    Log.d("Motion Event: ","Action was UP");
                    return true;
                default :
                    return false;
            }
        }
        else if (view.getId() == R.id.imageView_x){

            return true;
        }
        else
            return false;
    }
}
