package com.kaplanteam.cathy.dangerparty.Level3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kaplanteam.cathy.dangerparty.R;

/**
 * Created by Cole on 4/1/18.
 */

public class FragmentLevel3B extends Fragment implements View.OnClickListener {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.level3b, container, false);

        //wire any widgets -- must use rootView.findViewById


        wireWidgets(rootView);
        setListeners();

        //get any other initial set up done

        //return the view that we inflated
        return rootView;
    }

    private void wireWidgets(View rootView) {
    }

    private void setListeners() {
    }

    @Override
    public void onClick(View view) {
    }

}