package com.kaplanteam.cathy.dangerparty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by per6 on 3/15/18.
 */

public class SetUpActivity extends AppCompatActivity {
    private static final int RC_SELECT_PLAYERS = 9006;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        Intent i = getIntent();
    }

    private void invitePlayers() {
        // launch the player selection screen
        // minimum: 1 other player; maximum: 3 other players
        Games.getRealTimeMultiplayerClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getSelectOpponentsIntent(1, 3, true)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_SELECT_PLAYERS);
                    }
                });
    }
}
