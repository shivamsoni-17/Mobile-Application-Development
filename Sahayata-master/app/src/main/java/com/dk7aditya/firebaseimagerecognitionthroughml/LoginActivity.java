package com.dk7aditya.firebaseimagerecognitionthroughml;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();


        TextView userEmailForLogin = findViewById(R.id.userEmailForLogin);
        TextView passWordForLogin = findViewById(R.id.passWordForLogin);
        Button logIn = findViewById(R.id.logIn);
        TextView signUpPageText = findViewById(R.id.signUpPageText);
        TextView forgotPasswordLogin = findViewById(R.id.forgotPasswordLogin);
        logIn.setEnabled(false);

        userEmailForLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                logIn.setEnabled(!s.toString().isEmpty() && !passWordForLogin.getText().toString().isEmpty());
            }
        });

        passWordForLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                logIn.setEnabled(!s.toString().isEmpty() && !userEmailForLogin.getText().toString().isEmpty());
            }
        });

        logIn.setOnClickListener(v->{
                Log.d(TAG, "onClick: No empty fields for Login");
                mAuth.signInWithEmailAndPassword(userEmailForLogin.getText().toString(),passWordForLogin.getText().toString())
                        .addOnCompleteListener(task->{
                           if(task.isSuccessful()){
                               Intent homeActivityIntent = new Intent(LoginActivity.this, HomeActivity.class);
                               homeActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               homeActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                               startActivity(homeActivityIntent);
                               overridePendingTransition(0,0);
                           }else{
                               Log.w(TAG, "signInWithEmail:failure", task.getException());
                               Toast.makeText(LoginActivity.this, "Authentication failed.",
                                       Toast.LENGTH_SHORT).show();
                           }
                        });

        });

        signUpPageText.setOnClickListener(v -> {
                Intent SignUpPage = new Intent(LoginActivity.this, SignUpActivity.class);
                SignUpPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SignUpPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(SignUpPage);
                overridePendingTransition(0,0);

        });

        forgotPasswordLogin.setOnClickListener(v -> {
            Intent forgotPasswordIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(forgotPasswordIntent);
            overridePendingTransition(0,0);
        });


    }
}