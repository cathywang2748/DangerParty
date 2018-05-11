package com.kaplanteam.cathy.dangerparty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.kaplanteam.cathy.dangerparty.Level1.FragmentLevel1A;

/**
 * Created by Cole on 3/22/18.
 */

public class GameActivity extends AppCompatActivity {
    private Fragment currentFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game); //later change to if statement or something
        Intent i = getIntent();

        if(Math.random() < 1){//change later
            currentFragment = new FragmentLevel1A();
            switchToNewScreen();
        }
        else{
           currentFragment = new FragmentLevel1A();
            switchToNewScreen();
        }
    }

    private void switchToNewScreen() {
        //tell the fragment manager that if our current fragment isn't null, to replace whatever is there with it
        FragmentManager fm = getSupportFragmentManager();
        if (currentFragment != null) {
            fm.beginTransaction()
                    .replace(R.id.fragment_container, currentFragment)
                    .commit();
        }
    }
}
