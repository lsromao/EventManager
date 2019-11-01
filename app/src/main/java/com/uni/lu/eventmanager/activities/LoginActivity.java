package com.uni.lu.eventmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.controller.FirebaseController;

import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN_GOOGLE = 9001;
    private static final int RC_SIGN_IN_EMAIL = 9000;
    private static final int RC_SIGN_IN_CREATE = 1;

    // [START declare_auth]
    protected FirebaseAuth mAuth;
    // [END declare_auth]

    protected GoogleSignInClient mGoogleSignInClient;
    private SignInButton mBtnGoogleLogin;
    private Button mBtnLogin;
    private Button mBtnRegister;
    private TextView email;
    private TextView password;
    private FirebaseController FireController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // [START initialize_auth]
        // Initialize Firebase Auth
        FireController =  FirebaseController.getInstance();
        FireController.setmAuth(FirebaseAuth.getInstance());
        //mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        //Google Login Option - TODO: Refactor to abstract the class, single responsability

        mBtnGoogleLogin = findViewById(R.id.btn_GoogleLogin);
        mBtnRegister = findViewById(R.id.register);
        mBtnLogin = findViewById(R.id.login);
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);

        // [START config_signin]
        // Configure Google Sign In

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        FireController.setmGoogleSignInClient(GoogleSignIn.getClient(this, gso));
        //mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        mBtnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               signIn();
            }
        });
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signInEmail();
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createUser();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TEST", "Google sign in failed", e);
                // ...
            }
        } else if (requestCode == RC_SIGN_IN_EMAIL){
            Pattern pattern = Patterns.EMAIL_ADDRESS;
            if (!pattern.matcher(email.getText()).matches())
            {
                Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();

            }else{
                FireController.getmAuth().signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("test", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("TEST", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
            }

        } else if (requestCode == RC_SIGN_IN_CREATE){
            Pattern pattern = Patterns.EMAIL_ADDRESS;
            if (!pattern.matcher(email.getText()).matches())
            {
                Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();

            }else{
                FireController.getmAuth().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("test", "signInWithEmail:success");
                                    //FirebaseUser user = mAuth.getCurrentUser();
                                    goToListPage();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("TEST", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TEST", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FireController.getmAuth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TEST", "signInWithCredential:success");
                            //Thread.sleep(100);
                            goToListPage();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TEST", "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FireController.getmAuth().getCurrentUser();
        if (currentUser != null)
        {
            goToListPage();
        }

    }
    // [END on_start_check_user]

    private void goToListPage() {
        Intent home = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(home);
    }

    private void signIn() {
        Intent signInIntent = FireController.getmGoogleSignInClient().getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    private void signInEmail() {
        Intent signInEmail = new Intent(LoginActivity.this.getIntent());
        startActivityForResult(signInEmail , RC_SIGN_IN_EMAIL);
    }

    private void createUser() {
        Intent createUser = new Intent(LoginActivity.this.getIntent());
        startActivityForResult(createUser , RC_SIGN_IN_CREATE);
    }
}

