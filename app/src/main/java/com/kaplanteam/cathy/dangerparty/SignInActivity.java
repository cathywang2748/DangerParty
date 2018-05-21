package com.kaplanteam.cathy.dangerparty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    private Button bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        title = findViewById(R.id.textView_title);
        title.setOnClickListener(this);

        bluetooth = findViewById(R.id.button_bluetooth);
        bluetooth.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_title:
                Intent i = new Intent(SignInActivity.this, BackgroundActivity.class);
                startActivity(i);
                break;
            case R.id.button_bluetooth:
                Intent b = new Intent(SignInActivity.this, BluetoothActivity.class);
                startActivity(b);
                break;
        }
    }
}