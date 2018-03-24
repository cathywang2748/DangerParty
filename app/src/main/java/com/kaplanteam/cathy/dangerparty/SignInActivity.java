package com.kaplanteam.cathy.dangerparty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private SignInButton signInButton;
    public static final int RC_SIGN_IN = 1;
    private GoogleSignInClient c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        // Build a GoogleSignInClient with the options specified by gso.
        c = GoogleSignIn.getClient(this, gso);

        skipToGame();
    }

    private void skipToGame() {
        TextView title = findViewById(R.id.textView_title);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, GameActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);

    }

    private void updateUI(GoogleSignInAccount account) {
        //hide the sign-in button,
        // launch your main activity,
        Intent i = new Intent(SignInActivity.this, SetUpActivity.class);
        startActivity(i);
        // or whatever is appropriate for your app.
    }

    private void signIn() {
        Intent signInIntent = c.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("TAG", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
            //hello
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }
}
