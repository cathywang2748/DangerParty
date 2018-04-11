package com.kaplanteam.cathy.dangerparty;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Cole on 3/24/18.
 */

public class BackgroundActivity extends AppCompatActivity {
    //private TextSwitcher textSw;
    private TextView introText, textSwTextView;
    private TypeWriter tw;
    private Button ready;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.background);
        Intent i = getIntent();

        ready = (Button) findViewById(R.id.button_ready);
        tw = (TypeWriter) findViewById(R.id.tv);

        tw.setText("");
        tw.setCharacterDelay(150);
        tw.animateText("Welcome to Danger Island. \n" +
                "You came here for a party, and you \nwill get a party.\n" +
                "Enter if you dare!\n" +
                "There is one way to leave…\n" +
                "But you must follow the directions \nand work as a team to get out.\n" +
                "Are you ready?\n");

        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BackgroundActivity.this, GameActivity.class);
                startActivity(i);
            }
        });

        //sets button visible after text finishes typing
        CountDownTimer timer = new CountDownTimer(33000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
               ready.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                ready.setVisibility(View.VISIBLE);

            }
         }.start();




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

//        CountDownTimer timer = new CountDownTimer(2000, 1000) {
//            @Override
//            public void onTick(long l) {
//                //textSwTextView = (TextView) textSw.getChildAt(0);
//                Log.d("CountDownTimer: ", "" + l);
//
//            }
//
//            @Override
//            public void onFinish() {
//                //textSwTextView.setText(intro.get(ii));
//                //ii++;
//                Intent i = new Intent(BackgroundActivity.this, GameActivity.class);
//                startActivity(i);
//            }
//        }.start();

    }


}
