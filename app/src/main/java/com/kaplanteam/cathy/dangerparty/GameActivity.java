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

public class GameActivity extends AppCompatActivity {
    private TextSwitcher textSw;
    private TextView introText, textSwTextView;
    private int ii;
    private ArrayList<String> intro;

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
}
