package com.kaplanteam.cathy.dangerparty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Cole on 3/22/18.
 */

public class GameActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level1a); //later change to if statement or something
        Intent i = getIntent();


    }
}
