package com.kaplanteam.cathy.dangerparty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Cole on 3/24/18.
 */

public class FragmentLevel1B extends Fragment implements View.OnClickListener {
    private Button climb, pick, caress, burn, flick, mf1, mf2, mf3, mf4;
    private ImageView bottle;
    private boolean full;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.level1b, container, false);

        full = false;
        wireWidgetsB(rootView);
        setListenersB();

        return rootView;
    }

    private void wireWidgetsB(View rootView) {
        climb = rootView.findViewById(R.id.button1);
        pick = rootView.findViewById(R.id.button2A);
        caress = rootView.findViewById(R.id.button2B);
        burn = rootView.findViewById(R.id.button2C);
        flick = rootView.findViewById(R.id.button2D);
        mf1 = rootView.findViewById(R.id.button3A);
        mf2 = rootView.findViewById(R.id.button3B);
        mf3 = rootView.findViewById(R.id.button3C);
        mf4 = rootView.findViewById(R.id.button3D);
        bottle = rootView.findViewById(R.id.imageView_bottle);
    }

    private void setListenersB() {
        climb.setOnClickListener(this);
        pick.setOnClickListener(this);
        caress.setOnClickListener(this);
        burn.setOnClickListener(this);
        flick.setOnClickListener(this);
        mf1.setOnClickListener(this);
        mf2.setOnClickListener(this);
        mf3.setOnClickListener(this);
        mf4.setOnClickListener(this);
        bottle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button1:
                break;
            case R.id.button2A:
                break;
            case R.id.button2B:
                break;
            case R.id.button2C:
                break;
            case R.id.button2D:
                break;
            case R.id.button3A:
                break;
            case R.id.button3B:
                break;
            case R.id.button3C:
                break;
            case R.id.button3D:
                break;
            case R.id.imageView_bottle:
                if(full){
                    view.setBackgroundResource(R.drawable.bottle_empty);
                }
                else{
                    view.setBackgroundResource(R.drawable.bottle_full);
                }
                full = !full;
                break;
        }
    }
}
