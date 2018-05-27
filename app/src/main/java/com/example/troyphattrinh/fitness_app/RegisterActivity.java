package com.example.troyphattrinh.fitness_app;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;


public class RegisterActivity extends AppCompatActivity {

    DatabaseHelper dbh;
    private FirebaseAuth firebaseAuth;
    private EditText usernameText, passwordText, confirmPasswordText, emailText;
    private TextView dobText, errorText;
    private DatePickerDialog.OnDateSetListener dateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        usernameText = findViewById(R.id.username_textField);
        passwordText = findViewById(R.id.password_textField);
        confirmPasswordText = findViewById(R.id.confirm_password_textField);
        emailText = findViewById(R.id.email_textField);
        dobText = findViewById(R.id.dob_textView);
        errorText = findViewById(R.id.error_textView);


        dobText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
                        dateSetListener,
                        day, month, year);
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month +1;

                String date = dayOfMonth + "/" + month + "/" + year;
                dobText.setText(date);
            }

        };


    }

    public void clickRegButton(View v){

        String username = usernameText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String confirmPassword = confirmPasswordText.getText().toString().trim();
        String dob = dobText.getText().toString().trim();
        String email = emailText.getText().toString().trim();

        if(     checkUsername(username) &&
                checkPassword(password) &&
                checkConfirmPassword(confirmPassword, password) &&
                dob.length() != 0 &&
                email.length() != 0 ) {

            //add user info to a local database
            dbh = new DatabaseHelper(this);
            boolean success = dbh.addUser(username, password, dob, email);

            if(success){
                //create an account on Firebase server (this feature is just for sending verification email)
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            sendVerificationEmail();
                        }
                        else{
                            errorText.setText("User email already exist in the database");
                        }
                    }
                });
            }
            else{
                Toast.makeText(RegisterActivity.this, "ERROR", Toast.LENGTH_LONG).show();
            }

            errorText.setText("");
        }
        else{
            errorText.setText("Invalid input!!! Please enter again.");
        }
    }


    private boolean checkUsername(String username){
        return username.length() >= 1;
    }

    /*checks correct requirement for password (minimum 8 characters)*/
    private boolean checkPassword(String password){
        return password.length() >= 5;
    }

    /*compare password and confirmPassword*/
    private boolean checkConfirmPassword(String confirmPassword, String password){

        return confirmPassword.equals(password);

    }

    private void sendVerificationEmail(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Register successfully!!!", Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        finish();
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Failed to send verification email.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}