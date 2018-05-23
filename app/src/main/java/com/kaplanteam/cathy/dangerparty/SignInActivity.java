package com.kaplanteam.cathy.dangerparty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bluetooth, singlePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        bluetooth = findViewById(R.id.button_bluetooth);
        bluetooth.setOnClickListener(this);

        singlePlayer = findViewById(R.id.button_single_player);
        singlePlayer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_single_player:
                Intent i = new Intent(SignInActivity.this, GameActivity.class);
                startActivity(i);
                break;
            case R.id.button_bluetooth:
                Intent b = new Intent(SignInActivity.this, BluetoothActivity.class);
                startActivity(b);
                break;
        }
    }
}