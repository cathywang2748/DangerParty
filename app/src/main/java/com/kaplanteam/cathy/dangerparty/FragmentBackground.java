package com.kaplanteam.cathy.dangerparty;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kaplanteam.cathy.dangerparty.Level1.FragmentLevel1A;
import com.kaplanteam.cathy.dangerparty.Level1.FragmentLevel1B;

/**
 * Created by Cole on 3/24/18.
 */

public class FragmentBackground extends Fragment {
    private TypeWriter tw;
    private Button ready;
    private Fragment currentFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.background, container, false);

        ready = rootView.findViewById(R.id.button_ready);
        tw = rootView.findViewById(R.id.tv);

        //sets typewriter
        tw.setText("");
        tw.setCharacterDelay(70);
        tw.animateText("Welcome to Danger Island. \n" +
                "You came here for a party, and you \nwill get a party.\n" +
                "Enter if you dare!\n" +
                "There is one way to leave…\n" +
                "But you must follow the directions \nand work as a team to get out.\n" +
                "Are you ready?\n");

        //sets button
        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Math.random() < 0.5){//change later----------------------------------------------------
                    currentFragment = new FragmentLevel1A();
                    switchToNewScreen();
                }
                else{
                    currentFragment = new FragmentLevel1B();
                    switchToNewScreen();
                }
            }
        });

        //sets button visible after text finishes typing
        CountDownTimer timer = new CountDownTimer(33000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
               //ready.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                //ready.setVisibility(View.VISIBLE);

            }
         }.start();

        return rootView;
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
