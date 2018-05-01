package com.kaplanteam.cathy.dangerparty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title;

    private SignInButton signInButton;
    private Button signOutButton;
    private FirebaseAuth auth;
    public static final String TAG = "SignInActivity";
    public static final int RC_SIGN_IN = 1;
    private static final int RC_INVITATION_INBOX = 9008;


    private GoogleSignInClient c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        title = findViewById(R.id.textView_title);
        title.setOnClickListener(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.


        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        signOutButton = findViewById(R.id.sign_out_button);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
        // Build a GoogleSignInClient with the options specified by gso.
        c = GoogleSignIn.getClient(this, gso);

        auth = FirebaseAuth.getInstance();


    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser account) {
        //hide the sign-in button,
        // launch your main activity,
        TextView displayName = findViewById(R.id.displayName);
        ImageView profileImage = findViewById(R.id.profilePic);
        if(account != null) {
            displayName.setText(account.getDisplayName());
            displayName.setVisibility(View.VISIBLE);
            //Loading profile image
            Uri profilePicUrl = account.getPhotoUrl();
            if (profilePicUrl != null) {
                Glide.with(this).load(profilePicUrl).into(profileImage);
            }
            profileImage.setVisibility(View.VISIBLE);
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
        }
        else {
            displayName.setVisibility(View.GONE);
            profileImage.setVisibility(View.GONE);
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        }

        //Intent i = new Intent(SignInActivity.this, SetUpActivity.class);
        //startActivity(i);
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
            try{
                GoogleSignInAccount accnt = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(accnt);
            }   catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    //firebase
    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider
                .getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Sign in success, update UI with the signed in user's information
                    Log.d("on complete","success");
                    FirebaseUser user = auth.getCurrentUser();
                    updateUI(user);
                    Intent i = new Intent(SignInActivity.this, SetUpActivity.class);
                    startActivity(i);

                } else {
                    //If sign in fails, display a message to the user
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    Toast.makeText(getApplicationContext(), "Login Failed: ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
//            updateUI(account);
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.d("TAG", "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
//            //hello
//        }
    //}

    private void showInvitationInbox() {
        Games.getInvitationsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getInvitationInboxIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_INVITATION_INBOX);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_title:
                Intent i = new Intent(SignInActivity.this, BackgroundActivity.class);
                startActivity(i);
                break;
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
        }
    }

    private void signOut() {
        //Firebase sign out
        // Firebase sign out
        auth.signOut();

        // Google sign out
        c.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }
}
