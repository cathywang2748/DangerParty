package com.kaplanteam.cathy.dangerparty.Level3;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaplanteam.cathy.dangerparty.R;

/**
 * Created by Cole on 4/1/18.
 */

public class FragmentLevel3B extends Fragment implements View.OnClickListener {
    private Button woo, bum, banana, boom;
    private ImageView lFork, rFork, dynamite, spark, explosion, caveWall;
    private boolean lightOn;
    private ConstraintLayout background;
    private TextView scream;

    private SharedPreferences counter;
    private SharedPreferences.Editor editor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.level3b, container, false);

        counter = getActivity().getSharedPreferences("HELLO", Context.MODE_PRIVATE);
        editor = counter.edit();

        //wire any widgets -- must use rootView.findViewById

        lightOn = true;
        wireWidgets(rootView);
        setListeners();

        //get any other initial set up done

        //return the view that we inflated
        return rootView;
    }

    private void wireWidgets(View rootView) {
        woo = rootView.findViewById(R.id.button_woo);
        bum = rootView.findViewById(R.id.button_bum);
        banana = rootView.findViewById(R.id.button_banana);
        boom = rootView.findViewById(R.id.button_boom);
        lFork = rootView.findViewById(R.id.imageView_fork_left);
        rFork = rootView.findViewById(R.id.imageView_fork_right);
        dynamite = rootView.findViewById(R.id.imageView_dynamite);
        spark = rootView.findViewById(R.id.imageView_spark);
        explosion = rootView.findViewById(R.id.imageView_explosion);
        caveWall = rootView.findViewById(R.id.imageView_cave_drawings);
        background = rootView.findViewById(R.id.level3b_background);
        scream = rootView.findViewById(R.id.textView_scream);
    }

    private void setListeners() {
        woo.setOnClickListener(this);
        bum.setOnClickListener(this);
        banana.setOnClickListener(this);
        boom.setOnClickListener(this);
        lFork.setOnClickListener(this);
        rFork.setOnClickListener(this);
        dynamite.setOnClickListener(this);
        caveWall.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_woo:
                break;
            case R.id.button_banana:
                break;
            case R.id.button_bum:
                break;
            case R.id.button_boom:
                break;
            case R.id.imageView_dynamite:
                spark.setVisibility(View.VISIBLE);
                CountDownTimer timers = new CountDownTimer(6000, 1000) {
                    @Override
                    public void onTick(long l) {
                        if(l > 3500) {
                        }
                        else if(l > 2500)
                        {
                            explosion.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            spark.setVisibility(View.INVISIBLE);
                            explosion.setVisibility(View.INVISIBLE);
                            dynamite.setVisibility(View.INVISIBLE);
                        }

                    }

                    @Override
                    public void onFinish() {
                        dynamite.setVisibility(View.VISIBLE);
                    }
                }.start();
                break;
            case R.id.imageView_cave_drawings:
                lightOn = !lightOn;
                lightChange();
                break;
            case R.id.imageView_fork_left:
                break;
            case R.id.imageView_fork_right:
                break;
        }
    }

    private void lightChange(){
        if(lightOn){
            caveWall.setImageResource(R.drawable.cave_drawings);
            background.setBackgroundColor(Color.WHITE);
            scream.setTextColor(Color.BLACK);
        }
        else{
            caveWall.setImageResource(R.drawable.glow_worms);
            background.setBackgroundColor(Color.BLACK);
            scream.setTextColor(Color.WHITE);
        }
    }

}