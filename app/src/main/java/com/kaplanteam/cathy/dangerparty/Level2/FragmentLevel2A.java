package com.kaplanteam.cathy.dangerparty.Level2;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.kaplanteam.cathy.dangerparty.R;

import java.util.ArrayList;

/**
 * Created by Cole on 3/24/18.
 */

public class FragmentLevel2A extends Fragment implements View.OnTouchListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private double angle;
    private Switch getLostSwitch, zzyxzSwitch;
    private Button rub, hug;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.level2a, container, false);

        //wire any widgets -- must use rootView.findViewById
        angle = 0;
        final ImageView needle = rootView.findViewById(R.id.imageView_compass_needle);
        final ImageView xMark = rootView.findViewById(R.id.imageView_x);
        needle.setOnTouchListener(this);
        xMark.setOnTouchListener(this);

        wireWidgets(rootView);
        setListeners();

        //get any other initial set up done

        //return the view that we inflated
        return rootView;
    }

    private void wireWidgets(View rootView) {
        rub = rootView.findViewById(R.id.button_rub);
        hug = rootView.findViewById(R.id.button_hug);
        getLostSwitch = rootView.findViewById(R.id.switch1);
        zzyxzSwitch = rootView.findViewById(R.id.switch2);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
        rub.setOnClickListener(this);
        hug.setOnClickListener(this);
        getLostSwitch.setOnCheckedChangeListener(this);
        zzyxzSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button_rub:
                break;
            case R.id.button_hug:
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = MotionEventCompat.getActionMasked(motionEvent);

        if(view.getId() == R.id.imageView_compass_needle){
            switch(action) {
                case (MotionEvent.ACTION_DOWN) :
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
                    return true;
                default :
                    return false;
            }
        }
        else if (view.getId() == R.id.imageView_x){
            float x, y, currentX, currentY;
            switch(action) {
                case (MotionEvent.ACTION_DOWN):
                    return true;
                case (MotionEvent.ACTION_MOVE):
                    x = motionEvent.getX();
                    y = motionEvent.getY();
                    currentX = view.getX();
                    currentY = view.getY();
                    view.setX(x + currentX - dpToPx(9));
                    view.setY(y + currentY - dpToPx(9));
                    return true;
                case (MotionEvent.ACTION_UP):
                    //snap to nearest 0 (71), 260, 496, 648 /3.5
                    x = motionEvent.getX();
                    currentX = view.getX();
                    ArrayList<Float> locations = new ArrayList<>();
                    locations.add((float)dpToPx(19));
                    locations.add((float)dpToPx(74));
                    locations.add((float)dpToPx(142));
                    locations.add((float)dpToPx(185));
                    float minval = locations.get(0);
                    int min = 0;
                    for(int i = 1;i < locations.size();i++){
                        if (Math.pow(minval-(x+currentX), 2) > Math.pow(locations.get(i)-(x+currentX), 2)){
                            minval = locations.get(i);
                            min = i;
                        }
                    }
                    Toast.makeText(getContext(), "" + Resources.getSystem().getDisplayMetrics().density, Toast.LENGTH_SHORT).show();
                    view.setX(minval);
                    if(min == 0)
                        view.setY(dpToPx(67)); //55
                    else if(min == 1)
                        view.setY(dpToPx(43));
                    else if(min == 2)
                        view.setY(dpToPx(73));
                    else
                        view.setY(dpToPx(49));
                    return true;
                default:
                    return false;
            }
        }
        else
            return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch(compoundButton.getId()){
            case R.id.switch1:
                break;
            case R.id.switch2:
                break;
        }
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}