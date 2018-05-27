package com.example.troyphattrinh.fitness_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    //used to debug and keep track of the steps
    private static final String TAG = "importantMessage";
    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);

        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        printKeyHash();

        callbackManager = CallbackManager.Factory.create();

        profileTracker = new ProfileTracker()
        {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile)
            {
                newActivity(newProfile);
            }
        };


        accessTokenTracker = new AccessTokenTracker()
        {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken)
            {
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        LoginButton loginButton = findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                Profile profile = Profile.getCurrentProfile();
                newActivity(profile);
            }

            @Override
            public void onCancel()
            {
            }

            @Override
            public void onError(FacebookException error)
            {
            }

        });

        Log.i(TAG,"onCreate");
    }

    public void clickRegButton(View v){
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        TextView errorText = findViewById(R.id.loginError);
        errorText.setText("");
        startActivity(intent);
    }

    public void clickLoginButton(View v){
        EditText emailText = findViewById(R.id.emailText);
        EditText passwordText = findViewById(R.id.passwordText);
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        final TextView errorText = findViewById(R.id.loginError);

        if(email.isEmpty() || password.isEmpty()){
            errorText.setText("Invalid login details!!!");
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        if (checkEmailVerification()) {
                            errorText.setText("");
                        } else {
                            errorText.setText("Email not verified!!!");
                        }
                    } else {
                        errorText.setText("Incorrect login details!!!");
                    }
                }
            });
        }
    }


    //check if the email has already been confirmed
    private boolean checkEmailVerification(){
        Boolean verified = false;
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        String emailString = firebaseUser.getEmail().toString();


        if(firebaseUser.isEmailVerified()){
            verified = true;
            finish();

            Intent i = new Intent(MainActivity.this, MainMenuActivity.class);
            i.putExtra("normalEmail", emailString);

            startActivity(i);
        }
        else{
            Toast.makeText(MainActivity.this, "PLEASE VERIFY YOUR EMAIL", Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
        }

        return verified;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        newActivity(profile);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    protected void onStop()
    {
        super.onStop();
        profileTracker.stopTracking();
        accessTokenTracker.stopTracking();
    }


    private void newActivity(Profile profile)
    {
        if(profile != null)
        {
            Intent main = new Intent(MainActivity.this, FbMainMenuActivity.class);
           // main.putExtra("email", profile.getLinkUri());
            main.putExtra("name", profile.getFirstName());
            main.putExtra("avatar", profile.getProfilePictureUri(400,350).toString());
            finish();
            startActivity(main);
        }
    }


    private void printKeyHash()
    {
        try
        {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.troyphattrinh.fitness_app", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }

}