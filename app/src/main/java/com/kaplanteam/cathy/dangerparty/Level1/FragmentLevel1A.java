package com.kaplanteam.cathy.dangerparty.Level1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.kaplanteam.cathy.dangerparty.R;

/**
 * Created by Cole on 3/24/18.
 */

public class FragmentLevel1A extends Fragment implements View.OnTouchListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Button octopus, doggiePaddle, corkscrew, dolphin, elegantUndulations;
    private Switch drown, pressbutton;
    private ImageView bubbleS, bubbleM, bubbleL, vortex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.level1a, container, false);

        //wire any widgets -- must use rootView.findViewById

        wireWidgets(rootView);
        setListeners();

        //get any other initial set up done

        //return the view that we inflated
        return rootView;
    }

    private void wireWidgets(View rootView) {
        octopus = rootView.findViewById(R.id.button_octopus);
        doggiePaddle = rootView.findViewById(R.id.button_doggie_paddle);
        corkscrew = rootView.findViewById(R.id.button_corkscrew);
        dolphin = rootView.findViewById(R.id.button_dolphin);
        elegantUndulations = rootView.findViewById(R.id.button_elegant_undulations);
        drown = rootView.findViewById(R.id.switch_drown);
        pressbutton = rootView.findViewById(R.id.switch_pressbutton);
        bubbleS = rootView.findViewById(R.id.imageView_bubble_small);
        bubbleM = rootView.findViewById(R.id.imageView_bubble_medium);
        bubbleL = rootView.findViewById(R.id.imageView_bubble_large);
        vortex = rootView.findViewById(R.id.imageView_vortex);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
        octopus.setOnClickListener(this);
        doggiePaddle.setOnClickListener(this);
        corkscrew.setOnClickListener(this);
        dolphin.setOnClickListener(this);
        elegantUndulations.setOnClickListener(this);
        drown.setOnCheckedChangeListener(this);
        pressbutton.setOnCheckedChangeListener(this);
        bubbleS.setOnClickListener(this);
        bubbleM.setOnClickListener(this);
        bubbleL.setOnClickListener(this);
        vortex.setOnTouchListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}