package com.dk7aditya.firebaseimagerecognitionthroughml;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ForgotPassword";
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        auth = FirebaseAuth.getInstance();
        TextView emailAddressForRecovery = findViewById(R.id.emailAddressForRecovery);
        Button sendEmailConfirmation = findViewById(R.id.sendEmailConfirmation);
        sendEmailConfirmation.setEnabled(false);
        emailAddressForRecovery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    sendEmailConfirmation.setEnabled(!s.toString().isEmpty());
            }
        });
        sendEmailConfirmation.setOnClickListener(v -> {
            auth.sendPasswordResetEmail(emailAddressForRecovery.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent loginActivity = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                startActivity(loginActivity);
                                overridePendingTransition(0,0);
                                Toast.makeText(ForgotPasswordActivity.this,"Recovery link sent to email",Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(ForgotPasswordActivity.this,"Error Occurred, please try again.",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });





        });
    }
}