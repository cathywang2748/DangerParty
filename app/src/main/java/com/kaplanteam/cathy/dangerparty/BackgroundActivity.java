package com.kaplanteam.cathy.dangerparty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Cole on 3/24/18.
 */

public class BackgroundActivity extends AppCompatActivity {
    //private TextSwitcher textSw;
    private TextView introText, textSwTextView;
    private int ii;
    private ArrayList<String> intro;

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent i = getIntent();
        ii=0;

        setIntro();
        //textSw =  (TextSwitcher) findViewById(R.id.textSwitcher);
        /*textSw.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                introText = (TextView) findViewById(R.id.textView_intro);
                introText.setLayoutParams(new TextSwitcher
                        .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                return introText;
            }
        });*/

        CountDownTimer timer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long l) {
                //textSwTextView = (TextView) textSw.getChildAt(0);
                Log.d("CountDownTimer: ", "" + l);

            }

            @Override
            public void onFinish() {
                //textSwTextView.setText(intro.get(ii));
                //ii++;
                Intent i = new Intent(BackgroundActivity.this, GameActivity.class);
                startActivity(i);
            }
        }.start();

    }

    private void setIntro() {
        intro = new ArrayList<>();
        intro.add("Welcome to Danger Island");
        intro.add("You came here for a party, and you will get a party...");
        intro.add("Enter if you dare!");
        intro.add("There is one way to leave...");
        intro.add("But you must follow the directions and work as a team to survive.");
        intro.add("Are you ready?");
    }
}
